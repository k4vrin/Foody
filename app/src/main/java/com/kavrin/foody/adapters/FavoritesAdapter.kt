package com.kavrin.foody.adapters

import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kavrin.foody.R
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.kavrin.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.kavrin.foody.util.FavoritesDiffUtil
import com.kavrin.foody.viewmodels.MainViewModel

class FavoritesAdapter(
    private val requireActivity: FragmentActivity,
    private val mMainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favorites = emptyList<FavoriteRecipesEntity>()

    // MultiSelection Mode
    private var multiSelection = false

    // Selected Recipes In ActionMode
    private var selectedRecipes = mutableListOf<FavoriteRecipesEntity>()

    // Create a list to add viewHolders and then change their style when actionMode is finished
    private var myViewHolders = mutableListOf<MyViewHolder>()

    // Global action mode to use for title and when there is no item selected end the action mode
    private lateinit var mActionMode: ActionMode

    private lateinit var mRootView: View

    /*************************************  ViewHolder  *******************************************/

    class MyViewHolder(val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
    /*************************************  ViewHolder above  *******************************************/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Add each holder to the list to change their style when action mode is destroyed
        myViewHolders.add(holder)

        mRootView = holder.binding.root

        val currentItem = favorites[position]
        // Pass the selected recipes to BindingAdapters
        holder.bind(favoriteEntity = currentItem)

        /**
         * Single Click Listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                // Add the item to SelectedRecipes and change the style
                applySelection(holder, currentItem)
            } else {
                // Navigate to DetailsActivity
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentItem.result
                    )
                holder.binding.root.findNavController().navigate(action)
            }
        }

        /**
         * Long Click Listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                // We need fragment activity to start action mode
                requireActivity.startActionMode(this)
                // Add the item to SelectedRecipes and change the style
                applySelection(holder, currentItem)
                true
            } else {
//                multiSelection = false
                false
            }
        }
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

    /******************************  Contextual Action Mode below  **************************************/

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        // Inflate action mode menu
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        // Pass the action mode to the global action mode
        mActionMode = mode!!
        // Change status bar color
        applyStatusBarColor(R.color.contextualStatusBarColor)

        Log.d("KavActionMode", "${selectedRecipes.size}")
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        // Delete the selected items from FavoriteRecipesTable
        if (item?.itemId == R.id.delete_favorite_recipe) {
            selectedRecipes.forEach {
                mMainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed")
            Log.d("KavActionMode", "actionMode.finish()")
            mActionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        // Change the selected recipes style to normal
        myViewHolders.forEach { holder ->
            changeRecipeStyle(
                holder,
                backgroundColor = R.color.cardBgColor,
                strokeColor = R.color.strokeColor
            )
        }
        // kill multiSelection and clear the list
        multiSelection = false
        selectedRecipes.clear()
        // Change the status bar color to normal
        applyStatusBarColor(R.color.statusBarColor)
        Log.d("KavActionMode", "onDestroyActionMode: Called")
    }
    /******************************  Contextual Action Mode above  *********************************/

    /************************  Custom functions to manipulate the states  **************************/
    /**
     * Apply status bar color
     *
     * Change status bar color when action mode is created and is destroyed
     */
    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    /**
     * Apply selection
     *
     * @param holder
     * @param currentItem
     *
     * Add the selected items to the list and change their styles
     */
    private fun applySelection(holder: MyViewHolder, currentItem: FavoriteRecipesEntity) {
        if (selectedRecipes.contains(currentItem)) {
            // Remove the recipe from the list change its style to normal
            selectedRecipes.remove(currentItem)
            changeRecipeStyle(
                holder,
                backgroundColor = R.color.cardBgColor,
                strokeColor = R.color.strokeColor
            )
            // Either finish the action mode when there is no item selected or change the title accordingly
            applyActionModeTitle()
        } else {
            // Add the recipe and change the style to selected
            selectedRecipes.add(currentItem)
            changeRecipeStyle(
                holder,
                backgroundColor = R.color.cardBgLightColor,
                strokeColor = R.color.primaryColor
            )
            // Either finish the action mode when there is no item selected or change the title accordingly
            applyActionModeTitle()
        }
    }

    /**
     * Change recipe style
     *
     * @param holder
     * @param backgroundColor
     * @param strokeColor
     *
     * Change Selected Recipes Style
     */
    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )

        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    /**
     * Apply action mode title
     *
     * Either finish the action mode when there is no item selected or change the title accordingly
     */
    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            // Finish action mode when there is no item selected
            0 -> {
                Log.d("KavActionMode", "actionMode.finish()")
                mActionMode.finish()
            }
            // Change the the title accordingly
            1 -> mActionMode.title = "1 item selected"
            else -> mActionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            mRootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    /**
     * Clear contextual action mode
     *
     * When we navigate to another fragment action mode will be destroyed
     *
     * Will be called from FavoritesFragment
     */
    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) mActionMode.finish()
    }
}