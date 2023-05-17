package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import com.blankj.utilcode.util.ActivityUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.databinding.ActivityPaySuccBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderPayDetailsViewModel
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import org.greenrobot.eventbus.EventBus
import singleClick


/**
 * 支付成功 详情
 */
class PaySuccActivity :
    BaseActivity1<OrderPayDetailsViewModel, ActivityPaySuccBinding>() {

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
                EventBus.getDefault().post(
                    UniversalEvent(
                        UniversalEvent.EVENT_ORDERLIST_REFRESH,
                        null
                    )
                )
                finish()
            }

            tvOrderReview.singleClick {
                with(Bundle()) {
                    putSerializable("type", OrderListFragment.OrderType.PENDING_DELIVERY)
                    putInt("orderId", orderId)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PaySuccActivity,
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

    /**
     * 物理返回键 监听
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(
                UniversalEvent(
                    UniversalEvent.EVENT_ORDERLIST_REFRESH,
                    null
                )
            )
            finish()
        }
        return false
    }


}