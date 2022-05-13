package com.kavrin.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kavrin.foody.adapters.FavoritesAdapter
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity

class FavoritesBinding {

    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoriteRecipesEntity: List<FavoriteRecipesEntity>?,
            adapter: FavoritesAdapter?
        ) {
            if (favoriteRecipesEntity.isNullOrEmpty()) {
                when(view) {
                    is TextView -> view.visibility = View.VISIBLE
                    is ImageView -> view.visibility = View.VISIBLE
                    is RecyclerView -> view.visibility = View.INVISIBLE
                }
            } else {
                when(view) {
                    is TextView -> view.visibility = View.INVISIBLE
                    is ImageView -> view.visibility = View.INVISIBLE
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        adapter?.setData(favoriteRecipesEntity)
                    }
                }
            }
        }
    }
}