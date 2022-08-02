package com.yyz.animate.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
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
        db = AnimateDatabase.getInstance(this)
        initViews()
        initListener()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initViews()

    abstract fun initListener()

    fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, time).show()

}