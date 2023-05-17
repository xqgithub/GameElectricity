package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.databinding.ActivityPickgoodsSuccBinding
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderPayDetailsViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import singleClick


/**
 * 提货成功 详情
 */
class PickGoodsSuccActivity :
    BaseActivity1<OrderPayDetailsViewModel, ActivityPickgoodsSuccBinding>() {

    private val orderId: Int by lazy {
        intent.getIntExtra("orderId", -1)
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

            tvOrderReview.singleClick {
                with(Bundle()) {
                    putSerializable("type", OrderListFragment.OrderType.PENDING_DELIVERY)
                    putInt("orderId", orderId)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PickGoodsSuccActivity,
                        OrderDetailsActivity::class.java, this, true
                    )
                }
            }

            tvBackHome.singleClick {
                eventViewModel.choiseIndex.value = 0
                ActivityUtils.finishToActivity(MainActivity::class.java, false)
                finish()
            }
        }
    }
}