package com.xiaomi.douyin_yjr

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PlayVideoActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var progressBar: ProgressBar
    private var isPlaying = false
    private lateinit var backButton: ImageView
    private lateinit var downloadButton: TextView

    private lateinit var likeCount: TextView
    private lateinit var collectionCount: TextView
    private lateinit var commentCount: TextView

    private var isLiked=false
    private var isCollectioned=false
    private var isCommented=false

    private val handler = Handler()
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updateProgress()
            handler.postDelayed(this, 1000)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val videoUriString = intent.getStringExtra("VIDEO_URI")
                val videoUri = Uri.parse(videoUriString)
                downloadVideo(videoUri)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        videoView = findViewById(R.id.videoView)
        progressBar = findViewById(R.id.processBar)
        backButton = findViewById(R.id.back)
        downloadButton = findViewById(R.id.download)
        likeCount = findViewById(R.id.likeCount)
        collectionCount = findViewById(R.id.collectionCount)
        commentCount = findViewById(R.id.commentCount)


        val videoUriString = intent.getStringExtra("VIDEO_URI")
        val videoUri = Uri.parse(videoUriString)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setVideoURI(videoUri)


        videoView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (isPlaying) {
                    videoView.pause()
                    handler.removeCallbacks(updateProgressRunnable)
                    isPlaying = false
                } else {
                    videoView.start()
                    handler.post(updateProgressRunnable)
                    isPlaying = true
                }
            }
            true
        }

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            progressBar.max = mediaPlayer.duration
            videoView.start()
            isPlaying = true
            progressBar.visibility = View.VISIBLE
            handler.post(updateProgressRunnable)
        }

        videoView.setOnErrorListener { mp, what, extra ->
            progressBar.visibility = View.GONE
            false
        }

        videoView.setOnCompletionListener {
            progressBar.visibility = View.GONE
            handler.removeCallbacks(updateProgressRunnable)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        downloadButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val videoUriString = intent.getStringExtra("VIDEO_URI")
                val videoUri = Uri.parse(videoUriString)
                downloadVideo(videoUri)
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val videoUriString = intent.getStringExtra("VIDEO_URI")
                    val videoUri = Uri.parse(videoUriString)
                    downloadVideo(videoUri)
                } else {
                    requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }

        likeCount.setOnClickListener {
            var currentCount = likeCount.text.toString().toIntOrNull() ?: 0
            if (isLiked) {
                likeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0)
                currentCount--
                isLiked = false
            } else {
                likeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0)
                currentCount++
                isLiked = true
            }
            likeCount.text = currentCount.toString()
        }

        collectionCount.setOnClickListener {
            var currentCount = collectionCount.text.toString().toIntOrNull() ?: 0
            if (isCollectioned) {
                collectionCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collection, 0, 0, 0)
                currentCount--
                isCollectioned = false
            } else {
                collectionCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collectioned, 0, 0, 0)
                currentCount++
                isCollectioned = true
            }
            likeCount.text = currentCount.toString()
        }

        commentCount.setOnClickListener {
            var currentCount = commentCount.text.toString().toIntOrNull() ?: 0
            if (isCommented) {
                currentCount--
                isCommented = false
            } else {
                currentCount++
                isCommented = true
            }
            commentCount.text = currentCount.toString()
        }


    }

    private fun updateProgress() {
        val currentPosition = videoView.currentPosition
        progressBar.progress = currentPosition
    }

    private fun downloadVideo(videoUri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(videoUri)
            val values = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, "downloaded_video.mp4")
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
            }

            val uri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                val outputStream = contentResolver.openOutputStream(it)
                inputStream?.use { input ->
                    outputStream?.use { output ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (input.read(buffer).also { length = it } > 0) {
                            output.write(buffer, 0, length)
                        }
                        Toast.makeText(this, "Video downloaded successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } ?: run {
                Toast.makeText(this, "Failed to download video", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error downloading video", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
        handler.removeCallbacks(updateProgressRunnable)
    }

}