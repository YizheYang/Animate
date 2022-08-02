package com.yyz.animate

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.yyz.animate.base.BaseActivity
import com.yyz.animate.functions.list.ListFragment
import com.yyz.animate.functions.today.TodayFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_main

    override fun initViews() {
        vp_main.adapter = MainFragmentAdapter(this)
        vp_main.isUserInputEnabled = false
        vp_main.offscreenPageLimit = bnv_main.menu.size() - 1
    }

    override fun initListener() {
        vp_main.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bnv_main.selectedItemId = bnv_main.menu.getItem(position).itemId
            }
        })

        bnv_main.setOnItemSelectedListener { item ->
            vp_main.currentItem = item.order
            true
        }
    }

    inner class MainFragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = bnv_main.menu.size()

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TodayFragment.newInstance()
                1 -> ListFragment.newInstance()
                else -> Fragment()
            }
        }

    }
}