package com.sn.gameelectricity.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 */
class SimpleFragmentPageAdapter(fa: Fragment, private var mFragments: MutableList<Fragment>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}