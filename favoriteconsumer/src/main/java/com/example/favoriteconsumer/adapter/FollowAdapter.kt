package com.example.favoriteconsumer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteconsumer.R
import com.example.favoriteconsumer.data.DataFollow
import kotlinx.android.synthetic.main.follow_list.view.*

class FollowAdapter:RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {
    private val mData = ArrayList<DataFollow>()

    fun setData(items: ArrayList<DataFollow>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class FollowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(follow: DataFollow){
            with(itemView){
                Glide.with(itemView.context)
                    .load(follow.avatar)
                    .into(img_follow_list)
                tv_follow_list.text = follow.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_list, parent, false)
        return FollowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(mData[position])
    }

}