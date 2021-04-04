package cn.sqh.XierBanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_picture_detail.*

class PictureDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_detail)
        val srcId = intent.getIntExtra("srcId", 0)
        image.setImageResource(srcId)
    }
}