package com.yyz.animate.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.utils.DateConverter
import com.yyz.animate.utils.EpisodeStateConverter
import com.yyz.animate.utils.StateConverter

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 8:30
 * version 1.0
 * update none
 **/
@Database(entities = [AnimateNameBean::class, AnimateInfoBean::class], version = 1)
@TypeConverters(DateConverter::class, StateConverter::class, EpisodeStateConverter::class)
abstract class AnimateDatabase : RoomDatabase() {
    abstract fun getAnimateInfoDao(): AnimateInfoDao

    abstract fun getAnimateNameDao(): AnimateNameDao

    companion object {
        private var INSTANCE: AnimateDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AnimateDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context.applicationContext, AnimateDatabase::class.java, "animate")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as AnimateDatabase
        }
    }

}