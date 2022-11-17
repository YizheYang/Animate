package com.yyz.animate.functions.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.yyz.animate.R
import com.yyz.animate.constants.TAG
import com.yyz.animate.model.AnimateDatabase

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

        const val RANDOM = 101010
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "onUpdate: appWidgetIds===============================${appWidgetIds.size}")
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget)

            setTitleClick(context, views, id)

            // 设置listView的adapter
            val serviceIntent = Intent(context, AnimateWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
//            serviceIntent.setData(Uri.fromParts("content", (id + RANDOM).toString(), null))
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))
            views.setRemoteAdapter(R.id.lv_widget, serviceIntent)

            setListClick(context, views, id)

            appWidgetManager.updateAppWidget(id, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val manager = AppWidgetManager.getInstance(context)
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        val views = RemoteViews(context.packageName, R.layout.widget)
//        for (id in manager.getAppWidgetIds(ComponentName(context, AnimateWidgetProvider::class.java))) {
//            // 多次绑定，据说能解决偶尔点击事件失效的问题
//            setTitleClick(context, views, id)
//            setListClick(context, views, id)
//            manager.updateAppWidget(id, views)
//        }
        when (action) {
            REFRESH_ACTION -> {
                Log.d(TAG, "onReceive: ----------------------------------REFRESH_ACTION")
                if (intent.extras?.get("type") == TYPE_CLICK) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "数据刷新中...", Toast.LENGTH_SHORT).show()
                    }
                }
                val componentName = ComponentName(context, AnimateWidgetProvider::class.java)
                manager.updateAppWidget(manager.getAppWidgetIds(componentName), views)
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.lv_widget)
            }
            ITEM_ACTION -> {
                Log.d(TAG, "onReceive: ----------------------------------ITEM_ACTION")
//                val index = intent.extras?.getInt(EXTRA_ITEM, 0) ?: 0
//                Toast.makeText(context, "NO: $index", Toast.LENGTH_SHORT).show()
                val db = AnimateDatabase.getInstance(context)
                val id = intent.extras?.getInt("id", -1)
                val rv = RemoteViews(context.packageName, R.layout.item_widget)
                if (id == -1) {
                    throw IllegalArgumentException("组件点击事件返回的id为空")
                }
                val infoBean = db.getAnimateInfoDao()
                    .getAnimateInfoBeanFromId(id ?: throw IllegalArgumentException("组件点击事件返回的id为空"))
                    ?: throw IllegalArgumentException("查询返回的infobean为空")
                infoBean.episodeList.apply {
                    if (this.isNotEmpty()) {
                        last().already.apply {
                            if (!this) {
                                rv.setInt(
                                    R.id.tv_item_widget,
                                    "setBackgroundColor",
                                    context.resources.getColor(R.color.today_item_bg_already_seen)
                                )
                                rv.setInt(
                                    R.id.tv_item_widget,
                                    "setTextColor",
                                    context.resources.getColor(R.color.widget_tv_text)
                                )
                                Toast.makeText(context, "看完了", Toast.LENGTH_SHORT).show()
                            } else {
                                rv.setInt(
                                    R.id.tv_item_widget,
                                    "setBackgroundColor",
                                    context.resources.getColor(R.color.today_item_bg_not_seen)
                                )
                                rv.setInt(
                                    R.id.tv_item_widget,
                                    "setTextColor",
                                    context.resources.getColor(R.color.widget_tv_default_text)
                                )
                                Toast.makeText(context, "还没看", Toast.LENGTH_SHORT).show()
                            }
                            last().already = !this
                        }
                        db.getAnimateInfoDao().updateAnimateInfoBean(infoBean)
                    }
                }
                manager.updateAppWidget(appWidgetId, views)
                manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget)
                AnimateDatabase.close()
            }
        }
        super.onReceive(context, intent)
    }

    private fun setTitleClick(context: Context, views: RemoteViews, id: Int) {
        // 设置标题的点击事件
        // intent初始化的时候不能直接Intent()，里面不能为空，否则就会出现程序被杀后，点击事件失效
        val titleIntent = Intent(context, AnimateWidgetProvider::class.java)
        titleIntent.action = REFRESH_ACTION
        titleIntent.putExtra("type", TYPE_CLICK)
        titleIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        // 坑：安卓8后不允许隐式广播，需要添加包名变为显式
        titleIntent.`package` = context.packageName
        titleIntent.data = Uri.parse(titleIntent.toUri(Intent.URI_INTENT_SCHEME))
        val rePendingIntent = PendingIntent.getBroadcast(context, 0, titleIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.tv_widget, rePendingIntent)
    }

    private fun setListClick(context: Context, views: RemoteViews, id: Int) {
        // 设置listView的item的点击事件
        // 设置响应 “ListView” 的intent模板
        // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
        //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
        //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
        //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
        val listIntent = Intent(context, AnimateWidgetProvider::class.java)
        listIntent.action = ITEM_ACTION
        listIntent.`package` = context.packageName
        listIntent.data = Uri.parse(listIntent.toUri(Intent.URI_INTENT_SCHEME))
        listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, listIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setPendingIntentTemplate(R.id.lv_widget, pendingIntent)
    }
}