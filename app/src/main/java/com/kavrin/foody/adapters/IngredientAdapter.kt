package com.kavrin.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kavrin.foody.R
import com.kavrin.foody.databinding.IngredientsRowLayoutBinding
import com.kavrin.foody.models.ExtendedIngredient
import com.kavrin.foody.util.IngredientsDiffUtil
import com.kavrin.foody.util.PrConstants.BASE_IMAGE_URL

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            /**
             * From
             *
             * Inflate rowLayout
             */
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ingredientsImageView.load(BASE_IMAGE_URL + ingredients[position].image) {
            crossfade(durationMillis = 600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.ingredientName.text = ingredients[position].name.uppercase()
        holder.binding.ingredientAmount.text = ingredients[position].amount.toString()
        holder.binding.ingredientUnit.text = ingredients[position].unit
        holder.binding.ingredientConsistency.text = ingredients[position].consistency ?: ""
        holder.binding.ingredientOriginal.text = ingredients[position].original
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(newData: List<ExtendedIngredient>) {

        val recipesDiffUtil = IngredientsDiffUtil(oldList = ingredients, newList = newData)
        val ingredientsDiffUtil = DiffUtil.calculateDiff(recipesDiffUtil)

        ingredients = newData

        ingredientsDiffUtil.dispatchUpdatesTo(this)
    }
}