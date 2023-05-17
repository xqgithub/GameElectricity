package com.sn.gameelectricity.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.dialog.AddWishAwardDialog
import com.sn.gameelectricity.app.weight.dialog.AddWishDialog
import com.sn.gameelectricity.app.weight.dialog.AddWishedDialog
import com.sn.gameelectricity.data.model.bean.AddWishBus
import com.sn.gameelectricity.databinding.FragmentHomeHotBinding
import com.sn.gameelectricity.ui.activity.MainActivity
import com.sn.gameelectricity.ui.activity.ProductDetailsActivity
import com.sn.gameelectricity.ui.adapter.HomeHotAdapter
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import com.sn.gameelectricity.viewmodel.request.RequestProductDetailsViewModel
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import org.greenrobot.eventbus.EventBus

class HomeHotFragment : BaseFragment1<HomeViewModel, FragmentHomeHotBinding>() {

    private lateinit var homeHotAdapter: HomeHotAdapter

    private var topicId: Int = 0

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private val requestProductDetailsViewModel: RequestProductDetailsViewModel by viewModels()

    companion object {
        fun newInstance(topicId: Int): HomeHotFragment {
            val homeHotFragment = HomeHotFragment()
            with(Bundle()) {
                putInt("topicId", topicId)
                homeHotFragment.arguments = this
            }
            return homeHotFragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initActivitiesList()
    }

    override fun initData() {
        arguments?.let {
            topicId = it.getInt("topicId")
        }

        requestViewModel()
    }

    private fun initActivitiesList() {
        mViewBind.apply {
            homeHotAdapter = HomeHotAdapter(requireActivity(), mViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = homeHotAdapter
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
                    requestHomeViewModel.refreshGoodsPage(topicId)
                }

//                mViewBind.recyclerView.isRefreshing = true

                setOnLoadMoreListener {
                    requestHomeViewModel.loadMoreGoodsPage(topicId)
                }

                clearAnimation()
                setLoadingMoreBottomHeight(86f)

                setOnItemClickListener { v, position ->
                    with(Bundle()) {
                        putInt("goodsId", homeHotAdapter.getItemData(position).id)
                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                            requireActivity(),
                            ProductDetailsActivity::class.java, this, false
                        )
                    }
                }
                setOnItemChildClickListener { view, position ->
                    if (view.id == R.id.tvWish) {
                        if (CacheUtil.getWished()?.isWish == true) {
                            val addWishedDialog = AddWishedDialog(requireActivity())
                            addWishedDialog.onClickSure {
                                addWishData(position)
                            }
                            addWishedDialog.show()
                        } else {
                            val addWishDialog = AddWishDialog(requireActivity())
                            addWishDialog.onClickSure {
                                addWishData(position)
                            }
                            addWishDialog.show()
                        }
                    }
                }

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        LogUtils.e("789","HomeHotFragment ======addOnScrollListener==="+dy)
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

    private fun addWishData(position: Int) {
        val itemData = homeHotAdapter.getItemData(position)
        requestProductDetailsViewModel.wishForResult(itemData.id) {
            CacheUtil.setWished(AddWishBus(itemData.icon, true))
            eventViewModel.addWishEvent.value = AddWishBus(itemData.icon, true)
            val addWishAwardDialog = AddWishAwardDialog(requireActivity())
            addWishAwardDialog.onClickSure {
                eventViewModel.choiseHomeTop.value = 1
                mViewBind.recyclerView.smoothScrollToPosition(0)
                ActivityUtils.finishToActivity(MainActivity::class.java, false)
            }
            addWishAwardDialog.show()
        }
    }

    override fun createObserver() {
        eventViewModel.addWishEvent.observeInFragment(this@HomeHotFragment, Observer {
            if (it.isWish) {
                requestHomeViewModel.refreshGoodsPage(topicId)
            }
        })

    }

    fun requestViewModel() {
        requestHomeViewModel.homeGoodsPage.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                mViewBind.recyclerView.setEmptyView(R.layout.empty_el_home)
                mViewBind.recyclerView.isLoadMoreEnabled = false
            } else {
                mViewBind.recyclerView.removeEmptyView()
                mViewBind.recyclerView.isLoadMoreEnabled = true
            }
            homeHotAdapter.setNewData(it)
        })
        requestHomeViewModel.homeMoreGoodsPage.observe(viewLifecycleOwner, Observer {
            homeHotAdapter.addMoreData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e("789","HomeHotFragment ======onResume===")
        requestHomeViewModel.refreshGoodsPage(topicId)
    }
}