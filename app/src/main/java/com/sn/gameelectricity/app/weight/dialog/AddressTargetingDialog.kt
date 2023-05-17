package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.databinding.DialogAddressTargetingBinding
import com.sn.gameelectricity.ui.adapter.AddressTargetingAdapter
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/20
 * Time:14:42
 * author:dimple
 * 地址定位弹框
 */
class AddressTargetingDialog @JvmOverloads constructor(
    var mContext: Context,
    val shippingAddressViewModel: ShippingAddressViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId), OnGetPoiSearchResultListener {

    private val mBinding by inflate<DialogAddressTargetingBinding>()

    private lateinit var addressTargetingAdapter: AddressTargetingAdapter
    private lateinit var mPoiSearch: PoiSearch
    private var poiInfoList: MutableList<PoiInfo> = mutableListOf()
    private var mLatitude: Double? = 0.0
    private var mLongitude: Double? = 0.0
    private var curChoicePoiInfo: PoiInfo? = null
    private var searchKeyword = "公司"
    private var searchPageNum = 0

    //Item点击事件
    private lateinit var itemOnClickListener: (poiInfo: PoiInfo) -> Unit

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()

        mBinding.ivClose.singleClick {
            dismissDialog()
        }
    }

    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.width = width
        layoutParams.height = height
        layoutParams.gravity = Gravity.BOTTOM
        window!!.setWindowAnimations(R.style.BottomShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.apply {
            addressTargetingAdapter =
                AddressTargetingAdapter(mContext, shippingAddressViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = addressTargetingAdapter

                recyclerView.isRefreshEnabled = false

                setOnLoadMoreListener {
                    searchPageNum++
                    showSearchInfo(searchKeyword, searchPageNum)
                }

                setOnItemClickListener { v, position ->
                    val poiinfo = addressTargetingAdapter.getItemData(position)
                    curChoicePoiInfo = poiinfo
                    itemOnClickListener.invoke(poiinfo)
                    dismissDialog()
                }

                clearAnimation()
                setLoadingMoreBottomHeight(86f)
            }

            etSearch.apply {
                doAfterTextChanged {
                    searchKeyword = it.toString().trim()
                }
                setOnEditorActionListener { v, actionId, event ->
                    var result = false
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchPageNum == 0
                        poiInfoList.clear()
                        showSearchInfo(searchKeyword, searchPageNum)
                        result = true
                    }
                    result
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        searchPageNum = 0
        poiInfoList.clear()
        mPoiSearch = PoiSearch.newInstance()
        mPoiSearch.setOnGetPoiSearchResultListener(this@AddressTargetingDialog)
        showSearchInfo(searchKeyword, searchPageNum)
    }


    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clMain, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            null, "#FFFFFF"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clSearch, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 30f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 30f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 30f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 30f, true).toFloat(),
            null, "#F7F9FE"
        )
    }

    /**
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(mContext)
        errorPageView.post {
            val height = mBinding.recyclerView.height
            errorPageView.changeErrorIconPositionToTOP((0.25 * height).toInt())
        }
        mBinding.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            addressTargetingAdapter.setNewData(arrayListOf())
        }
    }


    /**
     * 显示搜索结果
     */
    private fun showSearchInfo(searchKeyword: String, searchPageNum: Int) {
        mPoiSearch.searchNearby(
            PoiNearbySearchOption()
                .location(LatLng(mLatitude!!, mLongitude!!))
                .radius(1000)
                .keyword(searchKeyword)
                .pageNum(searchPageNum)
        )
    }

    fun setData(
        mLatitude: Double? = 0.0,
        mLongitude: Double? = 0.0,
        searchKeyword: String = "公司"
    ) {
        this.mLatitude = mLatitude
        this.mLongitude = mLongitude
        this.searchKeyword = searchKeyword
    }


    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
            mPoiSearch.destroy()
        }
    }

    /**
     * 定位请求结果返回
     */
    override fun onGetPoiResult(poiResult: PoiResult) {
//        LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "PoiDetailResult =-= ${poiResult.error}")
        if (poiResult.allPoi == null || poiResult.allPoi.size == 0) {
            bindEmptyView()
            return
        }
        if (poiInfoList.size == 0) {
//            val poiInfo = PoiInfo()
//            poiInfo.name = "不显示位置"
//            val poiInfo1 = PoiInfo()
//            poiInfo1.name = poiResult.allPoi[0].city
//            poiInfoList.add(poiInfo)
//            poiInfoList.add(poiInfo1)
        }
        poiInfoList.addAll(poiResult.allPoi)
        mBinding.recyclerView.removeEmptyView()
        addressTargetingAdapter.setNewData(poiInfoList)
        addressTargetingAdapter.setCurChoicePoiInfo(curChoicePoiInfo)
    }

    override fun onGetPoiDetailResult(result: PoiDetailResult) {
        LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "PoiDetailResult =-= ${result.error}")
    }

    override fun onGetPoiDetailResult(result: PoiDetailSearchResult) {
        LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "PoiDetailResult =-= ${result.error}")
    }

    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
    }


    /**
     * item点击事件
     */
    fun setItemOnClickListener(itemOnClickListener: (poiInfo: PoiInfo) -> Unit) {
        this.itemOnClickListener = itemOnClickListener
    }


}