package com.yyz.animate.functions.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yyz.animate.R
import com.yyz.animate.entity.AnimateInfoBean
import com.yyz.animate.entity.AnimateNameBean
import kotlinx.android.synthetic.main.item_list.view.*

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:27
 * version 1.0
 * update none
 **/
class AnimateNameAdapter(private val nameList: List<AnimateNameBean>, private val animateList: List<AnimateInfoBean>) :
    RecyclerView.Adapter<AnimateNameAdapter.AnimateNameViewHolder>() {
    inner class AnimateNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animateName: TextView = itemView.tv_item_animate_name
        val ll = itemView.ll
        val recyclerView: RecyclerView = itemView.rv_item_list
        val arrow: ImageView = itemView.iv_list_arrow
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
        holder.ll.setOnClickListener {
            onItemClickListener?.onNameClick(holder)
        }
        holder.ll.setOnLongClickListener {
            onItemClickListener?.onNameLongClick(nameList[holder.adapterPosition].id ?: -1)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: AnimateNameViewHolder, position: Int) {
        holder.animateName.text = nameList[position].name
        val adapter = AnimateAdapter(animateList.filter { it.nameId == nameList[position].id })
        holder.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : AnimateAdapter.OnItemClickListener {
            // 解决不了嵌套adapter传递值和初始化顺序的问题，只能先把监听器放这个方法里
            // 例如，当需要adapter时，adapter还没初始化；如果先初始化adapter，则初始化时所需的当前name（需要holder的position）无法获得
            override fun onAnimateClick(id: Int) {
                onItemClickListener?.onAnimateClick(id)
            }

            override fun onLongClick(id: Int) {
                onItemClickListener?.onInfoLongClick(id)
            }

        })
    }

    override fun getItemCount() = nameList.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}