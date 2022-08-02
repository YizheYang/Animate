package com.yyz.animate.functions.list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.yyz.animate.R
import com.yyz.animate.base.BaseFragment
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import com.yyz.animate.functions.Add.AddActivity
import com.yyz.animate.functions.Info.InfoActivity
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 9:20
 * version 1.0
 * update none
 **/
class ListFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    override fun getLayoutId() = R.layout.fragment_list

    private val nameList = mutableListOf<AnimateNameBean>()
    private val animateList = mutableListOf<AnimateInfoBean>()
    private lateinit var nameAdapter: AnimateNameAdapter
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun initViews() {
        nameList.addAll(db.getAnimateNameDao().getAnimateNameBeanList())
        animateList.addAll(db.getAnimateInfoDao().getAnimateInfoBeanList())
        nameAdapter = AnimateNameAdapter(nameList, animateList)
        rv_list.adapter = nameAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                refreshList()
            }
        }
    }

    override fun initListener() {
        nameAdapter.setOnItemClickListener(object : AnimateNameAdapter.OnItemClickListener {
            override fun onNameClick(holder: AnimateNameAdapter.AnimateNameViewHolder) {
                holder.recyclerView.visibility = holder.recyclerView.visibility.run {
                    when (this) {
                        View.VISIBLE -> {
                            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
                            View.GONE
                        }
                        View.GONE -> {
                            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                            View.VISIBLE
                        }
                        else -> throw IllegalStateException()
                    }
                }
            }

            override fun onAnimateClick(id: Int) {
                InfoActivity.start(requireContext(), id)
            }

            override fun onNameLongClick(id: Int) {
                val temp = db.getAnimateNameDao().getAnimateNameBeanFormId(id)!!
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("是否要删除《" + temp.name + "》系列作品")
                    .setTitle("警告")
                    .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("确定") { dialog, which ->
                        db.getAnimateNameDao().deleteAnimateNameBean(temp)
                        db.getAnimateInfoDao().deleteAnimateInfoBeanFromNameId(id)
                        refreshList()
                    }
                    .create().show()
            }

            override fun onInfoLongClick(id: Int) {
                val temp = db.getAnimateInfoDao().getAnimateInfoBeanFromId(id)!!
                val tempName = db.getAnimateNameDao().getAnimateNameBeanFormId(temp.nameId)!!
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("是否要删除《" + tempName.name + "》" + temp.season)
                    .setTitle("警告")
                    .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("确定") { dialog, which ->
                        db.getAnimateInfoDao().deleteAnimateInfoBean(temp)
                        refreshList()
                    }
                    .create().show()
            }
        })

        fab_list.setOnClickListener {
            AddActivity.add(requireContext(), launcher)
        }
    }


    private fun refreshList() {
        nameList.clear()
        animateList.clear()
        nameList.addAll(db.getAnimateNameDao().getAnimateNameBeanList())
        animateList.addAll(db.getAnimateInfoDao().getAnimateInfoBeanList())
        nameAdapter.notifyDataSetChanged()
    }

}