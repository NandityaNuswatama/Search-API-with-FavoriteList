package com.example.bfaasubmission3.detail

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bfaasubmission3.R
import com.example.bfaasubmission3.adapter.SectionPagerAdapter
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.AVATAR_URL
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.bfaasubmission3.db.FavHelper
import com.example.bfaasubmission3.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import timber.log.Timber


class DetailActivity : AppCompatActivity(){
    companion object{
        const val EXTRA_DATA = "data"
    }

    private lateinit var uri: Uri
    private var dataUserItems: DataUserItems? = null
    private val API_KEY: String = com.example.bfaasubmission3.BuildConfig.API_KEY
    private var isFavorite: Boolean = false
    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val getData = intent?.getParcelableExtra(EXTRA_DATA) as DataUserItems
        getData.username.let { setDetail(it) }

        Timber.i(getData.toString())

        favHelper = FavHelper.getInstance(this)
        favHelper.open()
        val queryByUsername = favHelper.queryByUsername(getData.username)
        Timber.i("queryByUsername: $queryByUsername")

        val sectionPagerAdapter =
            getData.username.let { SectionPagerAdapter(this, supportFragmentManager, it) }
        view_pager.adapter = sectionPagerAdapter
        tabMode.setupWithViewPager(view_pager)

        isFavorite = checkFavList(getData.username)
        setStatusFav(isFavorite)
        uri = Uri.parse("$CONTENT_URI")
        fab_favorite.setOnClickListener {
            isFavorite = !isFavorite
            setStatusFav(isFavorite)
            if (isFavorite){
                addToFavList(getData)
                showSnackbarMessage("User added to My Favorite")
            }else {
                deleteFavorite(getData.id)
                showSnackbarMessage("User removed from My Favorite")
            }
        }
    }

    private fun setDetail(username: String){
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", API_KEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val name = responseObject.getString("name")
                    val company = responseObject.getString("company")
                    val repos = responseObject.getString("public_repos")
                    val avatar  = responseObject.getString("avatar_url")
                    val usernamedetail = responseObject.getString("login")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")

                    tv_name_detail.text = name
                    tv_company_detail.text = company
                    tv_repository_detail.text = repos
                    tv_username_detail.text = usernamedetail
                    tv_follower_detail.text = followers
                    tv_following_detail.text = following
                    Glide.with(this@DetailActivity)
                        .load(avatar)
                        .into(img_avatar_detail)

                }catch (e: Exception){
                    Timber.i(e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error!!.message}"
                }
                Timber.i(errorMessage)
            }
        })
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(view_pager, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setStatusFav(statusFav: Boolean){
        if(statusFav){
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24
            )
        }
    }

    private fun addToFavList(user: DataUserItems){
        val values = ContentValues()
        values.put(USERNAME, user.username)
        values.put(AVATAR_URL, user.avatar)
        if (CONTENT_URI != null) {
            contentResolver.insert(CONTENT_URI, values)
        }
    }

    private fun checkFavList(username: String): Boolean{
        val uri = Uri.parse("$CONTENT_URI/$username")
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.count != 0){
            dataUserItems = MappingHelper.mapCursorToObject(cursor)
            Timber.i("username:${dataUserItems?.username}")
            cursor.close()
            return true
        }
        return false
    }

    private fun deleteFavorite(id: Int){
        val uri = Uri.parse("$CONTENT_URI/$id")
        contentResolver.delete(uri, null, null)
    }
}