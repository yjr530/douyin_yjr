package com.xiaomi.douyin_yjr.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.xiaomi.douyin_yjr.PlayVideoActivity
import com.xiaomi.douyin_yjr.R
import com.xiaomi.douyin_yjr.data.Video

class RecyclerAdapter(val videoList: List<Video>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoTitle: TextView = view.findViewById(R.id.videoTitle)
        val videoImage: ImageView = view.findViewById(R.id.videoImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val video = videoList[position]
            Toast.makeText(
                parent.context, "you clicked view ${video.title}",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewHolder.videoImage.setOnClickListener {
            val position = viewHolder.adapterPosition
            val video = videoList[position]
            Toast.makeText(
                parent.context, "you clicked view ${video.title}",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(parent.context, PlayVideoActivity::class.java)
            parent.context.startActivity(intent)
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val fruit = videoList[position]
        holder.videoImage.setImageResource(fruit.videoId)
        holder.videoTitle.text = fruit.title
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}