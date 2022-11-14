package com.yyz.animate.functions.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.yyz.animate.R
import com.yyz.animate.constants.TAG
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.entity.InfoWithName
import com.yyz.animate.model.AnimateDatabase
import com.yyz.animate.utils.DayUtil

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.9.27 下午 9:50
 * version 1.0
 * update none
 **/
class AnimateWidgetFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private var list = mutableListOf<InfoWithName>()

    //    private val mAppWidgetId = (intent.data?.schemeSpecificPart)?.toInt()?.minus(AnimateWidgetProvider.RANDOM)
    private val mAppWidgetId =
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    private var db: AnimateDatabase? = null

    override fun onCreate() {
        if (db == null) {
            db = AnimateDatabase.getInstance(context)
        }
        updateData()
        list.addAll(
            (db ?: AnimateDatabase.getInstance(context)).getAnimateInfoDao()
                .getInfoWithNameListFromUpdateDay(DayUtil.getToday())
        )
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d(TAG, "getViewAt: ----------------------------")
        val views = RemoteViews(context.packageName, R.layout.item_widget)
        views.setTextViewText(R.id.tv_item_widget, list.run {
            if (size == 0) {
                "无"
            } else {
                if (this[position].infoBean.episodeList.last().already) {
                    views.setInt(
                        R.id.tv_item_widget,
                        "setBackgroundColor",
                        context.resources.getColor(R.color.today_item_bg_already_seen)
                    )
                }
                val sb = StringBuilder()
                sb.append(position + 1).append(".")
                sb.append(this[position].nameBean.name)
                sb.append(" ")
                sb.append(this[position].infoBean.season)
                sb.append("—")
                sb.append(this[position].infoBean.episodeList.size)
                sb.toString()
            }
        })

        val extras = Bundle()
        extras.putInt(AnimateWidgetProvider.EXTRA_ITEM, position)
        extras.putInt("id", list[position].infoBean.id ?: -1)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        views.setOnClickFillInIntent(R.id.tv_item_widget, fillInIntent)
        return views
    }

    override fun onDataSetChanged() {
        list = mutableListOf()
        list.clear()
        if (db == null) {
            db = AnimateDatabase.getInstance(context)
        }
        list.addAll(
            (db ?: AnimateDatabase.getInstance(context)).getAnimateInfoDao()
                .getInfoWithNameListFromUpdateDay(DayUtil.getToday())
        )

//        list.addAll(
//            listOf(
//                InfoWithName(
//                    AnimateInfoBean(
//                        0, 0, 0, 0, mutableListOf(), AnimateState.WATCHING,
//                        Date(0), Date(0), 0, AnimateType.TV
//                    ),
//                    AnimateNameBean(0, "null", Date(0), Date(0), 0)
//                )
//            )
//        )
        Log.d(TAG, "onDataSetChanged: -----------------------${list.size}")
    }

    private fun updateData() {
        if (db == null) {
            db = AnimateDatabase.getInstance(context)
        }
        val tempList = (db ?: AnimateDatabase.getInstance(context)).getAnimateInfoDao()
            .getAnimateInfoBeanListFromUpdateDay(DayUtil.getToday())
        for (temp in tempList) {
            val weeks = DayUtil.getWeeks(temp.airTime)
            while (temp.episodeList.size <= weeks) {
                temp.episodeList.add(EpisodeState(temp.episodeList.size + 1, false))
                (db ?: AnimateDatabase.getInstance(context)).getAnimateInfoDao().updateAnimateInfoBean(temp)
            }
        }

    }

    override fun onDestroy() {
        list.clear()
        AnimateDatabase.close()
        db = null
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return list[position].infoBean.id!!.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}