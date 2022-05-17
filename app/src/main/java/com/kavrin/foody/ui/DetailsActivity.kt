package com.kavrin.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.kavrin.foody.R
import com.kavrin.foody.adapters.PagerAdapter
import com.kavrin.foody.data.database.entities.FavoriteRecipesEntity
import com.kavrin.foody.databinding.ActivityDetailsBinding
import com.kavrin.foody.ui.fragments.ingredients.IngredientsFragment
import com.kavrin.foody.ui.fragments.instructions.InstructionsFragment
import com.kavrin.foody.ui.fragments.overview.OverviewFragment
import com.kavrin.foody.util.Constants.RECIPES_RESULT_KEY
import com.kavrin.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()

    private val mMainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*************************************** ViewPager **********************************************/
        // List of fragments
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        // List of titles
        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")
        // Put parcelable object(Result) in the bundle
        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPES_RESULT_KEY, args.result)
        // Initialize PagerAdapter
        val pagerAdapter = PagerAdapter(
            resultBundle = resultBundle,
            fragments = fragments,
            fa = this
        )
        // Set ViewPager adapter
        binding.viewPager.adapter = pagerAdapter
        // Connect ViewPager with tabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }
    /*************************************** Favorite **********************************************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(item: MenuItem) {
        mMainViewModel.readFavoriteRecipes.observe(this) { favoriteEntity ->
            try {
                for (savedRecipe in favoriteEntity) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(item, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivityLog", e.message.toString())
            }
        }
    }

    // Finish activity when back button clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        else if (item.itemId == R.id.save_to_favorites_menu && !recipeSaved ) saveToFavorites(item)
        else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved ) deleteFromFavorites(item)
        return super.onOptionsItemSelected(item)
    }

    private fun deleteFromFavorites(item: MenuItem) {
        val favoritesEntity =
            FavoriteRecipesEntity(
                id = savedRecipeId,
                result = args.result
            )
        mMainViewModel.deleteFavoriteRecipe(favoriteRecipesEntity = favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity =
            FavoriteRecipesEntity(
                id = 0,
                result = args.result
            )
        mMainViewModel.insertFavoriteRecipes(favoriteRecipesEntity = favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved.")
        recipeSaved = true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}