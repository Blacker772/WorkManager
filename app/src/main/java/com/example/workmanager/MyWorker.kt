package com.example.workmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MyWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        delay(2000)
        sendNotification()
        delay(1000)
        sendData()
        delay(3000)
        return Result.success(sendData())
    }

    private fun sendData(): Data{
        val name = inputData.getString("name")
        val surname = inputData.getString("surname")
        return Data.Builder()
            .putString("name", name)
            .putString("surname", surname)
            .putInt("age", 24)
            .build()
    }


    @SuppressLint("MissingPermission")
    fun sendNotification() {

        val name = inputData.getString("name")
        val surname = inputData.getString("surname")

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(applicationContext, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.music_svgrepo_com)
            .setContentTitle("$name $surname")
            .setContentText("$name $surname 24")
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, builder.build())
        }
    }

    companion object {
        const val MY_CHANNEL_ID = "My_Channel"
        const val MY_CHANNEL_NAME = "Test"
        const val ID = 10
    }
}


