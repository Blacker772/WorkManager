package com.example.workmanager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class MyWorker3(
    context: Context,
    workerParameters: WorkerParameters)
    :CoroutineWorker(context,workerParameters) {
    override suspend fun doWork(): Result {
        delay(2000)
        sendNotification()
        delay(3000)
        return Result.success()
    }

    private fun sendNotification(){
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

        val builder = NotificationCompat.Builder(applicationContext, MyWorker.MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.music_svgrepo_com)
            .setContentTitle("It's Worker number 3")
            .setContentText("Worked!")
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, builder.build())
        }
    }
}