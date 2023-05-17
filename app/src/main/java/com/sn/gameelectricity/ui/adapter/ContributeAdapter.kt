package com.sn.gameelectricity.ui.adapter

import android.content.Context
import com.sn.gameelectricity.databinding.AdapterContributeBinding
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools

/**
 * Date:2022/5/20
 * Time:10:00
 * author:dimple
 * 贡献明细 适配器
 */
class ContributeAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<String, AdapterContributeBinding>() {

    override fun bindView(binding: AdapterContributeBinding, bean: String, position: Int) {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            binding.clMain, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
            null, "#FFFFFF"
        )
    }
}