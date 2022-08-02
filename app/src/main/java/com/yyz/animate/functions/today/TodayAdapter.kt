package com.yyz.animate.functions.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import kotlinx.android.synthetic.main.item_today.view.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.1 上午 11:07
 * version 1.0
 * update none
 **/
class TodayAdapter(private val animateList: List<AnimateInfoBean>, private val nameList: List<AnimateNameBean>) :
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
            onItemClickListener?.onItemClick(animateList[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        holder.animateName.text =
            nameList.find { it.id == animateList[position].nameId }?.name + animateList[position].season
        holder.episode.text = "第${animateList[position].episode.last().no}集"
    }

    override fun getItemCount() = animateList.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

}