package com.kavrin.foody.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.R
import com.kavrin.foody.databinding.FragmentFoodJokeBinding
import com.kavrin.foody.util.NetworkResult
import com.kavrin.foody.util.PrConstants.API_KEY
import com.kavrin.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private val mMainViewModel: MainViewModel by viewModels()

    // Save food joke for intent share
    private var foodJoke = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mMainViewModel = mMainViewModel

        mMainViewModel.getFoodJoke(API_KEY)

        setHasOptionsMenu(true)

        mMainViewModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        binding.foodJokeTextView.text = it.text
                        foodJoke = it.text
                    }
                }
                is NetworkResult.Error -> {

                    /** Load Data From Cache */
                    // Data is cached in MainViewModel
                    loadJokeFromCache()
                    /** Load Data From Cache */

                    // The message is handled by NetworkResult in MainViewModel
                    showSnackBar(response.message.toString())
                }
                else -> { // is Loading
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }

    /**
     * Load joke from cache
     *
     * Set the food joke when api response is error
     *
     */
    private fun loadJokeFromCache() {
        lifecycleScope.launch {
            mMainViewModel.readFoodJoke.observe(viewLifecycleOwner) { jokeDatabase ->
                if (!jokeDatabase.isNullOrEmpty()) {
                    binding.foodJokeTextView.text = jokeDatabase[0].joke.text
                    foodJoke = jokeDatabase[0].joke.text
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Share food joke
        if (item.itemId == R.id.share_food_joke) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    private fun showSnackBar(message: String) {
        Snackbar
            .make(
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