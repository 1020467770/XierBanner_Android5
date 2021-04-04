package cn.sqh.mybanner

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.sqh.mybanner.anno.Visibility

class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mViews = SparseArray<View?>()
    @Deprecated("")
    fun bindData(data: T, position: Int, pageSize: Int) {
    }

    fun <V : View> findViewById(viewId: Int): V {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as V
    }

    fun setText(viewId: Int, text: CharSequence?) {
        val view = findViewById<View>(viewId)
        if (view is TextView) {
            view.text = text
        }
    }

    fun setText(viewId: Int, @StringRes textId: Int) {
        val view = findViewById<View>(viewId)
        if (view is TextView) {
            view.setText(textId)
        }
    }

    fun setTextColor(viewId: Int, @ColorInt colorId: Int) {
        val view = findViewById<View>(viewId)
        if (view is TextView) {
            view.setTextColor(colorId)
        }
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int) {
        val view = findViewById<View>(viewId)
        if (view is TextView) {
            view.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
        }
    }

    fun setOnClickListener(viewId: Int, clickListener: View.OnClickListener?) {
        findViewById<View>(viewId).setOnClickListener(clickListener)
    }

    fun setBackgroundResource(viewId: Int, @DrawableRes resId: Int) {
        findViewById<View>(viewId).setBackgroundResource(resId)
    }

    fun setBackgroundColor(viewId: Int, @ColorInt colorId: Int) {
        findViewById<View>(viewId).setBackgroundColor(colorId)
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int) {
        val view = findViewById<View>(viewId)
        if (view is ImageView) {
            view.setImageResource(resId)
        }
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?) {
        val view = findViewById<View>(viewId)
        if (view is ImageView) {
            view.setImageDrawable(drawable)
        }
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?) {
        val view = findViewById<ImageView>(viewId)
        view.setImageBitmap(bitmap)
    }

    fun setVisibility(@IdRes resId: Int, @Visibility visibility: Int) {
        findViewById<View>(resId).visibility = visibility
    }
}