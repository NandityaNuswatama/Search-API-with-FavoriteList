package com.example.consumerapp2.detail

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.consumerapp2.R
import com.example.consumerapp2.adapter.SectionPagerAdapter
import com.example.consumerapp2.data.DataUserItems
import com.example.consumerapp2.db.DatabaseContract
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
        const val EXTRA_POSITION = "position"
        const val EXTRA_LIST = "list"
        const val EXTRA_URL = "url"
        const val RESULT_ADD = 101
    }

    private var dataFav: DataUserItems? = null
    private var position: Int = 0
    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val getUsername = intent.getStringExtra(EXTRA_LIST)
        setDetail(getUsername!!)
        Timber.i(getUsername)
        val getAvatar = intent.getStringExtra(EXTRA_URL)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, getUsername)
        view_pager.adapter = sectionPagerAdapter
        tabMode.setupWithViewPager(view_pager)

        dataFav = intent.getParcelableExtra(EXTRA_DATA)
        if(dataFav != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        }else {
            dataFav = DataUserItems()
        }

        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()

        setStatusFav(false)
        fab_favorite.setOnClickListener {
            dataFav?.name = getUsername
            dataFav?.avatar = getAvatar

            val intent = Intent()
            intent.putExtra(EXTRA_DATA, dataFav)

            val values = ContentValues()
            values.put(DatabaseContract.FavColumns.USERNAME, getUsername)
            values.put(DatabaseContract.FavColumns.AVATAR_URL, getAvatar)

            val result = favHelper.insert(values)

            if(result > 0){
                dataFav?.id = result.toInt()
                if(dataFav?.username != getUsername) {
                    setResult(RESULT_ADD, intent)
                    finish()
                }
            } else{
                Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show()
            }

            setStatusFav(true)
            Timber.d(dataFav.toString())
            showSnackbarMessage("One Item added to Favorite")
        }
    }

    fun setDetail(username: String){
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "=token3b715ca01fb8551395d618c5033fbfd160cc8dee")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)

                    Timber.i(result)

                    val name = responseObject.getString("name")
                    val company = responseObject.getString("company")
                    val repos = responseObject.getString("public_repos")
                    val avatar  = responseObject.getString("avatar_url")
                    val usernamedetail = responseObject.getString("login")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")
                    Timber.i(company.toString())

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
}