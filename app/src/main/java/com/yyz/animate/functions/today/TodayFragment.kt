package com.yyz.animate.functions.today

import androidx.lifecycle.ViewModelProvider
import com.yyz.animate.MainViewModel
import com.yyz.animate.R
import com.yyz.animate.base.BaseFragment
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
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
    private val infoList = mutableListOf<AnimateInfoBean>()
    private val nameList = mutableListOf<AnimateNameBean>()
    private lateinit var vm: MainViewModel

    override fun initViews() {
        vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        updateData()
        val today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
        ns_today.attachDataSource(
            listOf(
                "今天有${db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(today).size}部番更新",
                "周一", "周二", "周三", "周四", "周五", "周六", "周日", "本周未看"
            )
        )

        infoList.addAll(db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(today))
        nameList.addAll(db.getAnimateNameDao().getAnimateNameBeanList())
        adapter = TodayAdapter(infoList, nameList)
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

        vm.refresh.observe(requireActivity()) {
            if (it == true) {
                updateData()
                val today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
                ns_today.attachDataSource(
                    listOf(
                        "今天有${db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(today).size}部番更新",
                        "周一", "周二", "周三", "周四", "周五", "周六", "周日", "本周未看"
                    )
                )
                infoList.clear()
                infoList.addAll(db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(today))
                adapter.notifyDataSetChanged()
                vm.refresh.value = false
            }
        }

        ns_today.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            val today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
            infoList.clear()
            infoList.addAll(
                if (position == 8) {
                    val temp = mutableListOf<AnimateInfoBean>()
                    for (i in 1..today) {
                        temp.addAll(
                            db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(i)
                                .filter { !it.episode.last().already }
                        )
                    }
                    temp
                } else {
                    db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(
                        if (position == 0) {
                            today
                        } else {
                            position
                        }
                    )
                }

            )
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateData() {
        val tempList = db.getAnimateInfoDao().getAnimateInfoBeanListFromUpdateDay(
            (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
        )
        for (temp in tempList) {
            val weeks =
                ((Date(System.currentTimeMillis()).time - temp.airTime.time) / (1000 * 60 * 60 * 24 * 7)).toInt()
            while (temp.episode.size <= weeks) {
                temp.episode.add(EpisodeState(temp.episode.size + 1, false))
                db.getAnimateInfoDao().updateAnimateInfoBean(temp)
            }
        }

    }

}