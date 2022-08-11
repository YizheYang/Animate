package com.yyz.animate.utils

import androidx.room.TypeConverter
import com.yyz.animate.constants.AnimateType

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.12 上午 12:44
 * version 1.0
 * update none
 **/
class AnimateTypeConverter {

    @TypeConverter
    fun converter(type: AnimateType): Int {
        return type.id
    }

    @TypeConverter
    fun revert(state: Int): AnimateType {
        var name = ""
        when (state) {
            AnimateType.TV.id -> name = AnimateType.TV.name
            AnimateType.MOVIE.id -> name = AnimateType.MOVIE.name
            AnimateType.OVA.id -> name = AnimateType.OVA.name
        }
        return AnimateType.valueOf(name)
    }
}