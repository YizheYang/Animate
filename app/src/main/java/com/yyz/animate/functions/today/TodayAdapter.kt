package com.yyz.animate.functions.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.InfoWithName
import kotlinx.android.synthetic.main.item_today.view.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.1 上午 11:07
 * version 1.0
 * update none
 **/
class TodayAdapter(private var list: List<InfoWithName>) :
    RecyclerView.Adapter<TodayAdapter.TodayViewHolder>() {

    inner class TodayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animateName: TextView = itemView.tv_item_today_animateName
        val episode: TextView = itemView.tv_item_today_episode
    }

    interface OnItemClickListener {
        fun onItemClick(animateInfoBean: AnimateInfoBean)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today, parent, false)
        val holder = TodayViewHolder(view)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(list[holder.adapterPosition].infoBean)
        }
        return holder
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        list[position].run {
            holder.animateName.text = nameBean.name + " " + infoBean.season
            holder.episode.text = "第${infoBean.episode.last().no}集"
        }
    }

    override fun getItemCount() = list.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setNewData(list: List<InfoWithName>) {
        this.list = list
        notifyDataSetChanged()
    }
}