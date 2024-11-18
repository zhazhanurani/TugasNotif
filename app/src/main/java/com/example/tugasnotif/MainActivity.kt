package com.example.tugasnotif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
//import android.os.Bundleg
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugasnotif.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val channelId = "TEST_NOTIF"
    private val notifId = 100


    companion object{
        var likeCount: Int = 0
        var dislikeCount: Int = 0
        const val ACTION_UPDATE_COUNTER = "com.example.tugasnotif.UPDATE_COUNTER"
    }

    private val counterReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Update the UI whenever counter changes
            binding.mainLike.text = likeCount.toString()
            binding.mainDislike.text = dislikeCount.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the UI
        binding.mainLike.text = likeCount.toString()
        binding.mainDislike.text = dislikeCount.toString()

        // Register the receiver to update counters
        registerReceiver(counterReceiver, IntentFilter(ACTION_UPDATE_COUNTER))


        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        binding.btnVote.setOnClickListener {

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            }
            else {
                0
            }

            // Intent untuk Like
            val likeIntent = Intent(this, NotifReceiver::class.java).apply {
                action = "ACTION_LIKE"
            }
            val likePendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                likeIntent,
                flag
            )

            // Intent untuk Dislike
            val dislikeIntent = Intent(this, NotifReceiver::class.java).apply {
                action = "ACTION_DISLIKE"
            }
            val dislikePendingIntent = PendingIntent.getBroadcast(
                this,
                1,
                dislikeIntent,
                flag
            )

            val notifImage = BitmapFactory.decodeResource(resources,
                R.drawable.img)

            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_leon)
                .setContentTitle("Counter")
                .setContentText("Vote! am I Gamteng?") // Isi pesan bebas
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(notifImage)
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.like, "Like", likePendingIntent) // Aksi pertama
                .addAction(R.drawable.dislike, "Dislike", dislikePendingIntent) // Aksi kedua
            notifManager.notify(notifId, builder.build())
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(counterReceiver) // Unregister receiver to avoid leaks
    }
}