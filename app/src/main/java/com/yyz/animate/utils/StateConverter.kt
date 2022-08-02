package com.yyz.animate.utils

import androidx.room.TypeConverter
import com.yyz.animate.constants.State

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 7:53
 * version 1.0
 * update none
 **/
class StateConverter {

    @TypeConverter
    fun converter(state: State): Int {
        return state.state
    }

    @TypeConverter
    fun revert(state: Int): State {
        var name = ""
        when (state) {
            1 -> name = State.WILL.name
            2 -> name = State.WATCHING.name
            3 -> name = State.ALREADY.name
        }
        return State.valueOf(name)
    }

}