package com.xiaomi.douyin_yjr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xiaomi.douyin_yjr.adapter.RecyclerAdapter
import com.xiaomi.douyin_yjr.data.Video

class HomeFragment : Fragment() {
    private val videoList = ArrayList<Video>()

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
        initVideos()
        val layoutManager = LinearLayoutManager(context)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = RecyclerAdapter(videoList)
        recyclerView.adapter = adapter
    }

    private fun initVideos() {
        videoList.add(Video("忽而今夏主题曲MV", R.drawable.ic_launcher_background))
        repeat(10) {

            videoList.add(Video("Video*****", R.drawable.ic_launcher_background))

        }
    }

}