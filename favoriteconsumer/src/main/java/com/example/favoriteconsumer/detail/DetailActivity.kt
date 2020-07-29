package com.example.favoriteconsumer.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.favoriteconsumer.R
import com.example.favoriteconsumer.adapter.SectionPagerAdapter
import com.example.favoriteconsumer.data.DataUserItems
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

    private val API_KEY: String = com.example.favoriteconsumer.BuildConfig.API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val getData = intent?.getParcelableExtra(EXTRA_DATA) as DataUserItems
        getData.username?.let { setDetail(it) }

        Timber.i(getData.toString())

        val sectionPagerAdapter = getData.username?.let { SectionPagerAdapter(this, supportFragmentManager, it) }
        view_pager.adapter = sectionPagerAdapter
        tabMode.setupWithViewPager(view_pager)

        setStatusFav()

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

    private fun setStatusFav(){
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
    }
}