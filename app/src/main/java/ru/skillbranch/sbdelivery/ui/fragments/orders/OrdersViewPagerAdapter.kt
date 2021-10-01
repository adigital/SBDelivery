package ru.skillbranch.sbdelivery.ui.fragments.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrdersViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = OrdersListFragment()
        fragment.arguments = Bundle().apply {
            putInt("tabPosition", position)
        }
        return fragment
    }
}
