package com.yyz.animate.utils

import java.util.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.15 下午 7:46
 * version 1.0
 * update none
 **/
object DayUtil {
    fun getToday(): Int = SunIsFirst()

    private fun SunIsFirst(): Int {
        return (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
    }

    /**
     * 指定时间是周几
     * @param time Long
     * @return Int
     */
    fun getDay(time: Long): Int {
        val c = Calendar.getInstance()
        c.time = Date(time)
        return (c.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
    }

    /**
     * 指定的时间是周几
     * @param date Date
     * @return Int
     */
    fun getDay(date: Date): Int {
        val c = Calendar.getInstance()
        c.time = date
        return (c.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
    }

    /**
     * 指定的时间距今有多少周，不足一周舍去多余天数
     * @param time Long
     * @return Int
     */
    fun getWeeks(time: Long): Int {
        return ((Date(System.currentTimeMillis()).time - time) / (1000 * 60 * 60 * 24 * 7)).toInt()
    }

    /**
     * 指定的时间距今有多少周，不足一周舍去多余天数
     * @param date Date
     * @return Int
     */
    fun getWeeks(date: Date): Int {
        return ((Date(System.currentTimeMillis()).time - date.time) / (1000 * 60 * 60 * 24 * 7)).toInt()
    }

}