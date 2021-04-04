package cn.sqh.mybanner

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import cn.sqh.mybanner.manager.BannerManager
import cn.sqh.mybanner.utils.BannerUtils.getOriginalPosition
import cn.sqh.mybanner.utils.BannerUtils.getRealPosition
import java.util.*

class BannerPager<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), LifecycleObserver {

    var adapter: BaseBannerAdapter<T>? = null

    private var currentPosition = 0
    private var isLooping = false
    private lateinit var mOnPageClickListener: OnPageClickListener
    private lateinit var mViewPager: ViewPager2
    private lateinit var mBannerManager: BannerManager
    private val mHandler = Handler()
    private var onPageChangeCallback: OnPageChangeCallback? = null
    private val mRunnable = Runnable { handlePosition() }
    private var startX = 0
    private var startY = 0
    private val mOnPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            pageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            pageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            pageScrollStateChanged(state)
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mBannerManager = BannerManager()
        mBannerManager.initAttrs(context, attrs)
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.bvp_layout, this)
        mViewPager = findViewById(R.id.vp_main)
    }

    override fun onDetachedFromWindow() {
        stopLoop()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startLoop()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isLooping = true
                stopLoop()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                isLooping = false
                startLoop()
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val doNotNeedIntercept = (!mViewPager.isUserInputEnabled
                || adapter != null
                && adapter!!.data!!.size <= 1)
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(
                    !mBannerManager
                        .bannerOptions.isDisallowParentInterceptDownEvent
                )
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = Math.abs(endX - startX)
                val disY = Math.abs(endY - startY)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                false
            )
            MotionEvent.ACTION_OUTSIDE -> {
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (disY > disX) {
            val canLoop = mBannerManager.bannerOptions.isCanLoop
            if (!canLoop) {
                if (currentPosition == 0 && endY - startY > 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(
                        currentPosition != data!!.size - 1
                                || endY - startY >= 0
                    )
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        } else if (disX > disY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (disX > disY) {
            val canLoop = mBannerManager.bannerOptions.isCanLoop
            if (!canLoop) {
                if (currentPosition == 0 && endX - startX > 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(
                        currentPosition != data!!.size - 1
                                || endX - startX >= 0
                    )
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        } else if (disY > disX) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun pageScrollStateChanged(state: Int) {
        if (onPageChangeCallback != null) {
            onPageChangeCallback!!.onPageScrollStateChanged(state)
        }
    }

    private fun pageSelected(position: Int) {
        val size = adapter!!.listSize
        val canLoop = mBannerManager.bannerOptions.isCanLoop
        currentPosition = getRealPosition(position, size)
        val needResetCurrentItem =
            size > 0 && canLoop && (position == 0 || position == BaseBannerAdapter.MAX_VALUE - 1)
        if (needResetCurrentItem) {
            resetCurrentItem(currentPosition)
        }
        if (onPageChangeCallback != null) {
            onPageChangeCallback!!.onPageSelected(currentPosition)
        }
    }

    private fun pageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val listSize = adapter!!.listSize
        val canLoop = mBannerManager.bannerOptions.isCanLoop
        val realPosition = getRealPosition(position, listSize)
        if (listSize > 0) {
            if (onPageChangeCallback != null) {
                onPageChangeCallback!!.onPageScrolled(
                    realPosition,
                    positionOffset,
                    positionOffsetPixels
                )
            }
        }
    }

    private fun handlePosition() {
        if (adapter!!.listSize > 1 && isAutoPlay) {
            mViewPager.currentItem = mViewPager.currentItem + 1
            mHandler.postDelayed(mRunnable, interval.toLong())
        }
    }

    private fun initBannerData() {
        val list = adapter!!.data
        list?.let { setupViewPager(it) }
    }

    private fun setupViewPager(list: List<T>) {
        if (adapter == null) {
            throw NullPointerException("需要适配器")
        }
        val bannerOptions = mBannerManager.bannerOptions
        currentPosition = 0
        adapter!!.isCanLoop = bannerOptions.isCanLoop
        adapter!!.setPageClickListener(mOnPageClickListener)
        mViewPager.adapter = adapter
        if (isCanLoopSafely) {
            mViewPager.setCurrentItem(getOriginalPosition(list.size), false)
        }
        mViewPager.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback)
        startLoop()
    }

    private fun resetCurrentItem(item: Int) {
        if (isCanLoopSafely) {
            mViewPager.setCurrentItem(getOriginalPosition(adapter!!.listSize) + item, false)
        } else {
            mViewPager.setCurrentItem(item, false)
        }
    }

    private val interval: Int
        get() = mBannerManager.bannerOptions.interval
    private val isAutoPlay: Boolean
        get() = mBannerManager.bannerOptions.isAutoPlay
    private val isCanLoopSafely: Boolean
        get() = (mBannerManager.bannerOptions.isCanLoop && adapter != null && adapter!!.listSize > 1)

    val data: List<T>?
        get() = adapter!!.data

    fun startLoop() {
        if (!isLooping && isAutoPlay && adapter != null && adapter!!.listSize > 1) {
            mHandler.postDelayed(mRunnable, interval.toLong())
            isLooping = true
        }
    }

    fun stopLoop() {
        if (isLooping) {
            mHandler.removeCallbacks(mRunnable)
            isLooping = false
        }
    }

    fun setAdapter(adapter: BaseBannerAdapter<T>): BannerPager<T> {
        this.adapter = adapter
        return this
    }

    fun setAutoPlay(autoPlay: Boolean): BannerPager<T> {
        mBannerManager.bannerOptions.isAutoPlay = autoPlay
        if (isAutoPlay) {
            mBannerManager.bannerOptions.isCanLoop = true
        }
        return this
    }

    fun setCanLoop(canLoop: Boolean): BannerPager<T> {
        mBannerManager.bannerOptions.isCanLoop = canLoop
        if (!canLoop) {
            mBannerManager.bannerOptions.isAutoPlay = false
        }
        return this
    }

    fun setOnPageClickListener(onPageClickListener: OnPageClickListener): BannerPager<T> {
        mOnPageClickListener = onPageClickListener
        return this
    }

    @JvmOverloads
    fun create(data: List<T> = ArrayList()) {
        if (adapter == null) {
            throw NullPointerException("需要适配器")
        }
        adapter!!.data = data
        initBannerData()
    }

    var currentItem: Int
        get() = currentPosition
        set(item) {
            if (isCanLoopSafely) {
                val currentItem = mViewPager.currentItem
                val pageSize = adapter!!.listSize
                val canLoop = mBannerManager.bannerOptions.isCanLoop
                val realPosition = getRealPosition(currentItem, adapter!!.listSize)
                if (currentItem != item) {
                    if (item == 0 && realPosition == pageSize - 1) {
                        mViewPager.currentItem = currentItem + 1
                    } else if (realPosition == 0 && item == pageSize - 1) {
                        mViewPager.currentItem = currentItem - 1
                    } else {
                        mViewPager.currentItem = currentItem + (item - realPosition)
                    }
                }
            } else {
                mViewPager.currentItem = item
            }
        }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        if (isCanLoopSafely) {
            val pageSize = adapter!!.listSize
            val currentItem = mViewPager.currentItem
            val canLoop = mBannerManager.bannerOptions.isCanLoop
            val realPosition = getRealPosition(currentItem, pageSize)
            if (currentItem != item) {
                if (item == 0 && realPosition == pageSize - 1) {
                    mViewPager.setCurrentItem(currentItem + 1, smoothScroll)
                } else if (realPosition == 0 && item == pageSize - 1) {
                    mViewPager.setCurrentItem(currentItem - 1, smoothScroll)
                } else {
                    mViewPager.setCurrentItem(currentItem + (item - realPosition), smoothScroll)
                }
            }
        } else {
            mViewPager.setCurrentItem(item, smoothScroll)
        }
    }

    interface OnPageClickListener {
        fun onPageClick(clickedView: View, position: Int)
    }

    fun registerOnPageChangeCallback(onPageChangeCallback: OnPageChangeCallback): BannerPager<T> {
        this.onPageChangeCallback = onPageChangeCallback
        return this
    }

    fun setLifecycleRegistry(lifecycleRegistry: Lifecycle): BannerPager<T> {
        lifecycleRegistry.addObserver(this)
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopLoop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        startLoop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        stopLoop()
    }

    init {
        init(context, attrs)
    }
}