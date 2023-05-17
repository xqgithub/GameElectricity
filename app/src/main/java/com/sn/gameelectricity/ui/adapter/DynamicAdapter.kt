package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.view.View
import com.sn.gameelectricity.data.model.bean.DynamicEventBean
import com.sn.gameelectricity.data.model.bean.TraceVO
import com.sn.gameelectricity.databinding.AdapterDynamicBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.DateUtil
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools

/**
 * Date:2022/5/24
 * Time:10:02
 * author:dimple
 * 我的动态 适配器
 */
class DynamicAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<DynamicEventBean, AdapterDynamicBinding>() {

    override fun bindView(binding: AdapterDynamicBinding, bean: DynamicEventBean, position: Int) {
        binding.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clContent, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                null, "#99FFFFFF"
            )
//            var a =
//                "成功收取鹅蛋<font color=\"#EF874E\">10</font>个，兑换金币<font color=\"#EF874E\">10</font>个"
            PublicPracticalMethodFromKT.ppmfKT.showHTMLContent(
                binding.tvContent,
                bean.dynamicEventCopyWriting
            )
            tvContentTime.text = DateUtil.getDateToString(bean.createTime, 0)

            tvTime.apply {
                if (bean.isshowTimeHead) this.visibility = View.VISIBLE else this.visibility =
                    View.GONE
                text = bean.timeTimeHead
            }
        }
    }
}