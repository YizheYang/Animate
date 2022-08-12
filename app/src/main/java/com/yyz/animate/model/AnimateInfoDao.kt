package com.yyz.animate.model

import androidx.room.*
import com.yyz.animate.constants.Constants
import com.yyz.animate.entity.AnimateInfoBean

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 8:29
 * version 1.0
 * update none
 **/
@Dao
interface AnimateInfoDao {
    @Insert
    fun insertAnimateInfoBean(animateInfoBean: AnimateInfoBean)

    @Query("SELECT * FROM animate_info")
    fun getAnimateInfoBeanList(): List<AnimateInfoBean>

    @Query("SELECT * FROM animate_info WHERE id == :id")
    fun getAnimateInfoBeanFromId(id: Int): AnimateInfoBean?

    @Query("SELECT * FROM animate_info WHERE name_id == :nameId")
    fun getAnimateInfoBeanListFromNameId(nameId: Int): List<AnimateInfoBean>

    @Query("SELECT * FROM animate_info WHERE state == ${Constants.WATCHING} and type == ${Constants.TV} and update_day == :updateDay")
    fun getAnimateInfoBeanListFromUpdateDay(updateDay: Int): List<AnimateInfoBean>

    @Update
    fun updateAnimateInfoBean(animateInfoBean: AnimateInfoBean)

    @Delete
    fun deleteAnimateInfoBean(animateInfoBean: AnimateInfoBean)

    @Query("DELETE FROM animate_info WHERE name_id == :id")
    fun deleteAnimateInfoBeanFromNameId(id: Int)
}