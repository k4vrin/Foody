package com.kavrin.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.kavrin.foody.R
import com.kavrin.foody.adapters.PagerAdapter
import com.kavrin.foody.databinding.ActivityDetailsBinding
import com.kavrin.foody.ui.fragments.ingredients.IngredientsFragment
import com.kavrin.foody.ui.fragments.instructions.InstructionsFragment
import com.kavrin.foody.ui.fragments.overview.OverviewFragment
import com.kavrin.foody.util.Constants.RECIPES_RESULT_KEY

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()

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
        val adapter = PagerAdapter(
            resultBundle = resultBundle,
            fragments = fragments,
            fa = this
        )
        // Set ViewPager adapter
        binding.viewPager.adapter = adapter
        // Connect ViewPager with tabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }
    // Finish activity when back button clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}