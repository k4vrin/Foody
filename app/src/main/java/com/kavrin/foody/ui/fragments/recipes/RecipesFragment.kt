package com.kavrin.foody.ui.fragments.recipes

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.R
import com.kavrin.foody.viewmodels.MainViewModel
import com.kavrin.foody.adapters.RecipesAdapter
import com.kavrin.foody.databinding.FragmentRecipesBinding
import com.kavrin.foody.util.Constants.ROW_DEFAULT_ID
import com.kavrin.foody.util.NetworkListener
import com.kavrin.foody.util.NetworkResult
import com.kavrin.foody.util.observeOnce
import com.kavrin.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * Recipes fragment
 *
 * Once Hilt is set up in your Application class and an application-level component is available,
 * Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation.
 *
 * If you annotate an Android class with @AndroidEntryPoint, then you also must annotate Android classes
 * that depend on it. For example, if you annotate a fragment, then you must also annotate any activities
 * where you use that fragment.
 *
 * @AndroidEntryPoint generates an individual Hilt component for each Android class in your project.
 * These components can receive dependencies from their respective parent classes as described in Component hierarchy.
 *
 *  Note: The following exceptions apply to Hilt support for Android classes:
 *    Hilt only supports activities that extend ComponentActivity, such as AppCompatActivity.
 *    Hilt only supports fragments that extend androidx.Fragment.
 *    Hilt does not support retained fragments.
 */
@AndroidEntryPoint
class RecipesFragment : Fragment(),
    SearchView.OnQueryTextListener {// import androidx.appcompat.widget.SearchView

    /**
     * Warning: Even though the view model has an @Inject constructor,
     * it is an error to request it from Dagger directly (for example, via field injection)
     * since that would result in multiple instances.
     * View Models must be retrieved through the ViewModelProvider API.
     * This is checked at compile time by Hilt.
     */
    private val mMainViewModel: MainViewModel by activityViewModels()
    private val mRecipesViewModel: RecipesViewModel by activityViewModels()

    private val mAdapter: RecipesAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    // Receiving args from BottomSheet
    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup binding
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mMainViewModel = mMainViewModel
        val view = binding.root

        // Initialize RecyclerView
        setUpRecyclerView()

        /**
         * Report that this fragment would like to participate in populating the options menu
         * by receiving a call to onCreateOptionsMenu and related methods.
         */
        setHasOptionsMenu(true)

        // Observe backOnline and assign it to variable
        mRecipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mRecipesViewModel.backOnline = it
        }

        // Initialize NetworkListener
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkListener = NetworkListener()
                networkListener.checkNetworkAvailability(requireContext())
                    .collectLatest {
                        mRecipesViewModel.networkStatus = it
                        showNetworkStatus(it)
                        // Show data or error
                        readDatabase()
                    }
            }
        }

        // Navigate to BottomSheet
        binding.recipesFab.setOnClickListener {
            // If there is no internet the BottomSheet won't show up instead we show a snack bar
            if (mRecipesViewModel.networkStatus) findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            else showNetworkStatus(mRecipesViewModel.networkStatus)
        }

        return view
    }

    /************************************* RecyclerView *******************************************/

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView

        recyclerView.adapter = mAdapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }

    /************************************* ROOM Database *******************************************/

    private fun readDatabase() {
        lifecycleScope.launch {
            /**
             * Observe the database once
             * if it's not empty show it
             * else request data from api and cache it to database
             */
            mMainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[ROW_DEFAULT_ID].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    /*************************************** Retrofit **********************************************/

    private fun requestApiData() {
        mMainViewModel.getRecipes(mRecipesViewModel.applyQueries())
        // Observe response and act accordingly
        mMainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    /** Load Data From Cache */
                    // Data is cached in MainViewModel
                    loadDataFromCache()
                    /** Load Data From Cache */
                    hideShimmerEffect()
                    // The message is handled by NetworkResult in MainViewModel
                    showSnackBar(message = response.message.toString())
                }
                is NetworkResult.Loading -> showShimmerEffect()
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mMainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) mAdapter.setData(database[0].foodRecipe)
                // Else will be handled in RecipesBinding by make the visibility of error image and text visible
            }
        }
    }

    /*************  Search Response  ***************/

    private fun searchApiData(searchQuery: String) {

        mMainViewModel.searchRecipes(mRecipesViewModel.applySearchQueries(searchQuery))
        // Observe response and act accordingly
        mMainViewModel.searchRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    /** Load Data From Cache */
                    // Data is cached in MainViewModel
                    loadDataFromCache()
                    /** Load Data From Cache */
                    hideShimmerEffect()
                    // The message is handled by NetworkResult in MainViewModel
                    showSnackBar(message = response.message.toString())
                }
                is NetworkResult.Loading -> showShimmerEffect()
            }
        }
    }


    /**
     * On create options menu
     *
     * inflate search menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        // Find our menu item
        val search = menu.findItem(R.id.menu_search)
        // Find our widget
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            searchApiData(searchQuery = it)
        }
        return true
    }

    // We don't use it because it's expensive for performance
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }


    /**********************************************************************************************/

    /**
     * Show network status
     *
     * Display a snack based on the status(Network status)
     */
    private fun showNetworkStatus(status: Boolean) {
        if (!status) {
            Snackbar.make(
                requireContext(),
                binding.root,
                "No Internet Connection",
                Snackbar.LENGTH_SHORT
            ).setAction("Okay") {}
                .show()
            // It should become online
            mRecipesViewModel.saveBackOnline(true)
            // Internet is back & we were not online
        } else if (status && mRecipesViewModel.backOnline) {
            showSnackBar(message = "We are back online")
            // Prevent Snack bar to show again
            mRecipesViewModel.saveBackOnline(false)
        }
    }

    fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}