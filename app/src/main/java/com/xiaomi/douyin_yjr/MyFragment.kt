package com.xiaomi.douyin_yjr

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class MyFragment : Fragment() {
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
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                val username = data?.getStringExtra("username")

                val txtUsername = view?.findViewById<TextView>(R.id.txt_username)
                txtUsername?.text = username

                val imgAvatar = view?.findViewById<ImageView>(R.id.img_avatar)
                imgAvatar?.setImageResource(R.drawable.ic_user_avatar)

                val txtLoginPrompt = view?.findViewById<TextView>(R.id.txt_login_prompt)
                txtLoginPrompt?.visibility = View.GONE

                val txtInfo = view?.findViewById<TextView>(R.id.txt_info)
                txtInfo?.text = "你没有新的动态哦~"




            }
        }
    }
}