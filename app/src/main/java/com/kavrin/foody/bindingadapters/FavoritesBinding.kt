package com.kavrin.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kavrin.foody.adapters.FavoritesAdapter
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity

class FavoritesBinding {

    companion object {
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(
            view: View,
            favoriteRecipesEntity: List<FavoriteRecipesEntity>?,
            mAdapter: FavoritesAdapter?
        ) {
            val dataCheck = favoriteRecipesEntity.isNullOrEmpty()
            when (view) {
                is RecyclerView -> {
                    view.isInvisible = dataCheck
                    if (!dataCheck) favoriteRecipesEntity?.let { mAdapter?.setData(it) }
                }
                else -> view.isVisible = dataCheck
            }
        }
    }
}