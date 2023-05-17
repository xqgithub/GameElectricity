package com.sn.gameelectricity.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.dialog.BeRewardFailDialog
import com.sn.gameelectricity.app.weight.dialog.BeRewardSuccDialog
import com.sn.gameelectricity.app.weight.dialog.BeRewardingDialog
import com.sn.gameelectricity.databinding.FragmentHomeDailyrobBinding
import com.sn.gameelectricity.ui.activity.ProductDetailsActivity
import com.sn.gameelectricity.ui.adapter.HomeDailyRobAdapter
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import org.greenrobot.eventbus.EventBus

/**
 * 每日必抢
 */
class HomeDailyRobFragment : BaseFragment1<HomeViewModel, FragmentHomeDailyrobBinding>() {

    private lateinit var homeDailyRobAdapter: HomeDailyRobAdapter

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private var pageNum: Int = 1


    val beRewardingDialog by lazy {
        BeRewardingDialog(requireActivity())
    }

    val beRewardSuccDialog by lazy {
        BeRewardSuccDialog(requireActivity())
    }

    val beRewardFailDialog by lazy {
        BeRewardFailDialog(requireActivity())
    }


    override fun initView(savedInstanceState: Bundle?) {
        initActivitiesList()
    }

    private fun initActivitiesList() {
        mViewBind.apply {
            homeDailyRobAdapter = HomeDailyRobAdapter(requireActivity(), mViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = homeDailyRobAdapter
                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(requireActivity(), 12f, true),
                            ScreenTools.getInstance().dp2px(requireActivity(), 8f, true),
                            ScreenTools.getInstance().dp2px(requireActivity(), 12f, true),
                            ScreenTools.getInstance().dp2px(requireActivity(), 4f, true)
                        )
                )


                setOnRefreshListener {
                    if (!NetworkUtils.isConnected()) {
                        this.isRefreshing = false
                        ToastUtil.showCenter("网络异常，请检查网络设置")
                        return@setOnRefreshListener
                    }
                    pageNum = 1
                    requestHomeViewModel.goodsEveryday(pageNum, 10)
                }



                setOnLoadMoreListener {
                    pageNum++
                    requestHomeViewModel.goodsEveryday(pageNum, 10)
                }

                clearAnimation()
                setLoadingMoreBottomHeight(86f)

                setOnItemClickListener { v, position ->
                    if (homeDailyRobAdapter.getItemData(position).type != 2) {
                        with(Bundle()) {
                            putInt("goodsId", homeDailyRobAdapter.getItemData(position).goodsId)
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                requireActivity(),
                                ProductDetailsActivity::class.java, this, false
                            )
                        }
                    }
                }

                setOnItemChildClickListener { view, position ->
                    if (view.id == R.id.tvOperate) {
                        val bean = homeDailyRobAdapter.getItemData(position)
                        when (bean.rewardType) {
                            1 -> {//冲冲冲！
                                beRewardingDialog.show()
                                requestHomeViewModel.everydayActivity(bean.id, {
                                    requestHomeViewModel.goodsEveryday(pageNum, 10)
                                    beRewardingDialog.dismissDialog()
                                    beRewardSuccDialog.show()
                                    beRewardSuccDialog.onClickSure {
                                        eventViewModel.choiseHomeTop.value = 1
                                        mViewBind.recyclerView.smoothScrollToPosition(0)
                                    }
                                }, {
                                    beRewardingDialog.dismissDialog()
                                    beRewardFailDialog.setContent(it)
                                    beRewardFailDialog.show()
                                })
                            }
                            2 -> {
                                requestHomeViewModel.goodsEveryday(pageNum, 10)
                                eventViewModel.choiseHomeTop.value = 1
                                mViewBind.recyclerView.smoothScrollToPosition(0)
                            }
                        }
                    }
                }


                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        LogUtils.e("789", "HomeDailyRobFragment ======addOnScrollListener===" + dy)
                        if (dy > 0) {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_HOMEHOTFRAGMENT_HOMEDAILYROBFRAGMENT_BYRECYCLERVIEW_SLIDE,
                                    null
                                )
                            )
                        }
                    }
                })
            }
        }
    }

    override fun createObserver() {
        requestHomeViewModel.homeGoodsEveryday.observe(viewLifecycleOwner, Observer {
            if (pageNum == 1) {
                if (it.isNullOrEmpty()) {
                    mViewBind.recyclerView.setEmptyView(R.layout.empty_el_home)
                    mViewBind.recyclerView.isLoadMoreEnabled = false
                } else {
                    mViewBind.recyclerView.removeEmptyView()
                    mViewBind.recyclerView.isLoadMoreEnabled = true
                }
                homeDailyRobAdapter.setNewData(it)
            } else {
                homeDailyRobAdapter.addMoreData(it)
            }

        })

        eventViewModel.refreshHomeDaily.observeInFragment(this@HomeDailyRobFragment, Observer {
            requestHomeViewModel.goodsEveryday(pageNum, 10)
        })
    }

    override fun onResume() {
        super.onResume()
        requestHomeViewModel.goodsEveryday(pageNum, 10)
        LogUtils.e("789", "HomeDailyRobFragment ======onResume===")
    }


}