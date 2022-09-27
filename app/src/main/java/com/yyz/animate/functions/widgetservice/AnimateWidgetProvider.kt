package com.yyz.animate.functions.widgetservice

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.yyz.animate.R
import com.yyz.animate.constants.TAG

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.9 下午 8:58
 * version 1.0
 * update none
 **/
class AnimateWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ITEM_ACTION = "com.yyz.animate.ITEM_ACTION"
        const val REFRESH_ACTION = "com.yyz.animate.REFRESH_ACTION"
        const val EXTRA_ITEM = "com.yyz.animate.EXTRA_ITEM"

        const val TYPE_CLICK = 0
        const val TYPE_AUTO = 1
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "onUpdate: appWidgetIds===============================${appWidgetIds.size}")
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget)

            setTitleClick(context, views)

            // 设置listView的adapter
            val serviceIntent = Intent(context, AnimateWidgetService::class.java)
            views.setRemoteAdapter(R.id.lv_widget, serviceIntent)

            setListClick(context, views, id)

            appWidgetManager.updateAppWidget(id, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val manager = AppWidgetManager.getInstance(context)
        for (id in manager.getAppWidgetIds(ComponentName(context, AnimateWidgetProvider::class.java))) {
            val views = RemoteViews(context.packageName, R.layout.widget)
            // 多次绑定，据说能解决偶尔点击事件失效的问题
            setTitleClick(context, views)
            setListClick(context, views, id)
            manager.updateAppWidget(id, views)
        }
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        when (action) {
            REFRESH_ACTION -> {
                Log.d(TAG, "onReceive: ----------------------------------REFRESH_ACTION")
                if (intent.extras?.get("type") == TYPE_CLICK) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "数据刷新中...", Toast.LENGTH_SHORT).show()
                    }
                }
                val componentName = ComponentName(context, AnimateWidgetProvider::class.java)
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.lv_widget)
            }
            ITEM_ACTION -> {
                Log.d(TAG, "onReceive: ----------------------------------ITEM_ACTION")
                val index = intent.extras?.getInt(EXTRA_ITEM, 0) ?: 0
                Toast.makeText(context, "NO: $index", Toast.LENGTH_SHORT).show()
            }
        }
        super.onReceive(context, intent)
    }

    private fun setTitleClick(context: Context, views: RemoteViews) {
        // 设置标题的点击事件
        val titleIntent = Intent()
        titleIntent.action = REFRESH_ACTION
        titleIntent.putExtra("type", TYPE_CLICK)
        // 坑：安卓8后不允许隐式广播，需要添加包名变为显式
        titleIntent.`package` = context.packageName
        val rePendingIntent = PendingIntent.getBroadcast(context, 0, titleIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.tv_widget, rePendingIntent)
    }

    private fun setListClick(context: Context, views: RemoteViews, id: Int) {
        // 设置listView的item的点击事件
        // 设置响应 “ListView” 的intent模板
        // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
        //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
        //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
        //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
        val listIntent = Intent()
        listIntent.action = ITEM_ACTION
        listIntent.`package` = context.packageName
        listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.lv_widget, pendingIntent)
    }
}