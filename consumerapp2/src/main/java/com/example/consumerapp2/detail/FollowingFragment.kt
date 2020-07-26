package com.example.consumerapp2.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp2.R
import com.example.consumerapp2.adapter.FollowAdapter
import com.example.consumerapp2.data.DataFollow
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import timber.log.Timber
import java.lang.Exception

class FollowingFragment : Fragment() {
    private lateinit var adapter: FollowAdapter
    val listItems = ArrayList<DataFollow>()

    companion object{
        var EXTRA_NAME = "name"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showRecycler()
    }

    private fun showRecycler(){
        adapter = FollowAdapter()
        adapter.notifyDataSetChanged()

        rv_user_following.setHasFixedSize(true)
        rv_user_following.layoutManager = LinearLayoutManager(context)
        rv_user_following.adapter = adapter

        if (arguments != null) {
            val dataName = arguments?.getString(EXTRA_NAME)
            setGithubFollowing(dataName.toString())
        }
        getFollowing()
    }

    private fun setGithubFollowing(username: String){
        val url = "https://api.github.com/users/$username/following"
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
                    showLoading(true)
                    val result = String(responseBody!!)
                    val responseObject = JSONArray(result)
                    Timber.i(url)

                    for (i in 0 until responseObject.length()){
                        val user = responseObject.getJSONObject(i)
                        val userData = DataFollow()
                        userData.name = user.getString("login")
                        userData.avatar = user.getString("avatar_url")
                        listItems.add(userData)
                        Timber.i(userData.name)
                    }
                    adapter.setData(listItems)
                    showLoading(false)
                    Timber.tag("adapter").i(listItems.toString())
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

    private fun getFollowing(): ArrayList<DataFollow>{
        return listItems
    }

    private fun showLoading(state: Boolean){
        if(state){
            progressBar2.visibility = View.VISIBLE
        }else{
            progressBar2.visibility = View.INVISIBLE
        }
    }
}