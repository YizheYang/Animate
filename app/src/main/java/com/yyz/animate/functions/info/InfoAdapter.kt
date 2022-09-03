package com.yyz.animate.functions.info

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.entity.AnimateInfoBean
import kotlinx.android.synthetic.main.item_info.view.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.8.1 下午 9:22
 * version 1.0
 * update none
 **/
class InfoAdapter(private var animateInfoBean: AnimateInfoBean) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val episode: TextView = itemView.tv_info_episode
        val cardView: CardView = itemView.cv_info
    }

    interface OnItemClickListener {
        fun onItemClick(animateInfoBean: AnimateInfoBean, position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        val holder = InfoViewHolder(view)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(animateInfoBean, holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.episode.text = animateInfoBean.episodeList[position].no.toString()
        holder.cardView.setCardBackgroundColor(
            if (animateInfoBean.episodeList[position].already) {
                Color.GREEN
            } else {
                Color.WHITE
            }
        )
    }

    override fun getItemCount() = animateInfoBean.episodeList.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setNewData(bean: AnimateInfoBean) {
        this.animateInfoBean = bean
        notifyDataSetChanged()
    }
}