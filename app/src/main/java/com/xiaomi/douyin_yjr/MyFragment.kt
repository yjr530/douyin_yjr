package com.xiaomi.douyin_yjr

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xiaomi.douyin_yjr.adapter.RecyclerAdapter
import com.xiaomi.douyin_yjr.data.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFragment : Fragment() {

    private val videoList = ArrayList<Video>()
    private val REQUEST_LOGIN = 1
    private val REQUEST_VIDEO_PICK = 2
    private var isLoggedIn = false
    private lateinit var txtInfo: TextView
    private lateinit var progressBar: ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        val imgAvatar = view.findViewById<ImageView>(R.id.img_avatar)
        imgAvatar.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val btnUploadVideo = view.findViewById<Button>(R.id.btn_upload_video)
        btnUploadVideo.setOnClickListener {
            if (isLoggedIn) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_VIDEO_PICK)
            } else {
                Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show()
            }
        }

        txtInfo = view.findViewById(R.id.txt_info)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = RecyclerAdapter(videoList)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOGIN -> if (resultCode == RESULT_OK) {
                isLoggedIn = true // 更新登录状态
                val username = data?.getStringExtra("username")

                val txtUsername = view?.findViewById<TextView>(R.id.txt_username)
                txtUsername?.text = username

                val imgAvatar = view?.findViewById<ImageView>(R.id.img_avatar)
                imgAvatar?.setImageResource(R.drawable.ic_user_avatar)

                val txtLoginPrompt = view?.findViewById<TextView>(R.id.txt_login_prompt)
                txtLoginPrompt?.visibility = View.GONE

                val txtInfo = view?.findViewById<TextView>(R.id.txt_info)
                txtInfo?.text = "你没有新的动态哦~"

                val btnUploadVideo = view?.findViewById<Button>(R.id.btn_upload_video)
                btnUploadVideo?.isEnabled = true // 登录后启用按钮

                // 如果登录后之前没有视频上传，更新提示信息
                updateInfoText()

            }
            REQUEST_VIDEO_PICK -> if (resultCode == RESULT_OK) {
                val videoUri = data?.data
                videoUri?.let {
                    handleVideoUri(it)
                }
            }
        }
    }

    private fun handleVideoUri(uri: Uri) {
//        Toast.makeText(requireContext(), "Selected video URI: $uri", Toast.LENGTH_SHORT).show()
        val videoThumbnail = getVideoThumbnail(requireContext(), uri)
        val video = Video(
            getRandomLengthString("天空，落日，大海......"),
            videoThumbnail,
            uri
        )
        videoList.add(video)
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter?.notifyDataSetChanged()

        // 更新提示信息
        updateInfoText()

        // 模拟上传视频的进度
        uploadVideoWithProgress(uri)
    }


    private fun updateInfoText() {
        if (videoList.isEmpty()) {
            txtInfo.text = "你没有新的动态哦~"
            txtInfo.visibility = View.VISIBLE
        } else {
            txtInfo.visibility = View.GONE
        }
    }

    private fun getVideoThumbnail(context: Context, videoUri: Uri): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        return try {
            mediaMetadataRetriever.setDataSource(context, videoUri)
            mediaMetadataRetriever.getFrameAtTime(0) // 0 表示提取视频的第一帧
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            mediaMetadataRetriever.release()
        }
    }

    private fun getRandomLengthString(str: String): String {
        val n = (1..5).random()
        val builder = StringBuilder()
        repeat(n) {
            builder.append(str)
        }
        return builder.toString()
    }

    private fun uploadVideoWithProgress(uri: Uri) {
        // 确保进度条的引用在 `onCreateView` 中正确初始化
        progressBar = requireView().findViewById(R.id.progressBar)

        // 设置进度条的最大值
        progressBar.max = 100
        progressBar.progress = 0
        progressBar.visibility = View.VISIBLE

        // 模拟上传过程
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                for (progress in 1..100 step 10) {
                    delay(200) // 模拟上传延迟
                    // 更新进度
                    withContext(Dispatchers.Main) {
                        progressBar.progress = progress
                    }
                }
            }
            // 上传完成
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "视频上传成功", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun uploadVideoWithProgress(uri: Uri) {
//        // 显示进度条
//        progressBar.visibility = View.VISIBLE
//
//        // 模拟上传过程
//        CoroutineScope(Dispatchers.Main).launch {
//            withContext(Dispatchers.IO) {
//                for (progress in 1..100 step 10) {
//                    delay(500) // 模拟上传延迟
//                    // 这里可以更新上传进度
//                }
//            }
//            progressBar.visibility = View.GONE
//            Toast.makeText(requireContext(), "视频上传成功", Toast.LENGTH_SHORT).show()
//        }
//    }
}