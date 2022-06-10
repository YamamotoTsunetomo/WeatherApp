package com.example.weather.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.presentation.services.notifications.Notification
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRepeatingNotification()
    }


    private fun setRepeatingNotification() {
        val notifyIntent = Intent(this, Notification::class.java)
        notifyIntent.putExtra("weatherID", INTENT_REQUEST_CODE)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                INTENT_REQUEST_CODE,
                notifyIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            NINE_AM_IN_MILLISECONDS,
            TWELVE_HOURS_IN_MILLISECONDS,
            pendingIntent
        )
    }

    companion object {
        private const val INTENT_REQUEST_CODE = 1
        private const val TWELVE_HOURS_IN_MILLISECONDS = 1000 * 3600 * 12L

        private val NINE_AM_IN_MILLISECONDS by lazy {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, 14)
            calendar.set(Calendar.MINUTE, 31)
            calendar.set(Calendar.SECOND, 1)

            calendar.timeInMillis
        }
    }
}