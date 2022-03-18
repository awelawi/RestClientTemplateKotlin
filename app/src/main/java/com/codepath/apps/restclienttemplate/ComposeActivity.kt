package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.AddTweet)
        btnTweet = findViewById(R.id.submit_button)

        client = TwitterApplication.getRestClient(this)



        //Handling the user's click on the tweet button
        btnTweet.setOnClickListener{
            //Take the content in the editText space
            val tweetContent = etCompose.text.toString()

            //1 Make sure the tweet isn't empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this, "Empty tweets not allowed", Toast.LENGTH_SHORT)
                    .show()

                //Look into snackbar message that's more visually appealing
            }
            //2 Make sure the tweet is under the character count.
            if(tweetContent.length > 148){
                Toast.makeText(this, "Tweet is too long! Limit is 148 characters", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
//                Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                //Make an api call to publish and display that content as a tweet.
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler()
                {
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully published tweet")

                        //Send the tweet back to timelineactivity
                        val tweet = Tweet.fromJson(json.jsonObject)


                        //pass back to timeline activity
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }


                })
            }
        }
    }
    companion object{
        val TAG = "PublishTweet"
    }
}