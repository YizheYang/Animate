package com.yyz.animate.functions.widgetservice

import android.content.Intent
import android.widget.RemoteViewsService


/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.9 下午 9:56
 * version 1.0
 * update none
 **/
class AnimateWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return AnimateWidgetFactory(this, intent)
    }

}