package com.kavrin.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.kavrin.foody.R
import com.kavrin.foody.models.Result
import com.kavrin.foody.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

class RecipesRowBinding {

    companion object {

        /**
         * On recipe click listener
         *
         * @param constraintLayout
         * @param result
         *
         * Pass the data from RecipesFragment to DetailsActivity
         */
        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(constraintLayout: ConstraintLayout, result: Result) {
            constraintLayout.setOnClickListener { 
                try {
                    val action =
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    constraintLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }

        /**
         * Load image from url
         *
         * @param view
         * @param imageUrl
         *
         * Load the image with coil
         */
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(view: ImageView, imageUrl: String) {
            // Use Coil to load image
            view.load(uri = imageUrl) {
                // Enable a crossfade animation with duration CrossfadeDrawable.DEFAULT_DURATION milliseconds when a request completes successfully.
                crossfade(durationMillis = 600)
                //Error!
                error(R.drawable.ic_error_placeholder)
            }
        }


        /**
         * Apply vegan color
         *
         * @param view
         * @param isVegan
         *
         * Apply vegan color for text and image view
         */
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, isVegan: Boolean) {
            if (isVegan) {
                when(view) {
                    is TextView -> view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    is ImageView -> view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                }
            }
        }

        /**
         * Parse html
         *
         * @param textView
         * @param description
         */
        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            description?.let {
                val des = Jsoup.parse(it).text()
                textView.text = des
            }
        }

    }
}