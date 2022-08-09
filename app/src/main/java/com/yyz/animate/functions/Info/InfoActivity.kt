package com.yyz.animate.functions.Info

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.yyz.animate.R
import com.yyz.animate.base.BaseActivity
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.functions.Add.AddActivity
import com.yyz.animate.utils.DateConverter
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
    private lateinit var animateInfoBean: AnimateInfoBean
    private var id = 0
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val list = mutableListOf<EpisodeState>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initViews() {
        id = intent.getBundleExtra("bundle")?.getInt("id") ?: 0
        animateInfoBean = db.getAnimateInfoDao().getAnimateInfoBeanFromId(id)!!
        val animateNameBean = db.getAnimateNameDao().getAnimateNameBeanFormId(animateInfoBean.nameId)!!
        initInfo(animateInfoBean, animateNameBean)
        list.addAll(animateInfoBean.episode)
        adapter = InfoAdapter(list)
        rv_info.adapter = adapter
        rv_info.layoutManager = GridLayoutManager(this, 4)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                animateInfoBean = db.getAnimateInfoDao().getAnimateInfoBeanFromId(id)!!
                val animateNameBean = db.getAnimateNameDao().getAnimateNameBeanFormId(animateInfoBean.nameId)!!
                initInfo(animateInfoBean, animateNameBean)
                list.clear()
                list.addAll(animateInfoBean.episode)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initListener() {
        adapter.setOnItemClickListener(object : InfoAdapter.OnItemClickListener {
            override fun onItemClick(episodeState: EpisodeState) {
                episodeState.already = !episodeState.already
                adapter.notifyDataSetChanged()
                db.getAnimateInfoDao().updateAnimateInfoBean(animateInfoBean)
            }
        })

        btn_info_edit.setOnClickListener {
//            AddActivity.edit(this, id)

            AddActivity.edit(this, id, launcher)
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
}