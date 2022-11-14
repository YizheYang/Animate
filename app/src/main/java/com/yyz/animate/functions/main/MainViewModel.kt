package com.yyz.animate.functions.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.3 下午 11:11
 * version 1.0
 * update none
 **/
class MainViewModel : ViewModel() {
    val refresh = MutableLiveData<Boolean>(false)
}