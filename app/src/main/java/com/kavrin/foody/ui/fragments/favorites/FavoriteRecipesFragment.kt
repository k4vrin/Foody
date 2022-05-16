package com.kavrin.foody.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kavrin.foody.adapters.FavoritesAdapter
import com.kavrin.foody.databinding.FragmentFavoriteRecipesBinding
import com.kavrin.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val mMainViewModel: MainViewModel by viewModels()

    // Pass activity and MainViewModel to adapter
    private val mAdapter: FavoritesAdapter by lazy { FavoritesAdapter(requireActivity(), mMainViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater, container, false)
        binding.mMainViewModel = mMainViewModel
        binding.lifecycleOwner = this
        binding.mAdapter = mAdapter

        setupRecyclerView()

//        lifecycleScope.launch {
//            mMainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) { favorites ->
//                mAdapter.setData(newData = favorites)
//            }
//        }

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.favoriteRecyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}