package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.data.model.bean.AddWishBus
import com.sn.gameelectricity.databinding.DialogBottomAddwishBinding
import com.sn.gameelectricity.ui.activity.ProductDetailsActivity
import com.sn.gameelectricity.ui.adapter.AddWishAdapter
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 首页添加心愿购 Dialog
 */
class HomeAddWishDialog @JvmOverloads constructor(
    var mActivity: FragmentActivity?,
    var mContext: Context,
    var requestHomeViewModel: RequestHomeViewModel,
    var viewLifecycleOwner: LifecycleOwner,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.MATCH_PARENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogBottomAddwishBinding>()
    private lateinit var addWishAdapter: AddWishAdapter

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        onClickCancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            addWishAdapter = AddWishAdapter(mContext)
            recyclerView.apply {
                layoutManager = GridLayoutManager(mContext, 2)
                adapter = addWishAdapter
                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(mContext, 3f, true),
                            ScreenTools.getInstance().dp2px(mContext, 5f, true),
                            ScreenTools.getInstance().dp2px(mContext, 3f, true),
                            ScreenTools.getInstance().dp2px(mContext, 5f, true)
                        )
                )

                setOnRefreshListener {
                    requestHomeViewModel.refreshGoodsAll()
                }

                setOnLoadMoreListener {
                    requestHomeViewModel.loadMoreGoodsAll()
                }

                clearAnimation()
                setLoadingMoreBottomHeight(86f)

                setOnItemClickListener { v, position ->
                    val itemData = addWishAdapter.getItemData(position)
                    with(Bundle()) {
                        putInt("goodsId", itemData.id)
                        mActivity?.let {
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                it,
                                ProductDetailsActivity::class.java, this, false
                            )
                        }
                    }
                }

                setOnItemChildClickListener { view, position ->
                    if (view.id == R.id.tvWish) {
                        if (CacheUtil.getWished()?.isWish == true) {
                            val addWishedDialog = AddWishedDialog(mContext)
                            addWishedDialog.onClickSure {
                                val itemData = addWishAdapter.getItemData(position)
                                requestHomeViewModel.wishForResult(itemData.id) {
                                    CacheUtil.setWished(AddWishBus(itemData.icon, true))
                                    eventViewModel.addWishEvent.value =
                                        AddWishBus(itemData.icon, true)
                                    requestHomeViewModel.refreshGoodsAll()
                                    val addWishAwardDialog = AddWishAwardDialog(mContext)
                                    addWishAwardDialog.onClickSure {
                                        dismissDialog()
                                    }
                                    addWishAwardDialog.show()
                                }
                            }
                            addWishedDialog.show()
                        } else {
                            val addWishDialog = AddWishDialog(mContext)
                            addWishDialog.onClickSure {
                                val itemData = addWishAdapter.getItemData(position)
                                requestHomeViewModel.wishForResult(itemData.id) {
                                    CacheUtil.setWished(AddWishBus(itemData.icon, true))
                                    eventViewModel.addWishEvent.value =
                                        AddWishBus(itemData.icon, true)
                                    requestHomeViewModel.refreshGoodsAll()
                                    val addWishAwardDialog = AddWishAwardDialog(mContext)
                                    addWishAwardDialog.onClickSure {
                                        dismissDialog()
                                    }
                                    addWishAwardDialog.show()
                                }
                            }
                            addWishDialog.show()
                        }
                    }
                }
            }
        }

        requestHomeViewModel.refreshGoodsAll()

        requestHomeViewModel.homeGoodsAll.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                mBinding.recyclerView.setEmptyView(R.layout.empty_el_home)
                mBinding.recyclerView.isLoadMoreEnabled = false
            } else {
                mBinding.recyclerView.removeEmptyView()
                mBinding.recyclerView.isLoadMoreEnabled = true
            }
            addWishAdapter.setNewData(it)
        })

        requestHomeViewModel.homeMoreGoodsAll.observe(viewLifecycleOwner, Observer {
            LogUtils.e()
            addWishAdapter.addMoreData(it)
        })
    }


    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.width = width
        layoutParams.height = height
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }


    /**
     * 点击取消按钮
     */
    fun onClickCancel() {
        mBinding.imgClose.singleClick {
            dismissDialog()
        }
    }

    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
        }
    }
}