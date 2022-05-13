package com.kavrin.foody.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kavrin.foody.adapters.IngredientAdapter
import com.kavrin.foody.databinding.FragmentIngredientsBinding
import com.kavrin.foody.models.Result
import com.kavrin.foody.util.Constants.RECIPES_RESULT_KEY

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: IngredientAdapter by lazy { IngredientAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIngredientsBinding.inflate(layoutInflater, container, false)

        // Get arguments from bundle
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)

        setupRecyclerView(myBundle)


        return binding.root
    }

    private fun setupRecyclerView(bundle: Result?) {
        val recyclerView = binding.ingredientsRecyclerView

        recyclerView.adapter = mAdapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        bundle?.extendedIngredients?.let { mAdapter.setData(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}