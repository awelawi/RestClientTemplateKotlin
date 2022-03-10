package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimeLineActivity : AppCompatActivity() {
    //to call the populate method that populates tweets intothe rv with an API call
    //u need to create an instance of the TwitterClient Class
    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    val tweets = ArrayList<Tweet>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        client = TwitterApplication.getRestClient(this)

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    fun populateHomeTimeline(){
        client.getHomeTimeline(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "oncreateSuccessful")
            //remove the safe call =back as youre sure the json object wont be null
                val jsonArray = json.jsonArray

                try{
                    val listOfNewtweets = Tweet.fromJsonArray(jsonArray)

                    //add all the new tweets to list of original tweet and notify the adapter that the data set has changed
                    tweets.addAll(listOfNewtweets)
                    adapter.notifyDataSetChanged()

            } catch(e: JSONException){
                Log.e(TAG, "JSON EXCEPTION $e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                //404 error meaninb the page doesn't exist
                Log.i(TAG, "OnFailure $statusCode")
            }

        })
    }
    companion object{
        val TAG = "TImeline Activity"
    }
}