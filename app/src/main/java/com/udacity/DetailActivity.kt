package com.udacity

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.utils.Notifications
import com.udacity.utils.Urls

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private var urlNum = 0
    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setSupportActionBar(binding.toolbar)
        Notifications.cancelNotification(this)
        binding.content.btnOk.setOnClickListener(this)
        getExtras()
        setFileName()
        setStatus()
    }

    private fun setViewBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getExtras() {
        urlNum = intent.getIntExtra(Notifications.EXTRA_URL, 0)
        status = intent.getIntExtra(Notifications.EXTRA_STATUS, 0)
    }

    private fun setFileName() {
        val title = when (Urls.getByOrdinal(urlNum)) {
            Urls.EMPTY -> getString(R.string.error)
            Urls.URL1 -> getString(R.string.notification_title1)
            Urls.URL2 -> getString(R.string.notification_title2)
            Urls.URL3 -> getString(R.string.notification_title3)
        }
        binding.content.tvFileNameValue.text = title
    }

    private fun setStatus() {
        val statusString = when (status) {
            DownloadManager.STATUS_SUCCESSFUL -> getString(R.string.success)
            else -> getString(R.string.failed)
        }
        binding.content.tvStatusValue.text = statusString
    }

    // View.OnClickListener

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnOk -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
