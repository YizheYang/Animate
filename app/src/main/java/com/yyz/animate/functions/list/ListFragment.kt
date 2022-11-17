package com.yyz.animate.functions.list

import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.base.BaseFragment
import com.yyz.animate.constants.Constants
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
    private var listSize = 0

    override fun initViews() {
        db.getAnimateNameDao().getNameWithInfoListLD().observe(requireActivity()) {
            if (!::nameAdapter.isInitialized) {
                nameAdapter = AnimateNameAdapter(it)
                rv_list.adapter = nameAdapter
                setAdapterListener()
            } else {
                nameAdapter.setNewData(it)
            }
            listSize = it.size
        }
    }

    /**
     * 还能向下滑动多少
     */
    private fun getDownDistance(mRecyclerView: RecyclerView): Int {
        val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItem: View = mRecyclerView.getChildAt(0)
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
        val itemCount = layoutManager.itemCount
        val recycleViewHeight: Int = mRecyclerView.height
        val itemHeight = firstVisibleItem.height
        val firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem)
        return (itemCount - firstItemPosition - 1) * itemHeight - recycleViewHeight + firstItemBottom
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
                Handler(Looper.getMainLooper()).postDelayed({
                    if ((rv_list.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == listSize - 1) {
                        rv_list.smoothScrollBy(
                            0, getDownDistance(rv_list) + holder.recyclerView.height, null, Constants.animationDuration
                        )
                    }
                }, 100)
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