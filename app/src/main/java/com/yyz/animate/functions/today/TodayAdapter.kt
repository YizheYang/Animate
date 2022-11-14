package com.yyz.animate.functions.today

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
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
        val background: CardView = itemView.cv_item_today
    }

    interface OnItemClickListener {
        fun onItemClick(animateInfoBean: AnimateInfoBean, holder: TodayViewHolder)
    }

    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today, parent, false)
        context = parent.context
        val holder = TodayViewHolder(view)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(list[holder.adapterPosition].infoBean, holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        list[position].run {
            holder.animateName.text = nameBean.name + " " + infoBean.season
            holder.episode.text = "第${
                infoBean.episodeList.run {
                    if (this.isEmpty()) {
                        "0"
                    } else {
                        last().no
                    }
                }
            }集"
            if (this.infoBean.episodeList.last().already) {
                holder.background.setCardBackgroundColor(context.resources.getColor(R.color.today_item_bg_already_seen))
            } else {
                holder.background.setCardBackgroundColor(context.resources.getColor(R.color.today_item_bg_not_seen))
            }
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