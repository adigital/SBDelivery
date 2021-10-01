package ru.skillbranch.sbdelivery.ui.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.sbdelivery.data.local.entities.Category

class CategoryViewPagerAdapter(fragment: Fragment, private val tabs: List<Category>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        val fragment = CategoryFragment()
        fragment.arguments = Bundle().apply {
            putString("categoryId", tabs[position].categoryId)
        }
        return fragment
    }
}
