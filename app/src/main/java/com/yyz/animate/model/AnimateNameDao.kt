package com.yyz.animate.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.entity.NameWithInfo

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

    @Query("SELECT * FROM animate_name WHERE name_id == :id")
    fun getAnimateNameBeanFormId(id: Int): AnimateNameBean?

    @Query("SELECT * FROM animate_name WHERE name == :name")
    fun getAnimateNameBeanFromName(name: String): AnimateNameBean?

    @Update
    fun updateAnimateNameBean(animateNameBean: AnimateNameBean)

    @Delete
    fun deleteAnimateNameBean(animateNameBean: AnimateNameBean)

    @Transaction
    @Query("SELECT * FROM animate_name")
    fun getNameWithInfoList(): LiveData<List<NameWithInfo>>
}