package cn.sqh.mybanner.utils

import android.util.Log
import cn.sqh.mybanner.BaseBannerAdapter

object BannerUtils {
    var isDebugMode = false
    private const val TAG = "SQH"
    fun log(tag: String?, msg: String?) {
        if (isDebugMode) {
            Log.e(tag, msg!!)
        }
    }

    fun log(msg: String?) {
        if (isDebugMode) {
            log(TAG, msg)
        }
    }

    @JvmStatic
    fun getRealPosition(position: Int, pageSize: Int): Int {
        return if (pageSize == 0) {
            0
        } else (position + pageSize) % pageSize
    }

    @JvmStatic
    fun getOriginalPosition(pageSize: Int): Int {
        return BaseBannerAdapter.MAX_VALUE / 2 - BaseBannerAdapter.MAX_VALUE / 2 % pageSize
    }
}