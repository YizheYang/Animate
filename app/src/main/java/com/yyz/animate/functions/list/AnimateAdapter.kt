package com.yyz.animate.functions.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.constants.AnimateType
import com.yyz.animate.entity.AnimateInfoBean
import kotlinx.android.synthetic.main.item_item_list.view.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:56
 * version 1.0
 * update none
 **/
class AnimateAdapter(private val list: List<AnimateInfoBean>) :
    RecyclerView.Adapter<AnimateAdapter.AnimateViewHolder>() {
    inner class AnimateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animate: TextView = itemView.tv_item_itemList
    }

    interface OnItemClickListener {
        fun onAnimateClick(id: Int)

        fun onLongClick(id: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item_list, parent, false)
        val holder = AnimateViewHolder(view)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onAnimateClick(list[holder.adapterPosition].id ?: -1)
        }
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.onLongClick(list[holder.adapterPosition].id ?: -1)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: AnimateViewHolder, position: Int) {
        val sb = StringBuilder()
        list[position].let {
            when (it.type) {
                AnimateType.MOVIE -> sb.append("剧场版").append(" ")
                AnimateType.OVA -> sb.append("OVA").append(" ")
                else -> {}
            }
            sb.append("第").append(it.season).append("季")
        }
        holder.animate.text = sb.toString()
    }

    override fun getItemCount() = list.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}