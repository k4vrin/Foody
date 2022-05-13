package com.kavrin.foody.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.kavrin.foody.R
import com.kavrin.foody.databinding.FragmentInstructionsBinding
import com.kavrin.foody.models.Result
import com.kavrin.foody.util.Constants

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(layoutInflater, container, false)

        // Get arguments from bundle
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPES_RESULT_KEY)

        binding.instructionsWebView.webViewClient = object : WebViewClient() {}
        myBundle?.sourceUrl?.let {
            binding.instructionsWebView.loadUrl(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}