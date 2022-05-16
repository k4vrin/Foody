package com.kavrin.foody.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.R
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
        // Initialize option menu
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Setup recycler view
     *
     */
    private fun setupRecyclerView() {
        val recyclerView = binding.favoriteRecyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Delete all
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mMainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("All recipes has been removed")
            }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}