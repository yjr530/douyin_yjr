package com.xiaomi.douyin_yjr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xiaomi.douyin_yjr.data.UserDao
import com.xiaomi.douyin_yjr.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDao(this)

        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        if (isRemember) {
            val account = prefs.getString("account", "")
            val password = prefs.getString("password", "")
            binding.accountEdit.setText(account)
            binding.passwordEdit.setText(password)
            binding.rememberPass.isChecked = true
        }

        binding.login.setOnClickListener {
            val account = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
//            if (account == "admin" && password == "123456") {
//                val editor = prefs.edit()
//                if (binding.rememberPass.isChecked) {
//                    editor.putBoolean("remember_password", true)
//                    editor.putString("account", account)
//                    editor.putString("password", password)
//                } else {
//                    editor.clear()
//                }
//                editor.apply()
//
//                val intent = Intent()
//                intent.putExtra("username", "大王叫我来巡山")
//                intent.putExtra("avator", R.drawable.ic_launcher_foreground)
//                setResult(RESULT_OK, intent)
//                finish()
//            } else {
//                Toast.makeText(
//                    this, "account or password is invalid",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userDao.getUser(account) == password) {
                val editor = prefs.edit()
                if (binding.rememberPass.isChecked) {
                    editor.putBoolean("remember_password", true)
                    editor.putString("account", account)
                    editor.putString("password", password)
                } else {
                    editor.clear()
                }
                editor.apply()

                val intent = Intent()
                intent.putExtra("username", "大王叫我来巡山")
                intent.putExtra("avator", R.drawable.ic_launcher_foreground)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this, "账号或密码无效",
                    Toast.LENGTH_SHORT
                ).show()
                binding.accountEdit.setText("")
                binding.passwordEdit.setText("")
            }
        }
        binding.txtRegister.setOnClickListener {
            val account = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "账号和密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userDao.getUser(account) != null) {
                Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userDao.addUser(account, password)
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
        }
        binding.txtBack.setOnClickListener {
            onBackPressed()
        }
    }
}