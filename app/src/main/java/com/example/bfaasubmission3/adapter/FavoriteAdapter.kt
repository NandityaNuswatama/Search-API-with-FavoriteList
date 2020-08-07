package com.example.bfaasubmission3.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.bfaasubmission3.detail.DetailActivity
import com.example.bfaasubmission3.detail.DetailActivity.Companion.EXTRA_DATA
import kotlinx.android.synthetic.main.favorite_list.view.*

class FavoriteAdapter(val context: Context): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<DataUserItems>()
        set(listFavorite) {
            if(listFavorite.size > 0){
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    fun getFav() = listFavorite

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(fav: DataUserItems, position: Int) {
            itemView.tv_fav.text = fav.username
            Glide.with(itemView.context)
                .load(fav.avatar)
                .into(itemView.img_fav)

            itemView.btn_delete_fav.setOnClickListener {
                deleteFavUser(fav.id)
            notifyItemRemoved(position)}

            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, fav)
                context.startActivity(intent)
            }
        }

        private fun deleteFavUser(id: Int){
            val uri = Uri.parse("$CONTENT_URI/$id")
            context.contentResolver.delete(uri, null, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_list, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position], position)
    }
}