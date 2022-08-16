package com.yyz.animate.functions.Info

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.yyz.animate.R
import com.yyz.animate.base.BaseActivity
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.functions.Add.AddActivity
import com.yyz.animate.utils.DateConverter
import com.yyz.animate.utils.DayUtil
import kotlinx.android.synthetic.main.activity_info.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:17
 * version 1.0
 * update none
 **/
class InfoActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, id: Int) {
            val intent = Intent(context, InfoActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", id)
            intent.putExtra("bundle", bundle)
            context.startActivity(intent)
        }

    }

    override fun getLayoutId() = R.layout.activity_info

    private lateinit var adapter: InfoAdapter
    private var id = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initViews() {
        id = intent.getBundleExtra("bundle")?.getInt("id") ?: -1

        db.getAnimateInfoDao().getInfoWithNameFromId(id).observe(this) {
            if (it.infoBean.episode.size <= DayUtil.getWeeks(it.infoBean.airTime)) {
                updateData(it.infoBean)
                return@observe
            }
            initInfo(it.infoBean, it.nameBean)
            if (!::adapter.isInitialized) {
                adapter = InfoAdapter(it.infoBean)
                rv_info.adapter = adapter
                rv_info.layoutManager = GridLayoutManager(this, 4)
                setAdapterListener()
            } else {
                adapter.setNewData(it.infoBean)
            }
        }
    }

    private fun setAdapterListener() {
        adapter.setOnItemClickListener(object : InfoAdapter.OnItemClickListener {
            override fun onItemClick(animateInfoBean: AnimateInfoBean, position: Int) {
                animateInfoBean.episode[position].already = !animateInfoBean.episode[position].already
                db.getAnimateInfoDao().updateAnimateInfoBean(animateInfoBean)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initListener() {
        btn_info_edit.setOnClickListener {
            AddActivity.edit(this, id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initInfo(animateInfoBean: AnimateInfoBean, animateNameBean: AnimateNameBean) {
        val sb = StringBuilder()
        sb.append(animateNameBean.name).append("    ")
            .append("第").append(animateInfoBean.season).append("季")
        tv_info_name.text = sb.toString()
        tv_info_time.text = DateConverter.converter(animateInfoBean.airTime)
        tv_info_state.text = animateInfoBean.state.state
    }

    private fun updateData(temp: AnimateInfoBean) {
        val weeks = DayUtil.getWeeks(temp.airTime)
        while (temp.episode.size <= weeks) {
            temp.episode.add(EpisodeState(temp.episode.size + 1, false))
            db.getAnimateInfoDao().updateAnimateInfoBean(temp)
        }
    }
}