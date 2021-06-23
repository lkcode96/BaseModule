package com.lk.baselibrary.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * 页面描述：fragment PagerAdapter
 *  lk
 */

class AbstractPagerAdapter(fm: FragmentActivity, private val list: MutableList<Fragment>) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]

}