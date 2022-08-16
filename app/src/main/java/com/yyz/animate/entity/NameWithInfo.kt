package com.yyz.animate.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.14 下午 7:49
 * version 1.0
 * update none
 **/
data class NameWithInfo(
    @Embedded
    val nameBean: AnimateNameBean,
    @Relation(
        parentColumn = "name_id",
        entityColumn = "nameId"
    )
    val infoBean: List<AnimateInfoBean>
)
