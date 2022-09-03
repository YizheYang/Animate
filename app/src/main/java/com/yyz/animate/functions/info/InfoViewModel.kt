package com.yyz.animate.functions.info

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.entity.InfoWithName
import com.yyz.animate.functions.add.AddActivity
import com.yyz.animate.model.AnimateDatabase
import com.yyz.animate.utils.DayUtil

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.20 下午 1:50
 * version 1.0
 * update none
 **/
class InfoViewModel(private val context: AppCompatActivity, id: Int) : ViewModel() {

    private val db: AnimateDatabase = AnimateDatabase.getInstance(context)
    private lateinit var adapter: InfoAdapter

    val beanLD = db.getAnimateInfoDao().getInfoWithNameFromIdLD(id)
    val name = MutableLiveData<String>("null")
    val date = MutableLiveData<String>("null")
    val state = MutableLiveData<String>("null")

    fun onClick(view: View) {
        AddActivity.edit(context, beanLD.value?.infoBean?.id ?: throw NullPointerException())
    }

    fun observeBean(it: InfoWithName, rv: RecyclerView) {
        if (it.infoBean.episodeList.size <= DayUtil.getWeeks(it.infoBean.airTime)) {
            updateData(it.infoBean)
            return
        }
        if (!::adapter.isInitialized) {
            adapter = InfoAdapter(it.infoBean)
            rv.adapter = adapter
            rv.layoutManager = GridLayoutManager(context, 4)
            setAdapterListener()
        } else {
            adapter.setNewData(it.infoBean)
        }
    }

    private fun setAdapterListener() {
        adapter.setOnItemClickListener(object : InfoAdapter.OnItemClickListener {
            override fun onItemClick(animateInfoBean: AnimateInfoBean, position: Int) {
                animateInfoBean.episodeList[position].already = !animateInfoBean.episodeList[position].already
                db.getAnimateInfoDao().updateAnimateInfoBean(animateInfoBean)
            }
        })
    }

    private fun updateData(temp: AnimateInfoBean) {
        val weeks = DayUtil.getWeeks(temp.airTime)
        val old = temp.episodeList.size
        while (temp.episodeList.size <= weeks) {
            temp.episodeList.add(EpisodeState(temp.episodeList.size + 1, false))
        }
        if (temp.episodeList.size != old) {
            db.getAnimateInfoDao().updateAnimateInfoBean(temp)
        }
    }

}