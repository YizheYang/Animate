package com.yyz.animate.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.15 下午 10:11
 * version 1.0
 * update none
 **/
data class InfoWithName(
    @Embedded
    val infoBean: AnimateInfoBean,
    @Relation(
        parentColumn = "nameId",
        entityColumn = "name_id",
        entity = AnimateNameBean::class
    )
    val nameBean: AnimateNameBean
)
