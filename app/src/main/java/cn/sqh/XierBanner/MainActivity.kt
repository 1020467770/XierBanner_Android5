package cn.sqh.XierBanner

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import cn.sqh.mybanner.BannerPager
import cn.sqh.mybanner.utils.BannerUtils

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DataActivity() {

    private lateinit var mPager: BannerPager<CustomBean>

    private val des = arrayOf("星星\n真好看哪！", "水母\n真好看哪！", "地球\n真好看哪！")

    private val data: List<CustomBean>
        get() {
            val list = ArrayList<CustomBean>()
            for (i in mDrawableList.indices) {
                val customBean = CustomBean()
                customBean.imageRes = mDrawableList[i]
                customBean.imageDescription = des[i]
                list.add(customBean)
            }
            return list
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewPager()
        updateUI(0)
    }

    private val TAG = "MainActivity"

    private fun setupViewPager() {
        mPager = findViewById(R.id.viewpager)
        mPager.apply {
            setCanLoop(true)
            setOnPageClickListener(object: BannerPager.OnPageClickListener {
                override fun onPageClick(clickedView: View, position: Int) {
                    Log.d(TAG, "setupViewPager: ${position}")
                    Log.d(TAG, "setupViewPager: ${data?.get(position)?.imageRes}")
                    val intent: Intent = Intent(this@MainActivity, PictureDetailActivity::class.java)
                    data?.get(position)?.let { intent.putExtra("srcId", it.imageRes) }
                    startActivity(intent)
                }
            })
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    BannerUtils.log("position:$position")
                    updateUI(position)
                }
            })
            adapter = SimpleAdapter().apply {
                mOnSubViewClickListener = object : SimpleAdapter.OnSubViewClickListener {
                    override fun onViewClick(view: View?, position: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "Logo Clicked,position:$position",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }.create(data)
    }

    fun onClick(view: View) {
        CustomViewActivity.start(this@MainActivity)
//        finish()
    }

    private fun updateUI(position: Int) {
        tv_describe?.text = des[position]
        val translationAnim = ObjectAnimator.ofFloat(tv_describe, "translationX", -120f, 0f)
        translationAnim.apply {
            duration = 1300L
            interpolator = DecelerateInterpolator()
        }
        val alphaAnimator = ObjectAnimator.ofFloat(tv_describe, "alpha", 0f, 1f)
        alphaAnimator.apply {
            duration = 1300L
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationAnim, alphaAnimator)
        animatorSet.start()

        if (position == mPager.data!!.size - 1 && btn_start?.visibility == View.GONE) {
            btn_start?.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(btn_start, "alpha", 0f, 1f)
                .setDuration(1300L).start()
        } else {
            btn_start?.visibility = View.GONE
        }

    }
}