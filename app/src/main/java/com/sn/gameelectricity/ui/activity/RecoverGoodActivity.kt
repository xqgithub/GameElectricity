package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.dialog.RecoveryDialog
import com.sn.gameelectricity.data.model.bean.AwardGetResponse
import com.sn.gameelectricity.data.model.bean.OrderDetailsBean
import com.sn.gameelectricity.databinding.ActivityRecoverGoodBinding
import com.sn.gameelectricity.viewmodel.OrderDetailsViewModel
import com.sn.gameelectricity.viewmodel.OrderPayDetailsViewModel
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import kotlinx.android.synthetic.main.activity_orderpay_details.*
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.util.ToastUtil
import org.greenrobot.eventbus.EventBus
import singleClick


/**
 * 回收 详情
 */
class RecoverGoodActivity :
    BaseActivity1<OrderPayDetailsViewModel, ActivityRecoverGoodBinding>() {

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    private lateinit var awardGetResponse: AwardGetResponse
    private var orderDetailsBean: OrderDetailsBean? = null

    private val goodsId: Int by lazy {
        intent.getIntExtra("goodsId", 0)
    }

    private val orderId: Int by lazy {
        intent.getIntExtra("orderId", 0)
    }
    private val recoverType: Int by lazy {
        intent.getIntExtra("recoverType", 0)
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        mViewBind.apply {

            ivBack.singleClick {
                finish()
            }

            tvRevocery.singleClick {
                val recoveryDialog = RecoveryDialog(this@RecoverGoodActivity)
                recoveryDialog.onClickSure {
                    if (recoverType == 1) {
                        requestHomeViewModel.gashaponPlay(
                            goodsId,
                            orderId,
                            recoverType
                        ) {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                    null
                                )
                            )
                            finish()
                            ToastUtil.showCustomSmall2Toast("x " + awardGetResponse.recoverCoinNum)
                        }
                    } else {
                        orderDetailsBean?.let {
                            requestHomeViewModel.gashaponPlay(
                                it.payloadGoodsInfoVO.id,
                                orderId,
                                recoverType
                            ) {
                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                        null
                                    )
                                )
                                finish()
                                ToastUtil.showCustomSmall2Toast("x " + awardGetResponse.recoverCoinNum)
                            }
                        }
                    }


                }
                recoveryDialog.show()
            }
        }
    }

    override fun createObserver() {

        if (recoverType == 1) {
            // TODO: 2022/7/11 获取回收商品内容
            requestHomeViewModel.gashaponPlay(goodsId) {
                mViewBind.apply {
                    awardGetResponse = it

                    ivIcon.load(it.icon)
                    tvGoodsName.text = it.goodsName
                    tvIntegralName.text = it.recoverCoinNum.toString()
                }
            }
        } else {
            orderDetailsViewModel.orderById(orderId)
        }

        orderDetailsViewModel.orderByIdLive.observe(this, Observer {
            orderDetailsBean = it

            requestHomeViewModel.gashaponPlay(it.payloadGoodsInfoVO.id) {
                mViewBind.apply {
                    awardGetResponse = it

                    ivIcon.load(it.icon)
                    tvGoodsName.text = it.goodsName
                    tvIntegralName.text = it.recoverCoinNum.toString()
                }
            }
        })
    }
}