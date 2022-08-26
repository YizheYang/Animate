package com.yyz.animate.base

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.yyz.animate.R
import com.yyz.animate.model.AnimateDatabase

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.20 下午 8:49
 * version 1.0
 * update none
 **/
abstract class BaseActivity2 : AppCompatActivity() {

    protected lateinit var db: AnimateDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.white)
        autoAdjustStatusBarText()
        db = AnimateDatabase.getInstance(this)
        initViews()
        initListener()
    }

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