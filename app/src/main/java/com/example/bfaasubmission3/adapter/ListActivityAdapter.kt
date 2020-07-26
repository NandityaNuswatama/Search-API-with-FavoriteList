package com.example.bfaasubmission3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.data.DataUserItems
import kotlinx.android.synthetic.main.activity_list.view.*

class ListActivityAdapter: RecyclerView.Adapter<ListActivityAdapter.ListViewHolder>() {
    private val mData = ArrayList<DataUserItems>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<DataUserItems>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUserItems: DataUserItems)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: DataUserItems) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(60, 60))
                .into(itemView.img_avatar)
            itemView.tv_name.text = user.name
            itemView.tv_company.text = user.company
            itemView.tv_repository.text = user.repository
            itemView.tv_followers.text = user.followers
            itemView.tv_following.text = user.following

            itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListActivityAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }
}