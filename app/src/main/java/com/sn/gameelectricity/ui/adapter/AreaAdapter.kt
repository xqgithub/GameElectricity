package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Color
import com.sn.gameelectricity.data.model.bean.AreaBean
import com.sn.gameelectricity.databinding.AdapterAreaBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT

/**
 * Date:2022/5/12
 * Time:15:44
 * author:dimple
 * 地区列表 适配器
 */
class AreaAdapter(val mContext: Context) :
    BaseFunBindingRecyclerViewAdapter<AreaBean, AdapterAreaBinding>() {
    override fun bindView(binding: AdapterAreaBinding, bean: AreaBean, position: Int) {
        binding.tvTitle.text = bean.areaName
    }
}