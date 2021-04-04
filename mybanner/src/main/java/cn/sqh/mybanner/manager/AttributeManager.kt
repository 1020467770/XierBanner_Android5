package cn.sqh.mybanner.manager

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import cn.sqh.mybanner.R

class AttributeManager(private val mBannerOptions: BannerOptions) {
    fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager)
            initBannerAttrs(typedArray)
            typedArray.recycle()
        }
    }

    private fun initBannerAttrs(typedArray: TypedArray) {
        val interval = typedArray.getInteger(R.styleable.BannerViewPager_bvp_interval, 3000)
        val isAutoPlay = typedArray.getBoolean(R.styleable.BannerViewPager_bvp_auto_play, true)
        val isCanLoop = typedArray.getBoolean(R.styleable.BannerViewPager_bvp_can_loop, true)
        mBannerOptions.interval = interval
        mBannerOptions.isAutoPlay = isAutoPlay
        mBannerOptions.isCanLoop = isCanLoop
    }
}