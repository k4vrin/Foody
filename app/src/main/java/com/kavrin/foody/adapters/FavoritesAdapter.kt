package com.kavrin.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.kavrin.foody.util.FavoritesDiffUtil

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>() {

    private var favorites = emptyList<FavoriteRecipesEntity>()

    class MyViewHolder(val binding: FavoriteRecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        /**
         * Bind
         *
         * Connect binding result to RecipesAdapter result
         */
        fun bind(favoriteEntity: FavoriteRecipesEntity) {
            binding.favoritesEntity = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            /**
             * From
             *
             * Inflate rowLayout
             */
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favorites[position]
        holder.bind(favoriteEntity = currentItem)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun setData(newData: List<FavoriteRecipesEntity>) {

        val favoritesDiffUtil = FavoritesDiffUtil(oldList = favorites, newList = newData)
        val favoritesDiffUtilResult = DiffUtil.calculateDiff(favoritesDiffUtil)

        favorites = newData

        favoritesDiffUtilResult.dispatchUpdatesTo(this)
    }
}