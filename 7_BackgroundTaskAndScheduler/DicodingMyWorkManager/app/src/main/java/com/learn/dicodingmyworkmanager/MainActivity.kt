package com.learn.dicodingmyworkmanager

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import android.Manifest.permission.POST_NOTIFICATIONS
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import com.learn.dicodingmyworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var workManager: WorkManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(POST_NOTIFICATIONS)
        }

        workManager = WorkManager.getInstance(this)
        binding.btnOneTimeTask.setOnClickListener(this)

        // Periodic Work Request
        binding.btnPeriodicTask.setOnClickListener(this)
        binding.btnCancelTask.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnOneTimeTask -> startOneTimeTask()
            R.id.btnPeriodicTask -> startPeriodicTask()
            R.id.btnCancelTask -> cancelPeriodicTask()
        }
    }

    private fun startOneTimeTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .build()
        workManager.enqueue(oneTimeWorkRequest)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this@MainActivity) { workInfo ->
                val status = workInfo.state.name
                binding.textStatus.append("\n" + status)
            }
    }

    private fun startPeriodicTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity) { workInfo ->
                val status = workInfo.state.name
                binding.textStatus.append("\n" + status)
                binding.btnCancelTask.isEnabled = false
                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    binding.btnCancelTask.isEnabled = true
                }
            }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }
}