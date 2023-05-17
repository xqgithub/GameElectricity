package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import com.sn.gameelectricity.databinding.AdapterShippingAddressBinding
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jingbin.library.adapter.BaseByViewHolder

/**
 * Date:2022/5/11
 * Time:19:18
 * author:dimple
 * 收货地址 适配器
 */
class ShippingAddressAdapter(
    val mContext: Context,
    val mViewModel: ShippingAddressViewModel
) : BaseFunBindingRecyclerViewAdapter<DeliveryAddressBean, AdapterShippingAddressBinding>() {

    override fun bindView(
        holder: BaseByViewHolder<DeliveryAddressBean>,
        binding: AdapterShippingAddressBinding,
        bean: DeliveryAddressBean,
        position: Int
    ) {
        binding.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvAddressDefault, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                null, "#FDEDE4"
            )
            tvNamePhone.text = "${bean.addresseeName}  ${bean.phoneNumber}"
            tvAddress.text =
                "${bean.provinceName} ${bean.cityName} ${bean.areaName} ${bean.streetName}${bean.address}"

            if (bean.type == 0) tvAddressDefault.visibility =
                View.VISIBLE else tvAddressDefault.visibility = View.GONE
        }
        holder.addOnClickListener(R.id.btnDelete)
        holder.addOnClickListener(R.id.iv_edit)
        holder.addOnClickListener(R.id.cl_content)
    }


    override fun bindView(
        binding: AdapterShippingAddressBinding,
        bean: DeliveryAddressBean,
        position: Int
    ) {
    }


}