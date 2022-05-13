package com.kavrin.foody.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.kavrin.foody.R
import com.kavrin.foody.databinding.FragmentOverviewBinding
import com.kavrin.foody.models.Result
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)

        // Get arguments from bundle
        val args = arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")
        // Pass data to views
        binding.mainImageView.load(myBundle?.image)
        binding.titleOverView.text = myBundle?.title
        // Parse html to string
        myBundle?.summary.let {
            val des = Jsoup.parse(it).text()
            binding.summaryTextView.text = des
        }
        binding.likeTextView.text = myBundle?.aggregateLikes.toString()
        binding.timeTextView.text = myBundle?.readyInMinutes.toString()

        if (myBundle?.vegetarian == true) {
            binding.vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (myBundle?.vegan == true) {
            binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (myBundle?.glutenFree == true) {
            binding.glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (myBundle?.dairyFree == true) {
            binding.diaryFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.diaryFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (myBundle?.veryHealthy == true) {
            binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (myBundle?.cheap == true) {
            binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }


        return binding.root
    }
}