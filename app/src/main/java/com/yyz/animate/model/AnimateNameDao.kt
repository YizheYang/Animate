package com.yyz.animate.model

import androidx.room.*
import com.yyz.animate.entity.AnimateNameBean

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 10:43
 * version 1.0
 * update none
 **/
@Dao
interface AnimateNameDao {
    @Insert
    fun insertAnimateNameBean(animateNameBean: AnimateNameBean)

    @Query("SELECT * FROM animate_name")
    fun getAnimateNameBeanList(): List<AnimateNameBean>

    @Query("SELECT * FROM animate_name WHERE id == :id")
    fun getAnimateNameBeanFormId(id: Int): AnimateNameBean?

    @Query("SELECT * FROM animate_name WHERE name == :name")
    fun getAnimateNameBeanFromName(name: String): AnimateNameBean?

    @Update
    fun updateAnimateNameBean(animateNameBean: AnimateNameBean)

    @Delete
    fun deleteAnimateNameBean(animateNameBean: AnimateNameBean)
}