package com.yyz.animate.utils

import androidx.room.TypeConverter
import com.yyz.animate.constants.AnimateState

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 7:53
 * version 1.0
 * update none
 **/
class AnimateStateConverter {

    @TypeConverter
    fun converter(state: AnimateState): Int {
        return state.id
    }

    @TypeConverter
    fun revert(state: Int): AnimateState {
        var name = ""
        when (state) {
            1 -> name = AnimateState.WILL.name
            2 -> name = AnimateState.WATCHING.name
            3 -> name = AnimateState.ALREADY.name
        }
        return AnimateState.valueOf(name)
    }

}