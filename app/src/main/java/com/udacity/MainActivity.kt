package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val URL1 =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL2 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL3 =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setSupportActionBar(binding.toolbar)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private fun setViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.content.customButton.setOnClickListener(this)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url: String) {
        /*  val request =
              DownloadManager.Request(Uri.parse(URL))
                  .setTitle(getString(R.string.app_name))
                  .setDescription(getString(R.string.app_description))
                  .setRequiresCharging(false)
                  .setAllowedOverMetered(true)
                  .setAllowedOverRoaming(true)

          val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
          downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.*/
    }

    // View.OnClickListener
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.custom_button -> onDownloadBtnClick()
        }
    }

    private fun onDownloadBtnClick() {
        when (binding.content.radioGroup.checkedRadioButtonId) {
            R.id.opt1 -> download(URL1)
            R.id.opt2 -> download(URL2)
            R.id.opt3 -> download(URL3)
            else -> showPleaseSelectToast()
        }
    }

    private fun showPleaseSelectToast() {
        Toast.makeText(this, R.string.select, Toast.LENGTH_LONG).show()
    }

}
