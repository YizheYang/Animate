package com.yyz.animate.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.yyz.animate.constants.Constants.formatType
import java.text.SimpleDateFormat
import java.util.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 10:30
 * version 1.0
 * update none
 **/
class DateConverter {

    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun converter(date: Date): String {
        return SimpleDateFormat(formatType).format(date)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun revert(strTime: String): Date {
        var res = Date()
        try {
            res = SimpleDateFormat(formatType).parse(strTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    companion object {
        fun converter(date: Date): String {
            return SimpleDateFormat(formatType).format(date)
        }
    }

}