package com.example.bfaasubmission3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.detail.DetailActivity
import com.example.bfaasubmission3.detail.DetailActivity.Companion.EXTRA_DATA
import kotlinx.android.synthetic.main.activity_list.view.*

class ListActivityAdapter(val context: Context): RecyclerView.Adapter<ListActivityAdapter.ListViewHolder>() {
    private val mData = ArrayList<DataUserItems>()

    fun setData(items: ArrayList<DataUserItems>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
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

            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, user)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
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