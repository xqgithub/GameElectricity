package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.core.util.size
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.weight.dialog.LogisticsInformationDialog
import com.sn.gameelectricity.data.model.bean.OrderListbean
import com.sn.gameelectricity.databinding.AdapterOrderListBinding
import com.sn.gameelectricity.ui.activity.ShippingAddressActivity
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.OrderListViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.amountDataConvert
import me.hgj.jetpackmvvm.util.load
import me.jingbin.library.adapter.BaseByViewHolder
import singleClick

/**
 * Date:2022/5/10
 * Time:16:21
 * author:dimple
 * 订单列表适配器
 */
class OrderListAdapter(
    val mContext: Context,
    val mViewModel: OrderListViewModel
) : BaseFunBindingRecyclerViewAdapter<OrderListbean, AdapterOrderListBinding>() {

    /**
     * 倒计时集合
     */
    private var countDownMap: SparseArray<CountDownTimer> = SparseArray()

//    private var countDownTimer: CountDownTimer? = null

    override fun bindView(
        holder: BaseByViewHolder<OrderListbean>,
        binding: AdapterOrderListBinding,
        bean: OrderListbean,
        position: Int
    ) {
//        super.bindView(holder, binding, bean, position)
        binding.apply {
            //设置Item背景
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clMain, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                null, "#FFFFFF"
            )


            var pay_color_start = ""
            var pay_color_end = ""
            var pay_isEnabled = true

            when (bean.payloadOrderSimpleInfoVO.groupAssistStatus) {
                1, 2 -> {
                    var avatarUrls = mutableListOf<String>()
                    val groupUserNum = bean.payloadOrderSimpleInfoVO.groupUserNum
                    if (groupUserNum > 3) {
                        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderSimpleInfoVO.avatarUrlList)) {
                            if (bean.payloadOrderSimpleInfoVO.avatarUrlList.size > 3) {
                                for (i in 0..2) {
                                    avatarUrls.add(
                                        i,
                                        bean.payloadOrderSimpleInfoVO.avatarUrlList[i]
                                    )
                                }
                            } else {
                                for (i in bean.payloadOrderSimpleInfoVO.avatarUrlList.indices) {
                                    avatarUrls.add(
                                        i,
                                        bean.payloadOrderSimpleInfoVO.avatarUrlList[i]
                                    )
                                }
                                val poordata = 4 - bean.payloadOrderSimpleInfoVO.avatarUrlList.size
                                if (poordata == 3) {
                                    avatarUrls.add(1, "")
                                    avatarUrls.add(2, "")
                                } else if (poordata == 2) {
                                    avatarUrls.add(2, "")
                                }
                            }
                        } else {
                            avatarUrls.add(0, "")
                            avatarUrls.add(1, "")
                            avatarUrls.add(2, "")
                        }
                        avatarUrls.add("ge_default_avatar_more")
                    } else {
                        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderSimpleInfoVO.avatarUrlList)) {
                            for (i in bean.payloadOrderSimpleInfoVO.avatarUrlList.indices) {
                                avatarUrls.add(
                                    i,
                                    bean.payloadOrderSimpleInfoVO.avatarUrlList[i]
                                )
                            }
                            val poordata =
                                groupUserNum - bean.payloadOrderSimpleInfoVO.avatarUrlList.size
                            if (poordata == 2) {
                                avatarUrls.add("")
                                avatarUrls.add("")
                            } else if (poordata == 1) {
                                avatarUrls.add("")
                            }
                        } else {
                            for (i in 0 until groupUserNum) {
                                avatarUrls.add("")
                            }
                        }
                    }

                    clHelpAvatars.visibility = View.VISIBLE
                    tvHelp.visibility = View.VISIBLE
                    mViewModel.addHelpAvatars(
                        mContext,
                        clHelpAvatars,
                        avatarUrls
                    )
                    tvHelp.text =
                        if (bean.payloadOrderSimpleInfoVO.groupAssistStatus == 1) "助力中" else "助力成功"

                    if (tvHelp.text.toString() == "助力成功") {
                        pay_color_start = "#F19B3F"
                        pay_color_end = "#ED7A57"
                        pay_isEnabled = true
                    } else {
                        pay_color_start = "#33F19B3F"
                        pay_color_end = "#33ED7A57"
                        pay_isEnabled = false
                    }
                }
                else -> {
                    clHelpAvatars.visibility = View.GONE
                    tvHelp.visibility = View.GONE
                    pay_color_start = "#F19B3F"
                    pay_color_end = "#ED7A57"
                    pay_isEnabled = true
                }
            }

            tvOrderNumber.text = "订单：${bean.payloadOrderSimpleInfoVO.orderNo}"
            sivImg.load(bean.payloadGoodsListVO.icon, R.drawable.img_empty, R.drawable.img_empty)
            tvOrderName.text = bean.payloadGoodsListVO.goodsName
            tvOrderPrice.text = "¥${amountDataConvert(bean.payloadGoodsListVO.discountPrice)}"
            tvOrderNum.text = "x${bean.payloadOrderSimpleInfoVO.orderNumber}"
            tvOrderIntroduce.text = "${bean.payloadGoodsListVO.patternName}"

            when (bean.payloadOrderSimpleInfoVO.orderType) {
                0 -> {//普通
                    tatRealPayment.apply {
                        visibility = View.VISIBLE
                        setDescribeText("需付款")
                        setNumText("¥${amountDataConvert(bean.payloadOrderSimpleInfoVO.factAmt)}")
                    }
                    tatCoinsIntegral.visibility = View.GONE
                }
                1 -> {//积分换购
                    tatCoinsIntegral.apply {
                        visibility = View.VISIBLE
                        setDescribeText("消耗积分")
                        setNumText("${bean.payloadOrderSimpleInfoVO.discountConsume}")
                    }
                    tatRealPayment.apply {
                        visibility = View.VISIBLE
                        setDescribeText("实付款")
                        setNumText("¥${amountDataConvert(bean.payloadOrderSimpleInfoVO.factAmt)}")
                    }
                }
                2 -> {//金币抵扣
                    tatRealPayment.apply {
                        visibility = View.VISIBLE
                        setDescribeText("实付款")
                        setNumText("¥${amountDataConvert(bean.payloadOrderSimpleInfoVO.factAmt)}")
                    }
                    tatCoinsIntegral.apply {
                        visibility = View.VISIBLE
                        setDescribeText("消耗金币")
                        setNumText("${bean.payloadOrderSimpleInfoVO.discountConsume}")
                    }
                }
                3, 8 -> {//邀请半价
                    tatRealPayment.apply {
                        visibility = View.VISIBLE
                        setDescribeText("需付款")
                        setNumText("¥${amountDataConvert(bean.payloadOrderSimpleInfoVO.factAmt)}")
                    }
                    tatCoinsIntegral.visibility = View.GONE
                }
                4, 5, 6, 7 -> {
                    tatCoinsIntegral.visibility = View.GONE
                    tatRealPayment.apply {
                        visibility =
                            if (bean.payloadOrderSimpleInfoVO.status == 2) View.VISIBLE else View.INVISIBLE
                        setDescribeText("实付款")
                        setNumText("¥${amountDataConvert(bean.payloadOrderSimpleInfoVO.factAmt)}")
                    }
                }
            }

            when (bean.payloadOrderSimpleInfoVO.status) {
                0 -> {
                    tvOrderType.apply {
                        text = "待付款"
                        setTextColor(Color.parseColor("#EF874E"))
                    }
                    tvOrderOperate3.apply {
                        isEnabled = pay_isEnabled
                        text = "立即支付"
                        setTextColor(Color.parseColor("#FFFFFF"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            tvOrderOperate3, -1, "",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            GradientDrawable.Orientation.LEFT_RIGHT, pay_color_start, pay_color_end
                        )
                        visibility = View.VISIBLE

                        mViewModel.startCountdownTask(
                            bean.payloadOrderSimpleInfoVO.currentTime,
                            bean.payloadOrderSimpleInfoVO.payEndTime
                        ) {
//                        val hour = it["hour"]!!.times(60).times(60)
                            val minute = it["minute"]!!.times(60)
                            val second = it["second"]!!
                            val countTime = minute + second
//                            if ((minute + second) < 0L) 10 * 60L else (minute + second)
                            if (countTime > 0) {
                                countDownMap.get(this.hashCode())?.cancel()
                                val countDownTimer =
                                    object : CountDownTimer(countTime * 1000L, 1000) {
                                        override fun onTick(millisUntilFinished: Long) {
                                            val time = millisUntilFinished / 1000L
                                            text = "立即支付 ${mViewModel.showCountTimeContent(time)}"
                                        }

                                        override fun onFinish() {
                                            removeData(position)
                                            notifyDataSetChanged()
                                        }
                                    }.start()
                                countDownMap.put(
                                    this.hashCode(),
                                    countDownTimer
                                )
                            }
                        }
                    }
                    tvOrderOperate2.visibility = View.GONE
                    tvOrderOperate.visibility = View.INVISIBLE
                }
                1 -> {
                    tvOrderType.apply {
                        text = "待提货"
                        setTextColor(Color.parseColor("#EF874E"))
                    }
                    tvOrderOperate.apply {
                        text = "提货"
                        setTextColor(Color.parseColor("#FFFFFF"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, -1, "",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
                        )
                        visibility = View.VISIBLE
                    }
                    tvOrderOperate2.apply {
                        text = "回收"
                        setTextColor(Color.parseColor("#EF874E"))

                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, ScreenTools.getInstance().dp2px(mContext, 1f, true), "#EF874E",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            null, "#00000000"
                        )
                        visibility = View.VISIBLE
                    }

                    tvOrderOperate3.visibility = View.GONE
                }
                2 -> {
                    tvOrderType.apply {
                        text = "待发货"
                        setTextColor(Color.parseColor("#EF874E"))
                    }
                    tvOrderOperate.apply {
                        text = "变更地址"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, ScreenTools.getInstance().dp2px(mContext, 1f, true), "#ECF0F8",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            null, "#00000000"
                        )
                        visibility = View.VISIBLE
                    }
                    tvOrderOperate2.visibility = View.GONE
                    tvOrderOperate3.visibility = View.GONE
                }
                3 -> {
                    tvOrderType.apply {
                        text = "待收货"
                        setTextColor(Color.parseColor("#EF874E"))
                    }

                    tvOrderOperate.apply {
                        text = "查看物流"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, ScreenTools.getInstance().dp2px(mContext, 1f, true), "#ECF0F8",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            null, "#00000000"
                        )
                    }
                    tvOrderOperate2.visibility = View.GONE
                    tvOrderOperate3.visibility = View.GONE
                }
                4 -> {
                    tvOrderType.apply {
                        text = "已完成"
                        setTextColor(Color.parseColor("#EF874E"))
                    }
                    tvOrderOperate2.apply {
                        text = "查看物流"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, ScreenTools.getInstance().dp2px(mContext, 1f, true), "#ECF0F8",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            null, "#00000000"
                        )
                        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderSimpleInfoVO.resultType)
                            && bean.payloadOrderSimpleInfoVO.resultType == 1
                        ) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                        }
                    }
                    tvOrderOperate.apply {
                        text = "删除订单"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, ScreenTools.getInstance().dp2px(mContext, 1f, true), "#ECF0F8",
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                            null, "#00000000"
                        )
                        visibility = View.VISIBLE
                    }
                    tvOrderOperate3.visibility = View.GONE
                }
            }


        }
        holder.addOnClickListener(R.id.tv_order_operate)
        holder.addOnClickListener(R.id.tv_order_operate2)
    }


    override fun bindView(binding: AdapterOrderListBinding, bean: OrderListbean, position: Int) {
    }


    /**
     * 清空资源
     */
    fun cancelAllTimers() {
        countDownMap.forEach { key, value ->
            countDownMap.get(key)?.cancel()
        }
    }
}