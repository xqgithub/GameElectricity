package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import com.baidu.mapapi.search.core.PoiInfo
import com.sn.gameelectricity.databinding.AdapterAddressTargetingBinding
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter

/**
 * Date:2022/5/20
 * Time:15:46
 * author:dimple
 * 地址定位列表 适配器
 */
class AddressTargetingAdapter(
    var mContext: Context,
    val shippingAddressViewModel: ShippingAddressViewModel
) : BaseFunBindingRecyclerViewAdapter<PoiInfo, AdapterAddressTargetingBinding>() {

    private var curChoicePoiInfo: PoiInfo? = null

    override fun bindView(binding: AdapterAddressTargetingBinding, bean: PoiInfo, position: Int) {
        binding.apply {
            tvStreet.text = bean.name
            tvAddress.text = bean.address
            curChoicePoiInfo?.let {
                if (TextUtils.equals(it.name, bean.name) &&
                    TextUtils.equals(it.address, bean.address)
                ) {
                    tvStreet.setTextColor(Color.parseColor("#EF874E"))
                } else {
                    tvStreet.setTextColor(Color.parseColor("#061925"))
                }
            }

        }
    }


    fun setCurChoicePoiInfo(curChoicePoiInfo: PoiInfo?) {
        this.curChoicePoiInfo = curChoicePoiInfo
    }
}