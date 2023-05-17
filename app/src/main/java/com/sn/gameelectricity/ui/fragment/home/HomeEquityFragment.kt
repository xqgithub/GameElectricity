package com.sn.gameelectricity.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.dialog.BeRewardFailDialog
import com.sn.gameelectricity.app.weight.dialog.EquityFailDialog
import com.sn.gameelectricity.app.weight.dialog.EquitySuccDialog
import com.sn.gameelectricity.databinding.FragmentHomeEquityBinding
import com.sn.gameelectricity.ui.activity.EquityActivity
import com.sn.gameelectricity.ui.activity.LeaderboardActivity
import com.sn.gameelectricity.ui.activity.ProductDetailsActivity
import com.sn.gameelectricity.ui.adapter.HomeEquityAdapter
import com.sn.gameelectricity.viewmodel.request.RequestEquityViewModel
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
 * 权益兑换
 */
class HomeEquityFragment : BaseFragment1<HomeViewModel, FragmentHomeEquityBinding>() {

    private lateinit var homeEquityAdapter: HomeEquityAdapter

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private val requestEquityViewModel: RequestEquityViewModel by viewModels()
    private var pageNum: Int = 1

    val equitySuccDialog by lazy {
        EquitySuccDialog(requireActivity())
    }

    val equityFailDialog by lazy {
        EquityFailDialog(requireActivity())
    }

    val beRewardFailDialog by lazy {
        BeRewardFailDialog(requireActivity())
    }

    override fun initView(savedInstanceState: Bundle?) {
        initActivitiesList()
    }

    private fun initActivitiesList() {
        mViewBind.apply {
            homeEquityAdapter = HomeEquityAdapter(requireActivity(), mViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = homeEquityAdapter
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
                    requestHomeViewModel.equityGoodsPage(pageNum, 10, 3)
                }

                setOnLoadMoreListener {
                    pageNum++
                    requestHomeViewModel.equityGoodsPage(pageNum, 10, 3)
                }

                clearAnimation()
                setLoadingMoreBottomHeight(86f)

                setOnItemClickListener { v, position ->
                    with(Bundle()) {
                        putInt("goodsId", homeEquityAdapter.getItemData(position).id)
                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                            requireActivity(),
                            ProductDetailsActivity::class.java, this, false
                        )
                    }
                }

                setOnItemChildClickListener { view, position ->
                    if (view.id == R.id.tvOperate) {
                        val bean = homeEquityAdapter.getItemData(position)
                        when (bean.rightStatus) {
                            0 -> {//立即兑换
                                requestEquityViewModel.rightsActivity(bean.rightsActivityId, {
                                    requestHomeViewModel.equityGoodsPage(pageNum, 10, 3)
                                    equitySuccDialog.show()
                                    equitySuccDialog.onClickSure {
                                    }
                                    equitySuccDialog.onClickSee {
                                        with(Bundle()) {
                                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                                requireActivity(),
                                                EquityActivity::class.java, this, false
                                            )
                                        }
                                    }
                                }, {

                                    if (it == 1879113784) {
                                        beRewardFailDialog.setContent("这个商品太火被换光啦！")
                                        beRewardFailDialog.show()
                                    } else if (it == 1879113785) {
                                        equityFailDialog.setContent("你的奖券数量不足")
                                        equityFailDialog.onClickTickets {
                                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                                requireActivity(),
                                                LeaderboardActivity::class.java, null, false
                                            )
                                        }
                                        equityFailDialog.show()
                                    } else {
                                        beRewardFailDialog.setContent("活动君走神了！")
                                        beRewardFailDialog.show()
                                    }
                                })
                            }
                        }
                    }
                }


                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
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
        requestHomeViewModel.equityGoodsPage.observe(viewLifecycleOwner, Observer {
            if (pageNum == 1) {
                if (it.isNullOrEmpty()) {
                    mViewBind.recyclerView.setEmptyView(R.layout.empty_el_home)
                    mViewBind.recyclerView.isLoadMoreEnabled = false
                } else {
                    mViewBind.recyclerView.removeEmptyView()
                    mViewBind.recyclerView.isLoadMoreEnabled = true
                }
                homeEquityAdapter.setNewData(it)
            } else {
                homeEquityAdapter.addMoreData(it)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        requestHomeViewModel.equityGoodsPage(pageNum, 10, 3)
    }


}