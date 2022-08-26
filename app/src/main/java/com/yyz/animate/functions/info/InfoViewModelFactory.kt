package com.yyz.animate.functions.info

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.20 下午 10:53
 * version 1.0
 * update none
 **/
class InfoViewModelFactory(val context: AppCompatActivity, val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AppCompatActivity::class.java, Int::class.java)
            .newInstance(context, id)
    }
}