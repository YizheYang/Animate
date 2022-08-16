package com.yyz.animate.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yyz.animate.constants.Constants
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.InfoWithName

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

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM animate_info")
    fun getInfoWithNameList(): LiveData<List<InfoWithName>>

    @Query("SELECT * FROM animate_info WHERE id == :id")
    fun getAnimateInfoBeanFromId(id: Int): AnimateInfoBean?

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM animate_info WHERE id = :id")
    fun getInfoWithNameFromId(id: Int): LiveData<InfoWithName>

    @Query("SELECT * FROM animate_info WHERE nameId == :nameId")
    fun getAnimateInfoBeanListFromNameId(nameId: Int): List<AnimateInfoBean>

    @Query(
        "SELECT * FROM animate_info " +
                "WHERE state == ${Constants.WATCHING} " +
                "and type == ${Constants.TV} " +
                "and update_day == :updateDay"
    )
    fun getAnimateInfoBeanListFromUpdateDay(updateDay: Int): List<AnimateInfoBean>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(
        "SELECT * FROM animate_name JOIN animate_info ON (animate_info.nameId = animate_name.name_id)" +
                "WHERE animate_info.state = ${Constants.WATCHING} " +
                "and animate_info.type = ${Constants.TV} " +
                "and animate_info.update_day = :updateDay"
    )
    fun getInfoWithNameListFromUpdateDay(updateDay: Int): List<InfoWithName>

    @Update
    fun updateAnimateInfoBean(animateInfoBean: AnimateInfoBean)

    @Delete
    fun deleteAnimateInfoBean(animateInfoBean: AnimateInfoBean)

    @Query("DELETE FROM animate_info WHERE nameId == :id")
    fun deleteAnimateInfoBeanFromNameId(id: Int)
}