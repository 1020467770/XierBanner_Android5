package cn.sqh.mybanner.manager

import android.content.Context
import android.util.AttributeSet

class BannerManager {
    private var mBannerOptions: BannerOptions?
    private val mAttributeManager: AttributeManager
    val bannerOptions: BannerOptions
        get() {
            if (mBannerOptions == null) {
                mBannerOptions = BannerOptions()
            }
            return mBannerOptions as BannerOptions
        }

    fun initAttrs(context: Context?, attrs: AttributeSet?) {
        mAttributeManager.init(context!!, attrs)
    }

    init {
        mBannerOptions = BannerOptions()
        mAttributeManager = AttributeManager(mBannerOptions!!)
    }
}