package com.yyz.animate.base

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.yyz.animate.R
import com.yyz.animate.model.AnimateDatabase

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 8:22
 * version 1.0
 * update none
 **/
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var db: AnimateDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        window.statusBarColor = resources.getColor(R.color.white)
        autoAdjustStatusBarText()
        db = AnimateDatabase.getInstance(this)
        initViews()
        initListener()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initViews()

    abstract fun initListener()

    fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, time).show()

    private fun autoAdjustStatusBarText() {
        //手机为浅色模主题时，状态栏字体颜色设为黑色，由于状态栏字体颜色默认为白色，所以深色主题不需要适配
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.decorView.windowInsetsController!!.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}