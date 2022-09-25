package com.yyz.animate.functions.add

import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yyz.animate.constants.AnimateState
import com.yyz.animate.constants.AnimateType
import com.yyz.animate.constants.TAG
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.entity.EpisodeState
import com.yyz.animate.model.AnimateDatabase
import com.yyz.animate.utils.DayUtil
import org.angmarch.views.NiceSpinner
import java.text.SimpleDateFormat
import java.util.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.26 下午 5:07
 * version 1.0
 * update none
 **/
class AddViewModel(private val context: AppCompatActivity, val id: Int) : ViewModel() {
    private var mode = AddActivity.ADD_MODE
    private val db = AnimateDatabase.getInstance(context)

    val name = MutableLiveData<String>("")
    val nameEnable = MutableLiveData<Boolean>(true)
    val season = MutableLiveData<String>("")
    val seasonEnable = MutableLiveData<Boolean>(true)
    val episode = MutableLiveData<String>("")
    val episodeEnable = MutableLiveData<Boolean>(true)

    val type = MutableLiveData<Int>(0)
    val typeEnable = MutableLiveData<Boolean>(true)
    private val tempTypeList = listOf(AnimateType.TV, AnimateType.MOVIE, AnimateType.OVA)
    val typeList = tempTypeList.map { it.type }

    val date = MutableLiveData<Long>(Date(System.currentTimeMillis()).time)

    val state = MutableLiveData<Int>(0)
    private val tempStateList = listOf(AnimateState.WILL, AnimateState.WATCHING, AnimateState.ALREADY)
    val stateList = tempStateList.map { it.state }

    val nameBeanList: MutableList<AnimateNameBean> = mutableListOf()
    var chooseNameBean: AnimateNameBean? = null

    init {
        mode = if (id == -1) {
            AddActivity.ADD_MODE
        } else {
            AddActivity.EDIT_MODE
        }
        if (mode == AddActivity.EDIT_MODE) {
            val bean = db.getAnimateInfoDao().getInfoWithNameFromId(id)
            name.value = bean.nameBean.name
            season.value = bean.infoBean.season.toString()
            episode.value = bean.infoBean.episodeList.size.toString()
            date.value = bean.infoBean.airTime.time
            state.value = bean.infoBean.state.id - 1
            type.value = bean.infoBean.type.id - 1

            nameEnable.value = false
            seasonEnable.value = false
            typeEnable.value = false
            if (type.value == AnimateType.MOVIE.id - 1) {
                episodeEnable.value = false
            }
        } else if (mode == AddActivity.ADD_MODE) {
            nameBeanList.addAll(db.getAnimateNameDao().getAnimateNameBeanList())
        }
    }

    fun onClick(view: View) {
        if (name.value?.isEmpty() == true || name.value == "" ||
            season.value?.isEmpty() == true || season.value == "" ||
            episode.value?.isEmpty() == true || episode.value == ""
        ) {
            Toast.makeText(context, "内容不完整", Toast.LENGTH_SHORT).show()
            return
        }
        when (mode) {
            AddActivity.ADD_MODE -> {
                val nameBean =
                    chooseNameBean ?: db.getAnimateNameDao()
                        .getAnimateNameBeanFromName(name.value ?: throw IllegalArgumentException())
                    ?: initNewNameBean()
                if (!initNewInfoBean(nameBean)) {
                    return
                }
            }
            AddActivity.EDIT_MODE -> {
                val infoBean = db.getAnimateInfoDao().getAnimateInfoBeanFromId(id) ?: throw NullPointerException()
                editInfoBean(infoBean)
            }
        }
        Toast.makeText(context, "编辑完毕", Toast.LENGTH_SHORT).show()
        context.setResult(AppCompatActivity.RESULT_OK)
        context.finish()
    }

    private fun initNewNameBean(): AnimateNameBean {
        val bean = AnimateNameBean(
            null,
            name.value ?: throw IllegalArgumentException(),
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
            0
        )
        db.getAnimateNameDao().insertAnimateNameBean(bean)
        return db.getAnimateNameDao().getAnimateNameBeanFromName(name.value ?: throw IllegalArgumentException())
            ?: throw NullPointerException()
    }

    private fun initNewInfoBean(nameBean: AnimateNameBean): Boolean {
        val temp = db.getAnimateNameDao().getNameWithInfoFromId(nameBean.id ?: throw NullPointerException("番名id错误"))
        for (b in temp.infoBean) {
            if (type.value == b.type.id - 1 && season.value?.toInt() == b.season) {
                Toast.makeText(context, "季度重复", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        val bean = AnimateInfoBean(
            null,
            nameBean.id,
            season.value?.toInt() ?: throw NullPointerException("季度错误"),
            episode.value?.toInt() ?: throw NullPointerException("集数错误"),
            mutableListOf(),
            tempStateList[state.value ?: throw NullPointerException("状态错误")],
            Date(System.currentTimeMillis()),
            Date(date.value ?: throw NullPointerException("首播时间错误")),
            DayUtil.getDay(date.value ?: throw NullPointerException("更新日错误")),
            tempTypeList[type.value ?: throw NullPointerException("类型错误")]
        )
        db.getAnimateInfoDao().insertAnimateInfoBean(bean)
        return true
    }

    private fun editInfoBean(infoBean: AnimateInfoBean) {
        val e = episode.value?.toInt() ?: throw NullPointerException()
        val bean = AnimateInfoBean(
            infoBean.id,
            infoBean.nameId,
            infoBean.season,
            e,
            kotlin.run {
                if (e == 0) {
                    mutableListOf()
                } else {
                    val list = mutableListOf<EpisodeState>()
                    for (i in 1..e) {
                        if (i <= infoBean.episodeList.size) {
                            list.add(infoBean.episodeList[i - 1])
                        } else {
                            list.add(EpisodeState(i, false))
                        }
                    }
                    list
                }
            },
            tempStateList[state.value ?: throw NullPointerException()],
            infoBean.initTime,
            Date(date.value ?: throw NullPointerException()),
            DayUtil.getDay(date.value ?: throw NullPointerException()),
            tempTypeList[type.value ?: throw NullPointerException()]
        )
        db.getAnimateInfoDao().updateAnimateInfoBean(bean)
    }

    fun onDateChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        date.postValue(c.time.time)
        // 很有趣的地方，这里post并不会立即执行，所以log看到的是之前的数据
        Log.d(TAG, "onDateChange: ==================${SimpleDateFormat.getDateInstance().format(date.value)}")
    }

    fun onSpinnerItemSelected(parents: NiceSpinner, view: View, position: Int, id: Long) {
        if (position == AnimateType.MOVIE.id - 1) {
            episode.value = "1"
            episodeEnable.value = false
        } else {
            episodeEnable.value = true
        }
    }
}