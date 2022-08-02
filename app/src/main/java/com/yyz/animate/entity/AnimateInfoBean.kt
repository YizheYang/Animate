package com.yyz.animate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yyz.animate.constants.State
import java.util.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 8:24
 * version 1.0
 * update none
 **/
@Entity(tableName = "animate_info")
data class AnimateInfoBean(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "name_id")
    val nameId: Int,
    val season: Int,
    val episodes: Int,
    val episode: MutableList<EpisodeState>,
    val state: State,
    @ColumnInfo(name = "init_time")
    val initTime: Date,
    val airTime: Date,// 首播时间
    @ColumnInfo(name = "update_day")
    val updateDay: Int// 更新时间
)
