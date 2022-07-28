package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.Downloader
import com.udacity.utils.Notifications
import com.udacity.utils.Urls

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setSupportActionBar(binding.toolbar)
        setupListeners()
        Notifications.createChannel(this)
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
            if (id == Downloader.downloadID && context != null) {
                Notifications.showNotification(context, Downloader.downloadUrl)
            }
        }
    }

    // View.OnClickListener
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.custom_button -> onDownloadBtnClick()
        }
    }

    private fun onDownloadBtnClick() {
        when (binding.content.radioGroup.checkedRadioButtonId) {
            R.id.opt1 -> Downloader.download(this, Urls.URL1)
            R.id.opt2 -> Downloader.download(this, Urls.URL2)
            R.id.opt3 -> Downloader.download(this, Urls.URL3)
            else -> showPleaseSelectToast()
        }
    }

    private fun showPleaseSelectToast() {
        Toast.makeText(this, R.string.select, Toast.LENGTH_LONG).show()
    }

}
