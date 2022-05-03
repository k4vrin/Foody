package com.kavrin.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.viewmodels.MainViewModel
import com.kavrin.foody.adapters.RecipesAdapter
import com.kavrin.foody.databinding.FragmentRecipesBinding
import com.kavrin.foody.util.NetworkResult
import com.kavrin.foody.util.observeOnce
import com.kavrin.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val mMainViewModel: MainViewModel by viewModels()
    private val mRecipesViewModel: RecipesViewModel by viewModels()

    private val mAdapter: RecipesAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup binding
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize RecyclerView
        setUpRecyclerView()

        readDatabase()

        return view
    }

    /**********************************************************************************************/

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

    /**********************************************************************************************/

    private fun readDatabase() {
        lifecycleScope.launch {
            mMainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "ReadDatabase Called")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "RequestApiData Called")
        mMainViewModel.getRecipes(mRecipesViewModel.applyQueries())
        mMainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    /** Cache Data */
                    loadDataFromCache()
                    /** Cache Data */
                    hideShimmerEffect()
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        response.message.toString(),
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
            }
        }
    }

    /**********************************************************************************************/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}