package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.data.model.bean.TraceVO
import com.sn.gameelectricity.databinding.AdapterLogisticsInfomationBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter

/**
 * Date:2022/6/1
 * Time:14:29
 * author:dimple
 * 物流信息 适配器
 */
class LogisticsInformationAdapter(val mContext: Context) :
    BaseFunBindingRecyclerViewAdapter<TraceVO, AdapterLogisticsInfomationBinding>() {
    override fun bindView(
        binding: AdapterLogisticsInfomationBinding,
        bean: TraceVO,
        position: Int
    ) {
        binding.apply {
            if (position == 0) {
                vSplitline.visibility = View.GONE
                tvShippingStatus.setTextColor(Color.parseColor("#EF874E"))
                tvTime.setTextColor(Color.parseColor("#EF874E"))
                tvShippingDetails.setTextColor(Color.parseColor("#061925"))
                ivDots.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ge_dots_runing
                    )
                )
            } else {
                vSplitline.visibility = View.VISIBLE
                tvShippingStatus.setTextColor(Color.parseColor("#A1A7AF"))
                tvTime.setTextColor(Color.parseColor("#A1A7AF"))
                tvShippingDetails.setTextColor(Color.parseColor("#A1A7AF"))
                ivDots.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ge_dots_runed
                    )
                )
            }
            if (position == itemCount - 1) {
                vSplitline2.visibility = View.GONE
            } else {
                vSplitline2.visibility = View.VISIBLE
            }

            if (bean.ishowaction) {
                tvShippingStatus.visibility = View.VISIBLE
            } else {
                tvShippingStatus.visibility = View.GONE
            }

            tvShippingStatus.text = bean.actionname
            tvTime.text = bean.acceptTime
            tvShippingDetails.text = bean.acceptStation

        }
    }

}