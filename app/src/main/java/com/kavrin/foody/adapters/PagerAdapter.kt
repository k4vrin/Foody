package com.kavrin.foody.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Pager adapter
 *
 * @property resultBundle
 * @property fragments
 * @constructor
 *
 * @param fa
 *
 * https://developer.android.com/jetpack/androidx/releases/viewpager2
 * https://developer.android.com/training/animation/vp2-migration
 */
class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    fa: FragmentActivity
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }
}