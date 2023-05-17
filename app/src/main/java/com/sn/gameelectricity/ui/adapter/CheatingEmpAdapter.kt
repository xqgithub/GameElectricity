package com.sn.gameelectricity.ui.adapter

import android.content.Context
import com.sn.gameelectricity.databinding.ItemCheatingPicsBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.jingbin.library.adapter.BaseByViewHolder

class CheatingEmpAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<String, ItemCheatingPicsBinding>() {

    override fun bindView(
        binding: ItemCheatingPicsBinding,
        bean: String,
        position: Int
    ) {
    }

    override fun bindView(
        holder: BaseByViewHolder<String>,
        binding: ItemCheatingPicsBinding,
        bean: String,
        position: Int
    ) {
        binding.apply {
        }
    }

}