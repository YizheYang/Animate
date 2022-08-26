package com.yyz.animate.functions.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.yyz.animate.R
import com.yyz.animate.base.BaseActivity
import com.yyz.animate.constants.AnimateState
import com.yyz.animate.constants.AnimateType
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.utils.DayUtil
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:16
 * version 1.0
 * update none
 **/
class AddActivity : BaseActivity() {
    companion object {
        @JvmStatic
        fun add(context: Context) {
            val intent = Intent(context, AddActivity::class.java)
            context.startActivity(intent)
        }

        @JvmStatic
        fun edit(context: Context, id: Int) {
            val intent = Intent(context, AddActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", id)
            intent.putExtra("bundle", bundle)
            context.startActivity(intent)
        }

        @JvmStatic
        fun add(context: Context, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, AddActivity::class.java)
            launcher.launch(intent)
        }

        @JvmStatic
        fun edit(context: Context, id: Int, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, AddActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", id)
            intent.putExtra("bundle", bundle)
            launcher.launch(intent)
        }

        private const val ADD_MODE = 0
        private const val EDIT_MODE = 1
    }

    override fun getLayoutId() = R.layout.activity_add

    private var mode = ADD_MODE

    override fun initViews() {
        ns_add_type.attachDataSource(listOf(AnimateType.TV, AnimateType.MOVIE, AnimateType.OVA))
        ns_add_state.attachDataSource(
            listOf(
                AnimateState.WILL.state,
                AnimateState.WATCHING.state,
                AnimateState.ALREADY.state
            )
        )
        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null) {
            mode = EDIT_MODE
        }
        if (mode == EDIT_MODE) {
            val animateInfoBean = db.getAnimateInfoDao().getAnimateInfoBeanFromId(bundle?.getInt("id") ?: -1)!!
            val animateNameBean = db.getAnimateNameDao().getAnimateNameBeanFormId(animateInfoBean.nameId)!!
            et_add_name.setText(animateNameBean.name)
            et_add_name.isEnabled = false
            et_add_season.setText(animateInfoBean.season.toString())
            et_add_season.isEnabled = false
            et_add_episodes.setText(animateInfoBean.episodes.toString())
            cv_add_airtime.date = animateInfoBean.airTime.time
            ns_add_state.selectedIndex = animateInfoBean.state.id - 1
            ns_add_type.selectedIndex = animateInfoBean.type.id - 1
            ns_add_type.isEnabled = false
        } else if (mode == ADD_MODE) {
            ns_add_state.selectedIndex = 0
        }
    }

    override fun initListener() {
        var date = db.getAnimateInfoDao().getAnimateInfoBeanFromId(
            intent.getBundleExtra("bundle")?.getInt("id") ?: -1
        )?.initTime ?: Date(System.currentTimeMillis())

        cv_add_airtime.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val c = Calendar.getInstance()
            c.set(year, month, dayOfMonth)
            date = c.time
        }

        ns_add_type.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            if (position == AnimateType.MOVIE.id - 1) {
                et_add_episodes.setText("1")
                et_add_episodes.isEnabled = false
            } else {
                et_add_episodes.isEnabled = true
            }
        }

        btn_add_confirm.setOnClickListener {
            if (et_add_name.text.isEmpty() || et_add_season.text.isEmpty()) {
                toast("内容不完整")
                return@setOnClickListener
            }
            val animateNameBean = db.getAnimateNameDao().getAnimateNameBeanFromName(et_add_name.text.toString())
            if (animateNameBean == null) {
                // 找不到肯定是新增
                val tempName = AnimateNameBean(
                    null,
                    et_add_name.text.toString(),
                    Date(System.currentTimeMillis()),
                    Date(System.currentTimeMillis()),
                    0
                )
                db.getAnimateNameDao().insertAnimateNameBean(tempName)
                val episodes = run {
                    try {
                        et_add_episodes.text.toString().toInt()
                    } catch (e: NumberFormatException) {
                        0
                    }
                }
                val tempInfo = AnimateInfoBean(
                    null,
                    db.getAnimateNameDao().getAnimateNameBeanFromName(et_add_name.text.toString())?.id ?: -1,
                    et_add_season.text.toString().toInt(),
                    episodes,
                    run {
                        if (episodes == 0) {
                            mutableListOf()
                        } else {
                            val list = mutableListOf<EpisodeState>()
                            for (i in 1..episodes) {
                                list.add(EpisodeState(i, false))
                            }
                            list
                        }
                    },
                    when (ns_add_state.selectedIndex) {
                        0 -> AnimateState.WILL
                        1 -> AnimateState.WATCHING
                        2 -> AnimateState.ALREADY
                        else -> AnimateState.WILL
                    },
                    Date(System.currentTimeMillis()),
                    date,
                    DayUtil.getDay(date),
                    when (ns_add_type.selectedIndex) {
                        0 -> AnimateType.TV
                        1 -> AnimateType.MOVIE
                        2 -> AnimateType.OVA
                        else -> AnimateType.TV
                    }
                )
                db.getAnimateInfoDao().insertAnimateInfoBean(tempInfo)
            } else {
                if (mode == ADD_MODE) {
                    // 找得到名字但是是新增，相当于只有第一季，现在添加第二季
                    val season = et_add_season.text.toString().toInt()
                    val nameId =
                        db.getAnimateNameDao().getAnimateNameBeanFromName(et_add_name.text.toString())?.id ?: -1
                    val tempList = db.getAnimateInfoDao().getAnimateInfoBeanListFromNameId(nameId)
                    if (season in tempList.map { it.season }) {
                        toast("重复季度")
                        return@setOnClickListener
                    }
                    val episodes = run {
                        try {
                            et_add_episodes.text.toString().toInt()
                        } catch (e: NumberFormatException) {
                            0
                        }
                    }
                    val tempInfo = AnimateInfoBean(
                        null,
                        nameId,
                        season,
                        episodes,
                        run {
                            if (episodes == 0) {
                                mutableListOf()
                            } else {
                                val list = mutableListOf<EpisodeState>()
                                for (i in 1..episodes) {
                                    list.add(EpisodeState(i, false))
                                }
                                list
                            }
                        },
                        when (ns_add_state.selectedIndex) {
                            0 -> AnimateState.WILL
                            1 -> AnimateState.WATCHING
                            2 -> AnimateState.ALREADY
                            else -> AnimateState.WILL
                        },
                        Date(System.currentTimeMillis()),
                        date,
                        DayUtil.getDay(date),
                        when (ns_add_type.selectedIndex) {
                            0 -> AnimateType.TV
                            1 -> AnimateType.MOVIE
                            2 -> AnimateType.OVA
                            else -> AnimateType.TV
                        }
                    )
                    db.getAnimateInfoDao().insertAnimateInfoBean(tempInfo)
                } else if (mode == EDIT_MODE) {
                    // 修改模式
                    val temp = db.getAnimateInfoDao()
                        .getAnimateInfoBeanFromId(intent.getBundleExtra("bundle")?.getInt("id") ?: -1)!!
                    val episodes = run {
                        try {
                            et_add_episodes.text.toString().toInt()
                        } catch (e: NumberFormatException) {
                            0
                        }
                    }
                    val tempInfo = AnimateInfoBean(
                        temp.id,
                        temp.nameId,
                        temp.season,
                        episodes,
                        run {
                            if (episodes == 0) {
                                mutableListOf()
                            } else {
                                val list = mutableListOf<EpisodeState>()
                                for (i in 1..episodes) {
                                    if (i <= temp.episode.size) {
                                        list.add(temp.episode[i - 1])
                                    } else {
                                        list.add(EpisodeState(i, false))
                                    }
                                }
                                list
                            }
                        },
                        when (ns_add_state.selectedIndex) {
                            0 -> AnimateState.WILL
                            1 -> AnimateState.WATCHING
                            2 -> AnimateState.ALREADY
                            else -> AnimateState.WILL
                        },
                        temp.initTime,
                        date,
                        DayUtil.getDay(date),
                        when (ns_add_type.selectedIndex) {
                            0 -> AnimateType.TV
                            1 -> AnimateType.MOVIE
                            2 -> AnimateType.OVA
                            else -> AnimateType.TV
                        }
                    )
                    db.getAnimateInfoDao().updateAnimateInfoBean(tempInfo)
                }
            }
            toast("编辑完成")
            setResult(RESULT_OK)
            finish()
        }

    }
}