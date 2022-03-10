package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject

class Tweet {
    //Display for the tweet
    var tweetBody: String = ""
    var timeCreated: String = ""
    var user: User? = null

    companion object{
        //Create a new tweet from the one specific jsonObject to a tweet object
        fun fromJson(jsonObject: JSONObject) : Tweet{
            val tweet =  Tweet()
            tweet.tweetBody = jsonObject.getString("text")
            tweet.timeCreated = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))

            return tweet
        }

        //Takes a whole jsonArray and converts it to a list of tweets
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet>{
            val tweets = ArrayList<Tweet>()
            for (i in 0..jsonArray.length()){
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
    }
}