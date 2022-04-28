package com.kavrin.foody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kavrin.foody.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Setup binding
    private lateinit var binding: ActivityMainBinding
    // NavHostFragment
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Setup Fragments
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        /**
         * App bar configuration
         *
         * topLevelDestinations: The set of destinations by id considered at the top level of your information hierarchy.
         *  The Up button will not be displayed when on these destinations.
         *
         * fallbackOnNavigateUpListener: The OnNavigateUpListener that should be invoked if
         *  androidx.navigation.NavController.navigateUp returns false.
         *
         * Openable drawerLayout: The Openable layout that should be toggled from the Navigation button.
         *  The the Navigation button will show a drawer symbol when it is not being shown as an Up button.
         *
         * https://developer.android.com/reference/androidx/navigation/ui/AppBarConfigurationKt
         *
         */
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.favoriteRecipesFragment,
                R.id.foodJokeFragment
            )
        )
        // Connect navController to bottomNavigation
        binding.bottomNavigationView.setupWithNavController(navController = navController)

        /**
         * Show each Fragment title in action bar and
         * configuration - Additional configuration options for customizing the behavior of the ActionBar
         * The AppBarConfiguration you provide controls how the Navigation button is displayed.
         */
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * On support navigate up
     *
     * Navigate Up from child fragment to parent fragment
     */
    override fun onSupportNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() || return super.onSupportNavigateUp()
    }
}