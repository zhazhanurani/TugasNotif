package com.example.tugasnotif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.tugasnotif.MainActivity.Companion.dislikeCount
import com.example.tugasnotif.MainActivity.Companion.likeCount


class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        when (action) {
            "ACTION_LIKE" -> {
                likeCount++
                Log.d("Check Value Bang", "Like: $likeCount, Dislike: $dislikeCount")
                Toast.makeText(context, "Liked! Count: ${likeCount}", Toast.LENGTH_SHORT).show()
            }
            "ACTION_DISLIKE" -> {
                dislikeCount++
                Log.d("Check Value Bang", "Like: $likeCount, Dislike: $dislikeCount")
                Toast.makeText(context, "Disliked! Count: ${dislikeCount}", Toast.LENGTH_SHORT).show()
            }
        }
        // Notify MainActivity to update its UI
        val updateIntent = Intent(MainActivity.ACTION_UPDATE_COUNTER)
        context?.sendBroadcast(updateIntent)
        MainActivity
    }
}
