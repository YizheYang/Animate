package com.yyz.animate.functions.today

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import com.yyz.animate.R
import com.yyz.animate.base.BaseFragment
import com.yyz.animate.constants.AnimateState
import com.yyz.animate.constants.TAG
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.entity.InfoWithName
import com.yyz.animate.functions.appwidget.AnimateWidgetProvider
import com.yyz.animate.utils.DayUtil
import kotlinx.android.synthetic.main.fragment_today.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 8:40
 * version 1.0
 * update none
 **/
class TodayFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = TodayFragment()
    }

    override fun getLayoutId() = R.layout.fragment_today

    private lateinit var adapter: TodayAdapter

    override fun initViews() {
        db.getAnimateInfoDao().getInfoWithNameListLD().observe(requireActivity()) {
            if (updateData()) {
                return@observe
            }
            sendRefreshBroadcast()
            val list = db.getAnimateInfoDao().getInfoWithNameListFromUpdateDay(DayUtil.getToday())
            ns_today.attachDataSource(
                listOf(
                    "今天有${list.size}部番更新",
                    "周一", "周二", "周三", "周四", "周五", "周六", "周日",
                    "本周未看", "正在看"
                )
            )
            if (!::adapter.isInitialized) {
                adapter = TodayAdapter(list)
                rv_today.adapter = adapter
                setAdapterListener()
            } else {
                adapter.setNewData(list)
            }
        }
    }

    private fun setAdapterListener() {
        adapter.setOnItemClickListener(object : TodayAdapter.OnItemClickListener {
            override fun onItemClick(animateInfoBean: AnimateInfoBean) {
                animateInfoBean.episodeList.run {
                    if (this.isNotEmpty()) {
                        last().already = true
                        db.getAnimateInfoDao().updateAnimateInfoBean(animateInfoBean)
                        toast("看完了")
                    }
                }
            }
        })
    }

    override fun initListener() {
        ns_today.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            val today = DayUtil.getToday()
            val tempList = mutableListOf<InfoWithName>()
            tempList.addAll(
                when (position) {
                    0 -> db.getAnimateInfoDao().getInfoWithNameListFromUpdateDay(today)
                    1, 2, 3, 4, 5, 6, 7 -> db.getAnimateInfoDao().getInfoWithNameListFromUpdateDay(position)
                    8 -> {
                        val temp = mutableListOf<InfoWithName>()
                        for (i in 1..today) {
                            temp.addAll(
                                db.getAnimateInfoDao().getInfoWithNameListFromUpdateDay(i)
                                    .filter { !it.infoBean.episodeList.last().already }
                            )
                        }
                        temp
                    }
                    9 -> {
                        db.getAnimateInfoDao().getInfoWithNameListFromState(AnimateState.WATCHING)
                    }
                    else -> mutableListOf()
                }
            )
            adapter.setNewData(tempList)
        }
    }

    private fun updateData(): Boolean {
        var change = false
        val tempList = db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(DayUtil.getToday())
        for (temp in tempList) {
            val weeks = DayUtil.getWeeks(temp.airTime)
            while (temp.episodeList.size <= weeks) {
                temp.episodeList.add(EpisodeState(temp.episodeList.size + 1, false))
                db.getAnimateInfoDao().updateAnimateInfoBean(temp)
                change = true
            }
        }
        return change
    }

    private fun sendRefreshBroadcast() {
        val ids = AppWidgetManager.getInstance(requireContext())
            .getAppWidgetIds(ComponentName(requireContext(), AnimateWidgetProvider::class.java))
        for (id in ids) {
            val intent = Intent(AnimateWidgetProvider.REFRESH_ACTION)
            intent.`package` = requireActivity().packageName
            intent.putExtra("type", AnimateWidgetProvider.TYPE_AUTO)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            requireActivity().sendBroadcast(intent)
            Log.d(TAG, "sendRefreshBroadcast: -----------------")
        }
    }

}