package com.yyz.animate.functions.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.yyz.animate.R

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.9 下午 8:58
 * version 1.0
 * update none
 **/
class AnimateWidgetProvider : AppWidgetProvider() {

    companion object {
        val TOAST_ACTION = "TOAST_ACTION"
        val EXTRA_ITEM = "EXTRA_ITEM"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val views = RemoteViews(context.packageName, R.layout.widget)


        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == TOAST_ACTION) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val index = intent.extras?.getInt(EXTRA_ITEM, -1)

        }
    }
}