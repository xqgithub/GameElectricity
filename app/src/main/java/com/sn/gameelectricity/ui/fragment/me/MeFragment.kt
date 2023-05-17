package com.sn.gameelectricity.ui.fragment.me

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.databinding.FragmentMeBinding
import com.sn.gameelectricity.ui.activity.*
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.state.MeViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.util.*
import singleClick

/**
 * 我的页面
 */
class MeFragment : BaseFragment1<MeViewModel, FragmentMeBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarColor(R.color.transparent)
            .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        mViewBind.apply {

            ituiAll.apply {
                setAvatarDataFromRes(R.drawable.ge_me_all)
                setTextData("全部")
                setItemOnClickListener {
                    mViewModel.intentToJumpOrderListActivity(
                        requireActivity(),
                        OrderListFragment.OrderType.All
                    )
                }
            }
            ituiPrePayment.apply {
                setAvatarDataFromRes(R.drawable.ge_me_pre_payment)
                setTextData("待付款")
                setItemOnClickListener {
                    mViewModel.intentToJumpOrderListActivity(
                        requireActivity(),
                        OrderListFragment.OrderType.PENDING_PAYMENT
                    )
                }
            }
            ituiPendingDelivery.apply {
                setAvatarDataFromRes(R.drawable.ge_me_pending_delivery)
                setTextData("待提货")
                setItemOnClickListener {
                    mViewModel.intentToJumpOrderListActivity(
                        requireActivity(),
                        OrderListFragment.OrderType.PENDING_DELIVERED
                    )
                }
            }
            ituiPrepareShipment.apply {
                setAvatarDataFromRes(R.drawable.ge_me_prepare_shipment)
                setTextData("待发货")
                setItemOnClickListener {
                    mViewModel.intentToJumpOrderListActivity(
                        requireActivity(),
                        OrderListFragment.OrderType.PENDING_DELIVERY
                    )
                }
            }

            ituiPendingReceipt.apply {
                setAvatarDataFromRes(R.drawable.ge_me_pending_receipt)
                setTextData("待收货")
                setItemOnClickListener {
                    mViewModel.intentToJumpOrderListActivity(
                        requireActivity(),
                        OrderListFragment.OrderType.PENDING_RECEIPT
                    )
                }
            }

            ituiCompleted.apply {
                setAvatarDataFromRes(R.drawable.ge_me_completed)
                setTextData("已完成")
                setItemOnClickListener {
                    mViewModel.intentToJumpOrderListActivity(
                        requireActivity(),
                        OrderListFragment.OrderType.COMPLETED
                    )
                }
            }

            ituiEquityCenter.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_me_equity_center,
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                )
                setTextData("权益中心")
                setItemOnClickListener {
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        requireActivity(),
                        EquityActivity::class.java, null, false
                    )
                }
            }

            ituiAddressManagement.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_me_address_management,
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                )
                setTextData("地址管理")
                setItemOnClickListener {
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        mActivity,
                        ShippingAddressActivity::class.java, null, false
                    )
                }
            }
            ituiAccountSecurity.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_me_account_security,
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                )
                setTextData("账号安全")
                setItemOnClickListener {
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        mActivity,
                        AccountSecurityActivity::class.java, null, false,
                        -1, -1,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                }
            }

            ivSettting.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    mActivity,
                    PersonalSettingActivity::class.java, null, false
                )
//                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                    mActivity,
//                    TestPag::class.java, null, false
//                )
            }

            clCard.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    requireActivity(),
                    PersonalInfoEditActivity::class.java,
                    null,
                    false
                )
            }
        }
    }


    override fun initData() {
        super.initData()
//        val user = CacheUtil.getUser()!!
        dataEcho()
    }


    override fun lazyLoadData() {
//        mViewModel.focusList("", 0, 10)
//
//        mViewModel.universalListLive.observe(viewLifecycleOwner, Observer { beans ->
//
//        })
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewModel.userInfoLive.observe(viewLifecycleOwner) { beans ->
            mViewBind.apply {
                tvUsernickname.text =
                    PublicPracticalMethodFromKT.ppmfKT.textViewShowLimitedLength(beans.nickName, 24)
                tvPhoneNum.text = mViewModel.getPhoneEncryption(beans.mobile)
                tvMyGoldNum.text =
                    App.appViewModelInstance.goldCoinConversion(beans.goldCoin)
                tvMyScoresNum.text =
                    App.appViewModelInstance.goldCoinConversion(beans.score)
                sivAvatar.load(
                    beans.avatarUrl,
                    R.drawable.default_user_avatar,
                    R.drawable.default_user_avatar
                )
            }
        }

        mViewModel.countUserOrderLive.observe(viewLifecycleOwner) { bean ->
            mViewBind.apply {
                ituiPrePayment.setDotData(
                    "${bean.countToBePaid}", 10f, "#ffffff",
                    dataNullConvertToInt(bean.countToBePaid) as Int > 0
                )
                ituiPendingDelivery.setDotData(
                    "${bean.countGoodsToBePickedUp}", 10f, "#ffffff",
                    dataNullConvertToInt(bean.countGoodsToBePickedUp) as Int > 0
                )

                ituiPrepareShipment.setDotData(
                    "${bean.countToBeShipped}", 10f, "#ffffff",
                    dataNullConvertToInt(bean.countToBeShipped) as Int > 0
                )

                ituiPendingReceipt.setDotData(
                    "${bean.countToBeReceived}", 10f, "#ffffff",
                    dataNullConvertToInt(bean.countToBeReceived) as Int > 0
                )
            }
        }
    }


    override fun onResume() {
        super.onResume()
        mViewModel.getuserInfo()
        mViewModel.countUserOrder()

    }


    override fun createObserver() {
    }

    inner class ProxyClick {

    }

    override fun handleResponseError(errorCode: Int) {
        LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "$errorCode")
    }
}