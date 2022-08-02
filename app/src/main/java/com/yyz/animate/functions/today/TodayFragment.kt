package com.yyz.animate.functions.today

import com.yyz.animate.R
import com.yyz.animate.base.BaseFragment
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.EpisodeState
import kotlinx.android.synthetic.main.fragment_today.*
import java.util.*

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
        updateData()
        tv_today_title.text = "今天有${
            db.getAnimateInfoDao()
                .getAnimateInfoBeanListFromUpdateDay((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1)
                .size
        }部番更新"
        adapter = TodayAdapter(
            db.getAnimateInfoDao()
                .getAnimateInfoBeanListFromUpdateDay((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1),
            db.getAnimateNameDao().getAnimateNameBeanList()
        )
        rv_today.adapter = adapter
    }

    override fun initListener() {
        adapter.setOnItemClickListener(object : TodayAdapter.OnItemClickListener {
            override fun onItemClick(animateInfoBean: AnimateInfoBean) {
                animateInfoBean.episode.last().already = true
                db.getAnimateInfoDao().updateAnimateInfoBean(animateInfoBean)
                toast("看完了")
            }
        })
    }

    private fun updateData() {
        val tempList = db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(
            (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
        )
        for (temp in tempList) {
            if (temp.episode.size <= ((Date(System.currentTimeMillis()).time - temp.airTime.time) / (1000 * 60 * 60 * 24 * 7)).toInt()) {
                temp.episode.add(EpisodeState(temp.episode.size + 1, false))
                db.getAnimateInfoDao().updateAnimateInfoBean(temp)
            }
        }

    }

}