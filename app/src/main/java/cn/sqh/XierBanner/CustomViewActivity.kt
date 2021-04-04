package cn.sqh.XierBanner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.sqh.myloginview.MyLoginView
import kotlinx.android.synthetic.main.activity_custom_view.*

class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        val loginView = loginView
        loginView.setCornerRadius(20f, 20f, 20f, 20f)
        loginView.setOnLoginClickListener(object : MyLoginView.OnLoginClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@CustomViewActivity, "您点击了登录按钮", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CustomViewActivity::class.java))
        }
    }
}