package com.kavrin.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.kavrin.foody.R

class RecipesRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(view: ImageView, imageUrl: String) {
            // Use Coil to load image
            view.load(uri = imageUrl) {
                crossfade(durationMillis = 600)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(view: TextView, likes: Int) {
            view.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(view: TextView, readyInMinutes: Int) {
            view.text = readyInMinutes.toString()
        }

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

    }
}