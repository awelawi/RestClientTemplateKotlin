package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    lateinit var swipeContainer: SwipeRefreshLayout


    val tweets = ArrayList<Tweet>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        client = TwitterApplication.getRestClient(this)

        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "REFERESHING TIMELINE")
            populateHomeTimeline()
        }

        //Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

//    Handles clicks on menu time
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.compose) {
//            Toast.makeText(this, "The compose icon has been pressed", Toast.LENGTH_SHORT)
//                .show()

            //Navigate to a new screen using intent
            val intent = Intent(this, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    return super.onOptionsItemSelected(item)
    }

    //This method is called when we come back from ComposeActivity

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //REQUEST_CODE IS DEFINED BELOW
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //Extract name value from extra results
                //Getting data from the intent which is the tweet
            val tweet = data?.getParcelableExtra<Tweet>("tweet") as Tweet

            //Update timeline
             tweets.add(0, tweet)

            //Update adapter
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun populateHomeTimeline(){
        client.getHomeTimeline(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "oncreateSuccessful")
            //remove the safe call =back as youre sure the json object wont be null
                val jsonArray = json.jsonArray

                try{
                    //clears out the currently fetched tweets, to obtain the most recent tweet when refereshed
                    adapter.clear()
                    val listOfNewtweets = Tweet.fromJsonArray(jsonArray)

                    //add all the new tweets to list of original tweet and notify the adapter that the data set has changed
                    tweets.addAll(listOfNewtweets)
                    adapter.notifyDataSetChanged()
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false)

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
        val REQUEST_CODE = 25
    }
}