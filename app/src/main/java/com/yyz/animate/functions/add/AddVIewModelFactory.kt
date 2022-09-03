package com.yyz.animate.functions.add

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.26 下午 6:13
 * version 1.0
 * update none
 **/
class AddVIewModelFactory(private val context: AppCompatActivity, private val id: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AppCompatActivity::class.java, Int::class.java).newInstance(context, id)
    }
}