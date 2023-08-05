package com.example.macrostracker.ui.diary.foodList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    var mealId: Long = 0
    var date: String = ""

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyFoodsFragment.newInstance(mealId, date)
            1 -> MyRecipesFragment.newInstance(mealId, date)
            else -> throw UnsupportedOperationException("Unknown position")
        }
    }


}