package com.yyz.animate.functions.list

import android.app.AlertDialog
import android.view.View
import com.yyz.animate.R
import com.yyz.animate.base.BaseFragment
import com.yyz.animate.functions.add.AddActivity
import com.yyz.animate.functions.info.InfoActivity
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

    private lateinit var nameAdapter: AnimateNameAdapter

    override fun initViews() {
        db.getAnimateNameDao().getNameWithInfoListLD().observe(requireActivity()) {
            if (!::nameAdapter.isInitialized) {
                nameAdapter = AnimateNameAdapter(it)
                rv_list.adapter = nameAdapter
                setAdapterListener()
            } else {
                nameAdapter.setNewData(it)
            }
        }
    }

    private fun setAdapterListener() {
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
                db.getAnimateNameDao().getAnimateNameBeanFormId(id)?.let {
                    AlertDialog.Builder(requireContext())
                        .setMessage("是否要删除《" + it.name + "》系列作品")
                        .setTitle("警告")
                        .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                        .setPositiveButton("确定") { dialog, which ->
                            db.getAnimateNameDao().deleteAnimateNameBean(it)
                            db.getAnimateInfoDao().deleteAnimateInfoBeanFromNameId(id)
                        }
                        .create().show()
                }
            }

            override fun onInfoLongClick(id: Int) {
                db.getAnimateInfoDao().getAnimateInfoBeanFromId(id)?.let { info ->
                    db.getAnimateNameDao().getAnimateNameBeanFormId(info.nameId)?.let { name ->
                        AlertDialog.Builder(requireContext())
                            .setMessage("是否要删除《" + name.name + "》" + info.season)
                            .setTitle("警告")
                            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                            .setPositiveButton("确定") { dialog, which ->
                                db.getAnimateInfoDao().deleteAnimateInfoBean(info)
                            }
                            .create().show()
                    }

                }

            }
        })
    }

    override fun initListener() {
        fab_list.setOnClickListener {
            AddActivity.add(requireContext())
        }
    }

}