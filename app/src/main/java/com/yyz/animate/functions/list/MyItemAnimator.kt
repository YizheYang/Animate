package com.yyz.animate.functions.list

import androidx.recyclerview.widget.RecyclerView

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.4 下午 8:29
 * version 1.0
 * update none
 **/
class MyItemAnimator : RecyclerView.ItemAnimator() {








    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun runPendingAnimations() {
        TODO("Not yet implemented")
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        TODO("Not yet implemented")
    }

    override fun endAnimations() {
        TODO("Not yet implemented")
    }

    override fun isRunning(): Boolean {
        TODO("Not yet implemented")
    }

}