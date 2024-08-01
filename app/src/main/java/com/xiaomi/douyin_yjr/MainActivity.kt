package com.xiaomi.douyin_yjr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xiaomi.douyin_yjr.data.UserDao

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val myFragment = MyFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userDao = UserDao(this)

        val defaultUser = userDao.getUser("admin")
        if (defaultUser == null) {
            userDao.addUser("admin", "123456")
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, homeFragment)
            commit()
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, homeFragment)
                        commit()
                    }
                    true
                }

                R.id.navigation_my -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, myFragment)
                        commit()
                    }
                    true
                }
                else->false
            }

        }
    }
}