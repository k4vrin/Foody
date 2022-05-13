package com.kavrin.foody.util

import androidx.recyclerview.widget.DiffUtil
import com.kavrin.foody.models.ExtendedIngredient

class IngredientsDiffUtil(
    private val oldList: List<ExtendedIngredient>,
    private val newList: List<ExtendedIngredient>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}