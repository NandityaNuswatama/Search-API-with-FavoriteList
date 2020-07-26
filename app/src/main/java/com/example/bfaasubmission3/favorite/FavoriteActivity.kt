package com.example.bfaasubmission3.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.adapter.FavoriteAdapter
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.detail.DetailActivity
import com.example.bfaasubmission3.db.FavHelper
import com.example.bfaasubmission3.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var favHelper: FavHelper
    private var position: Int = 0

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val RESULT_DELETE = 201
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "My Favorite"

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter()
        rv_favorite.adapter = adapter

        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()

        if(savedInstanceState == null){
            loadFavAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<DataUserItems>(EXTRA_NAME)
            if(list != null){
                adapter.listFavorite = list
            }
        }

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(dataUserItems: DataUserItems) {
                toDetailUser(dataUserItems)
                showSnackbarMessage("User Clicked")
            }
        })

        adapter.deleteSelectedItem(object : FavoriteAdapter.OnItemDelete{
            override fun onIconClicked(dataUserItems: DataUserItems) {
                deleteFav(dataUserItems)
                showSnackbarMessage("Delete selected user")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadFavAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun loadFavAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            progresBar_fav.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO){
                val cursor = favHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progresBar_fav.visibility = View.INVISIBLE
            val favorites = deferredNotes.await()
            if(favorites.size > 0){
                adapter.listFavorite = favorites
            }else{
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("No Favorite was added yet")
            }
        }
    }

    private fun toDetailUser(dataUserItems: DataUserItems){
        val toDetail = Intent(this, DetailActivity::class.java)
        toDetail.putExtra(DetailActivity.EXTRA_LIST, dataUserItems.username)
        startActivity(toDetail)
    }

    private fun deleteFav(dataUserItems: DataUserItems){
        val result = favHelper.deleteById(dataUserItems.id.toString()).toLong()
        if (result > 0) {
            val intent = Intent()
            intent.putExtra(EXTRA_POSITION, position)
            setResult(RESULT_DELETE, intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to remove user", Toast.LENGTH_SHORT).show()
        }
    }
}