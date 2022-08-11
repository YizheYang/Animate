package com.yyz.animate.constants

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 7:46
 * version 1.0
 * update none
 **/
enum class AnimateState(val id: Int, val state: String) {
    // 1还没看，2正在看，3看完了
    WILL(1, "还没看"),
    WATCHING(2, "正在看"),
    ALREADY(3, "已看完")
}