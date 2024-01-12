package com.example.workmanager

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation.State.FAILURE
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf

import com.example.workmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        binding?.btButton?.setOnClickListener {
            startWork()
        }
    }

    private fun startWork() {
        val worker1 = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val worker2 = OneTimeWorkRequestBuilder<MyWorker2>()
            .build()

        val worker3 = OneTimeWorkRequestBuilder<MyWorker3>()
            .build()

        WorkManager
            .getInstance(applicationContext)
            .beginWith(worker1)
            .then(worker2)
            .then(worker3)
            .enqueue()

        WorkManager.getInstance(applicationContext)
            .getWorkInfoByIdLiveData(worker1.id)
            .observe(this) {
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val name = it.outputData.getString("name")
                        val surname = it.outputData.getString("surname")
                        val age = it.outputData.getInt("age", 0)
                        binding?.tvText?.text = "$name $surname $age"
                    }

                    else -> {}
                }
            }
    }

    private val data = Data.Builder()
        .putString("name", "Nurlan")
        .putString("surname", "Veliev")
        .build()

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()
}







//сделать цепочку из notification