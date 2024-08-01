package com.xiaomi.douyin_yjr

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xiaomi.douyin_yjr.adapter.RecyclerAdapter
import com.xiaomi.douyin_yjr.data.Video
import com.xiaomi.douyin_yjr.data.VideoDao

class HomeFragment : Fragment() {
    private val videoList = ArrayList<Video>()
    private lateinit var videoDao: VideoDao

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoDao = VideoDao(requireContext())
        initVideos()
        loadVideosFromDatabase()

        val layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = RecyclerAdapter(videoList)
        recyclerView.adapter = adapter
    }

    private fun getRandomLengthString(str: String): String {
        val n = (1..5).random()
        val builder = StringBuilder()
        repeat(n) {
            builder.append(str)
        }
        return builder.toString()
    }

//    private fun initVideos() {
//        val videoUris = listOf(
//            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video1"),
//            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video2"),
//            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video3"),
//            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video4"),
//            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video5"),
//        )
//
//        repeat(10) {
//            videoList.add(
//                Video(
//                    getRandomLengthString("在日薄桑榆的海边，看黄昏坠落人间的一刻。"),
//                    getVideoThumbnail(requireContext(), videoUris[0]),
//                    videoUris[0]
//                )
//            )
//            videoList.add(
//                Video(
//                    getRandomLengthString("那片海的浪不会停......"),
//                    getVideoThumbnail(requireContext(), videoUris[1]),
//                    videoUris[1]
//                )
//            )
//            videoList.add(
//                Video(
//                    getRandomLengthString("喜欢海，喜欢日落。"),
//                    getVideoThumbnail(requireContext(), videoUris[2]),
//                    videoUris[2]
//                )
//            )
//            videoList.add(
//                Video(
//                    getRandomLengthString("落日尤其浪漫！"),
//                    getVideoThumbnail(requireContext(), videoUris[3]),
//                    videoUris[3]
//                )
//            )
//            videoList.add(
//                Video(
//                    getRandomLengthString("海的那边是什么？"),
//                    getVideoThumbnail(requireContext(), videoUris[4]),
//                    videoUris[4]
//                )
//            )
//        }
//    }

    private fun initVideos() {

        val videoUris = listOf(
            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video1"),
            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video2"),
            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video4"),
            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video3"),
            Uri.parse("android.resource://com.xiaomi.douyin_yjr/raw/video5"),
        )

        val titles = listOf(
            getRandomLengthString("在日薄桑榆的海边，看黄昏坠落人间的一刻。"),
            getRandomLengthString("那片海的浪不会停......"),
            getRandomLengthString("喜欢海，喜欢日落。"),
            getRandomLengthString("落日尤其浪漫！"),
            getRandomLengthString("海的那边是什么？")
        )

        // 清除旧数据
        videoDao.clearVideos()

        // 插入视频数据到数据库两遍
        repeat(5) { // 重复两次
            videoUris.forEachIndexed { index, uri ->
                val title = titles.getOrElse(index % titles.size) { "默认标题" } // 确保标题不会越界
                val description = "视频描述 $index"
                val filepath = uri.toString()

                // 插入视频元数据到数据库
                videoDao.addVideo(title, description, filepath)
            }
        }
    }

    private fun loadVideosFromDatabase() {
        videoList.clear()
        videoDao.getVideos().forEach { video ->
            val thumbnail = getVideoThumbnail(requireContext(), video.uri)
            videoList.add(Video(video.title, thumbnail, video.uri))
        }
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter?.notifyDataSetChanged()
    }

    private fun getVideoThumbnail(context: Context, videoUri: Uri): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        return try {
            mediaMetadataRetriever.setDataSource(context, videoUri)
            mediaMetadataRetriever.getFrameAtTime(10) // 0 表示提取视频的第一帧
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            mediaMetadataRetriever.release()
        }
    }
}