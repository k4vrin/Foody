package com.kavrin.foody.util

import androidx.recyclerview.widget.DiffUtil
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.models.ExtendedIngredient

class FavoritesDiffUtil(
    private val oldList: List<FavoriteRecipesEntity>,
    private val newList: List<FavoriteRecipesEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}