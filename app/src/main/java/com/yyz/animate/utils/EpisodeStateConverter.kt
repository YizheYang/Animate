package com.yyz.animate.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yyz.animate.entity.EpisodeState

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.1 上午 11:28
 * version 1.0
 * update none
 **/
class EpisodeStateConverter {
    @TypeConverter
    fun converter(states: List<EpisodeState>): String {
        return Gson().toJson(states)
    }

    @TypeConverter
    fun revert(string: String): List<EpisodeState> {
        return Gson().fromJson(string, object : TypeToken<List<EpisodeState?>?>() {}.type)
    }

}