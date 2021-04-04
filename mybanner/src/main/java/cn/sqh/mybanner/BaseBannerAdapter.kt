package cn.sqh.mybanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import cn.sqh.mybanner.BannerPager.OnPageClickListener
import cn.sqh.mybanner.utils.BannerUtils.getRealPosition
import java.util.*

abstract class BaseBannerAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {
    protected var mList: MutableList<T> = ArrayList()
    var isCanLoop = false
    private var mPageClickListener: OnPageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val itemView =
            LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return createViewHolder(parent, itemView, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val realPosition = getRealPosition(position, listSize)
        holder.itemView.setOnClickListener { clickedView ->
            if (mPageClickListener != null) {
                mPageClickListener!!.onPageClick(clickedView, getRealPosition(position, listSize))
            }
        }
        bindData(holder, mList[realPosition], realPosition, listSize)
    }

    override fun getItemViewType(position: Int): Int {
        val realPosition = getRealPosition(position, listSize)
        return getViewType(realPosition)
    }

    override fun getItemCount(): Int {
        return if (isCanLoop && listSize > 1) {
            MAX_VALUE
        } else {
            listSize
        }
    }

    var data: List<T>?
        get() = mList
        set(list) {
            if (null != list) {
                mList.clear()
                mList.addAll(list)
            }
        }

    fun setPageClickListener(pageClickListener: OnPageClickListener?) {
        mPageClickListener = pageClickListener
    }

    val listSize: Int
        get() = mList.size

    protected fun getViewType(position: Int): Int {
        return 0
    }

    fun createViewHolder(parent: ViewGroup, itemView: View?, viewType: Int): BaseViewHolder<T> {
        return BaseViewHolder(itemView!!)
    }

    protected abstract fun bindData(
        holder: BaseViewHolder<T>,
        data: T,
        position: Int,
        pageSize: Int
    )

    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int

    companion object {
        const val MAX_VALUE = 1000
    }
}