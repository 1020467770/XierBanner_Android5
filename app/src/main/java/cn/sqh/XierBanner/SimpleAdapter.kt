package cn.sqh.XierBanner

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import cn.sqh.mybanner.BaseBannerAdapter
import cn.sqh.mybanner.BaseViewHolder

class SimpleAdapter : BaseBannerAdapter<CustomBean>() {

    var mOnSubViewClickListener: OnSubViewClickListener? = null

    override fun bindData(
        holder: BaseViewHolder<CustomBean>,
        data: CustomBean,
        position: Int,
        pageSize: Int
    ) {
        val imageStart: ImageView = holder.findViewById(R.id.iv_logo)
        holder.setImageResource(R.id.banner_image, data.imageRes)
        holder.setOnClickListener(R.id.iv_logo) { view: View? ->
            if (null != mOnSubViewClickListener) mOnSubViewClickListener!!.onViewClick(view, holder.adapterPosition)
        }
        val alphaAnimator = ObjectAnimator.ofFloat(imageStart, "alpha", 0f, 1f)
        alphaAnimator.duration = 1500
        alphaAnimator.start()
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_custom_view;
    }

    interface OnSubViewClickListener {
        fun onViewClick(view: View?, position: Int)
    }

}