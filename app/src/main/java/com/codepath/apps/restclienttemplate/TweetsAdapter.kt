package com.codepath.apps.restclienttemplate

import android.text.Layout
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.apps.restclienttemplate.models.timeformatter
import org.json.JSONObject
import org.w3c.dom.Text

//Turns tweets objects to something the recyclerview can displau
class TweetsAdapter(val tweets: ArrayList<Tweet>)  : RecyclerView.Adapter<TweetsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        //Inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    fun getFormattedTimeStamp(jsonObject: JSONObject) : String {
        val tweet = Tweet()
        var CreatedAt: String = ""
//            tweet.timeCreated =
        CreatedAt = jsonObject.getString("created_at")
        tweet.timeCreated = timeformatter.TimeFormatter.getTimeDifference(CreatedAt)
        return tweet.timeCreated
    }

    //Method is in charge of populating into items fthrough the viewholder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        //Get the data model based on the position
        val tweet: Tweet = tweets.get(position)

        //Set item view based on views and data model
        val radius = 30;
        val margin =  15;

        holder.tvTweetHandle.text = tweet.user?.name
        holder.tvFullTweet.text = tweet.tweetBody
//        tweet.timeCreated = getFormattedTimeStamp(JSONObject())
        holder.tvTimeStamp.setText(tweet.timeCreated)
        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).centerCrop() // scale image to fill the entire ImageView
            .transform(RoundedCorners(radius)).into(holder.ivProfileimg)
    }

//    Tells adapter how many views are in your adapter class
    override fun getItemCount(): Int {
    return tweets.size
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

//    fun getFormattedTimeStamp(jsonObject: JSONObject) : String {
//        val tweet = Tweet()
//        var CreatedAt: String = ""
////            tweet.timeCreated =
//        CreatedAt = jsonObject.getString("created_at")
//        tweet.timeCreated = timeformatter.TimeFormatter.getTimeDifference(CreatedAt)
//        return tweet.timeCreated
//    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivProfileimg = itemView.findViewById<ImageView>(R.id.ivProfileImg)
        val tvTweetHandle = itemView.findViewById<TextView>(R.id.tvTweetHandle)
        val tvFullTweet = itemView.findViewById<TextView>(R.id.tvFullTweet)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)


    }
}