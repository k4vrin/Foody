package com.kavrin.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.R
import com.kavrin.foody.viewmodels.MainViewModel
import com.kavrin.foody.adapters.RecipesAdapter
import com.kavrin.foody.databinding.FragmentRecipesBinding
import com.kavrin.foody.util.Constants.ROW_DEFAULT_ID
import com.kavrin.foody.util.NetworkResult
import com.kavrin.foody.util.observeOnce
import com.kavrin.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
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
class RecipesFragment : Fragment() {

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

        // Show data or error
        readDatabase()

        // Navigate to BottomSheet
        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
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
                    Log.d("RecipesFragment", "ReadDatabase Called")
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
        Log.d("RecipesFragment", "RequestApiData Called")
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
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        response.message.toString(), // The message is handled by NetworkResult in MainViewModel
                        Snackbar.LENGTH_SHORT
                    ).show()
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

    /**********************************************************************************************/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}