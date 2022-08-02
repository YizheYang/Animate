package com.yyz.animate.constants

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 7:46
 * version 1.0
 * update none
 **/
enum class State(val state: Int) {
    // 1还没看，2正在看，3看完了
    WILL(1),
    WATCHING(2),
    ALREADY(3)
}