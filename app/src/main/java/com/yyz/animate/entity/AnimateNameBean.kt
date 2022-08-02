package com.yyz.animate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 8:24
 * version 1.0
 * update none
 **/
@Entity(tableName = "animate_name")
data class AnimateNameBean(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    @ColumnInfo(name = "init_time")
    val initTime: Date,
    @ColumnInfo(name = "latest_time")
    val latestTime: Date,
    val priority: Int
)
