package com.kavrin.foody.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.viewmodels.MainViewModel
import com.kavrin.foody.adapters.RecipesAdapter
import com.kavrin.foody.databinding.FragmentRecipesBinding
import com.kavrin.foody.util.NetworkResult
import com.kavrin.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val mMainViewModel: MainViewModel by viewModels()
    private val mRecipesViewModel: RecipesViewModel by viewModels()

    private val mAdapter: RecipesAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentRecipesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        requestApiData()

        return view
    }

    private fun requestApiData() {
        mMainViewModel.getRecipes(mRecipesViewModel.applyQueries())
        mMainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}