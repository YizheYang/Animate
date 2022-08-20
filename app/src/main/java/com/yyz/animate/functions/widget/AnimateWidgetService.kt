package com.yyz.animate.functions.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.yyz.animate.R
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.model.AnimateDatabase
import java.util.*


/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.9 下午 9:56
 * version 1.0
 * update none
 **/
class AnimateWidgetService() : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return AnimateFactory(this.applicationContext, intent)
    }

    inner class AnimateFactory(private val context: Context, intent: Intent) : RemoteViewsFactory {
        private val list = mutableListOf<AnimateInfoBean>()
        private var db: AnimateDatabase = AnimateDatabase.getInstance(context)

        override fun onCreate() {
            val today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
            list.addAll(db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(today))
        }

        override fun onDataSetChanged() {
            val today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
            val temp = db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(today)
            list.clear()
            list.addAll(temp.filter { !it.episode.run { this[lastIndex].already } })
        }

        override fun onDestroy() {
            list.clear()
            db.close()
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.item_widget)
            views.setTextViewText(
                R.id.tv_item_widget,
                db.getAnimateNameDao().getAnimateNameBeanFormId(list[position].nameId)?.name
            )
            val extras = Bundle()
            extras.putInt(AnimateWidgetProvider.EXTRA_ITEM, position)
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)
            views.setOnClickFillInIntent(R.id.tv_item_widget, fillInIntent)
            return views
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return list[position].id!!.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }
}