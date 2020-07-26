package com.example.bfaasubmission3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.data.DataUserItems
import kotlinx.android.synthetic.main.activity_favorite.view.*
import kotlinx.android.synthetic.main.favorite_list.view.*

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onItemDelete: OnItemDelete
    var listFavorite = ArrayList<DataUserItems>()
    set(listFavorite) {
        if(listFavorite.size > 0){
            this.listFavorite.clear()
        }
        this.listFavorite.addAll(listFavorite)
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(fav: DataUserItems) {
            itemView.tv_fav.text = fav.username
            Glide.with(itemView.context)
                .load(fav.name)
                .into(itemView.img_fav)
            val btnDelete = itemView.btn_delete_fav
            btnDelete.setOnClickListener { onItemDelete.onIconClicked(fav) }
            val tvClickCallback = itemView.tv_fav
            tvClickCallback.setOnClickListener{ onItemClickCallback.onItemClicked(fav) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUserItems: DataUserItems)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback?){
        this.onItemClickCallback = onItemClickCallback!!
    }

    interface OnItemDelete{
        fun onIconClicked(dataUserItems: DataUserItems)
    }

    fun deleteSelectedItem(onItemDelete: OnItemDelete){
        this.onItemDelete = onItemDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_list, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }
}