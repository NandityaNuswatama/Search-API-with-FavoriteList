package com.example.bfaasubmission3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaasubmission3.adapter.ListActivityAdapter
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.favorite.FavoriteActivity
import com.example.bfaasubmission3.settings.SettingActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListActivityAdapter
    val listItems = ArrayList<DataUserItems>()
    private val API_KEY: String = BuildConfig.API_KEY.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ListActivityAdapter(this)
        adapter.notifyDataSetChanged()

        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = adapter

        searchUser()

//        adapter.setOnItemClickCallback(object : ListActivityAdapter.OnItemClickCallback{
//            override fun onItemClicked(dataUserItems: DataUserItems) {
//                toDetailUser(dataUserItems)
//                Timber.i("user selected")
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_fav_list -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchUser(): Boolean{
        val searchView = findViewById<SearchView>(R.id.search)
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                getGithubUser(query)
                imageView.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity, "Searching for $query", Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    fun getGithubUser(username: String){
        showLoading(true)
        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", API_KEY)
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
                    val list = responseObject.getJSONArray("items")

                    Timber.i(url)
                    listItems.clear()
                    for (i in 0 until list.length()){
                        val user = list.getJSONObject(i)
                        val userData = DataUserItems()

                        userData.name = user.getString("login")
                        userData.company = user.getString("organizations_url")
                        userData.repository = user.getString("repos_url")
                        userData.avatar = user.getString("avatar_url")
                        userData.username = user.getString("login")
                        userData.followers = user.getString("followers_url")
                        userData.following = user.getString("following_url")
                        listItems.add(userData)
                        Timber.i(userData.name)
                    }
                    adapter.setData(listItems)
                    showLoading(false)
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
                showLoading(false)
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

    private fun showLoading(state: Boolean){
        if(state){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.INVISIBLE
        }
    }

//    private fun toDetailUser(dataUserItems: DataUserItems){
//        val toDetail = Intent(this, DetailActivity::class.java)
//        toDetail.putExtra(DetailActivity.EXTRA_LIST, dataUserItems.username)
//        toDetail.putExtra(EXTRA_DATA, dataUserItems)
//        startActivity(toDetail)
//    }
}