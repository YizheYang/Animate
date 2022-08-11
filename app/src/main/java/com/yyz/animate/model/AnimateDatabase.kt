package com.yyz.animate.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.utils.AnimateStateConverter
import com.yyz.animate.utils.AnimateTypeConverter
import com.yyz.animate.utils.DateConverter
import com.yyz.animate.utils.EpisodeStateConverter

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.30 下午 8:30
 * version 1.0
 * update none
 **/
@Database(entities = [AnimateNameBean::class, AnimateInfoBean::class], version = 2)
@TypeConverters(
    DateConverter::class,
    AnimateStateConverter::class,
    EpisodeStateConverter::class,
    AnimateTypeConverter::class
)
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
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE as AnimateDatabase
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE animate_info ADD COLUMN type integer not null default 1")
            }
        }
    }
}