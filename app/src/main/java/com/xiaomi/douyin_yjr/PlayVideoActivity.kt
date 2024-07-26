package com.xiaomi.douyin_yjr

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.VideoView

class PlayVideoActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        val uri = Uri.parse("android.resource://$packageName/${R.raw.video}")
        videoView = findViewById(R.id.videoView)
        videoView.setVideoURI(uri)

        val play: Button = findViewById(R.id.play)
        play.setOnClickListener {
            if (!videoView.isPlaying) {
                videoView.start()
            }
            val processBar: ProgressBar = findViewById(R.id.processBar)
            if (processBar.visibility == View.VISIBLE) {
                processBar.visibility = View.GONE
            } else {
                processBar.visibility = View.VISIBLE
                processBar.progress = processBar.progress + 10
            }
        }

        val pause: Button = findViewById(R.id.pause)
        pause.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
            }
        }

        val replay: Button = findViewById(R.id.replay)
        replay.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.resume()
            }
        }


    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            1 -> {
//                if(resultCode==Activity.RESULT_OK){
//
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.suspend()
    }
}