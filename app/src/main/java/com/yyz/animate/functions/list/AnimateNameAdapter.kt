package com.yyz.animate.functions.list

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.entity.NameWithInfo
import kotlinx.android.synthetic.main.item_list.view.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:27
 * version 1.0
 * update none
 **/
class AnimateNameAdapter(private var list: List<NameWithInfo>) :
    RecyclerView.Adapter<AnimateNameAdapter.AnimateNameViewHolder>() {
    inner class AnimateNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animateName: TextView = itemView.tv_item_animate_name
        val ll_name = itemView.ll_list_name
        val recyclerView: RecyclerView = itemView.rv_item_list
        val arrow: ImageView = itemView.iv_list_arrow
        val ll_line = itemView.ll_line
    }

    interface OnItemClickListener {
        fun onNameClick(holder: AnimateNameViewHolder)

        fun onAnimateClick(id: Int)

        fun onNameLongClick(id: Int)

        fun onInfoLongClick(id: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimateNameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        val holder = AnimateNameViewHolder(view)
        holder.ll_name.setOnClickListener {
            onItemClickListener?.onNameClick(holder)
        }
        holder.ll_name.setOnLongClickListener {
            onItemClickListener?.onNameLongClick(list[holder.adapterPosition].nameBean.id ?: -1)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: AnimateNameViewHolder, position: Int) {
        holder.animateName.text = list[position].nameBean.name
        val adapter = AnimateAdapter(list[position].infoBean)
        holder.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : AnimateAdapter.OnItemClickListener {
            // 解决不了嵌套adapter传递值和初始化顺序的问题，只能先把监听器放这个方法里
            // 例如，当需要adapter时，adapter还没初始化；如果先初始化adapter，则初始化时所需的当前name（需要holder的position）无法获得
            override fun onAnimateClick(id: Int) {
                onItemClickListener?.onAnimateClick(id)
                Handler(Looper.getMainLooper()).postDelayed({
                    holder.ll_name.performClick()
                }, 1000)
            }

            override fun onLongClick(id: Int) {
                onItemClickListener?.onInfoLongClick(id)
            }

        })
    }

    override fun getItemCount() = list.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setNewData(list: List<NameWithInfo>) {
        this.list = list
        notifyDataSetChanged()
    }
}