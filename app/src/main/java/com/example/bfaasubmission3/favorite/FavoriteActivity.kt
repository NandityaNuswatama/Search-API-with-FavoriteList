package com.example.bfaasubmission3.favorite

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.adapter.FavoriteAdapter
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
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

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "My Favorite"

        adapter = FavoriteAdapter(this)
        rv_favorite.adapter = adapter
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        val handlerThread = HandlerThread("favoriteThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val mObserver = object : ContentObserver(handler) {
            override fun onChange(change: Boolean) {
                loadFavAsync()
            }
        }
        if (CONTENT_URI != null) {
            contentResolver.registerContentObserver(CONTENT_URI, true, mObserver)
        }

        if(savedInstanceState == null){
            loadFavAsync()
            adapter.notifyDataSetChanged()
        }else{
            val list = savedInstanceState.getParcelableArrayList<DataUserItems>(EXTRA_NAME)
            if(list != null){
                adapter.listFavorite = list
            }
        }
    }


    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun loadFavAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            val db = async(Dispatchers.IO) {
                val cursor = CONTENT_URI?.let { contentResolver?.query(it, null, null, null, null) }
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = db.await()
            if (users.size > 0) {
                adapter.listFavorite = users
            }else{
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("No Favorite was added yet")
            }
            Timber.i("Favorite: $users")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_NAME, adapter.getFav())
    }
}