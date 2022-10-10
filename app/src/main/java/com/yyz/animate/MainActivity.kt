package com.yyz.animate

import android.os.Handler
import android.os.Looper
import android.os.Process
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.yyz.animate.base.BaseActivity
import com.yyz.animate.functions.list.ListFragment
import com.yyz.animate.functions.today.TodayFragment
import com.yyz.animate.model.AnimateDatabase
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

    private var isExit = false

    /**
     * 实现第二次返回才退出软件，防止误触
     * @param keyCode 按下的按键
     * @param event 点击事件
     * @return 是否已经处理事件
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            if (!isExit) {
                isExit = true
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({ isExit = false }, 1000)
            } else {
                finish()
                Handler(Looper.getMainLooper()).postDelayed({ Process.killProcess(Process.myPid()) }, 100)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnimateDatabase.close()
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