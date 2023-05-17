package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.data.model.bean.AreaSelectedBean
import com.sn.gameelectricity.databinding.DialogAreaBinding
import com.sn.gameelectricity.ui.adapter.AreaAdapter
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/12
 * Time:14:52
 * author:dimple
 * 地区选择弹框
 */
class AreaDialog @JvmOverloads constructor(
    var mContext: Context,
    val shippingAddressViewModel: ShippingAddressViewModel,
    var areaSelectedBean: AreaSelectedBean,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {
    private val mBinding by inflate<DialogAreaBinding>()

    enum class AreaType {
        PROVINCE, CITY, AREA, STREET
    }

    private var pageNum: Int = 1
    private var areaType = AreaType.PROVINCE
//    private var areaCode_Province = 0
//    private var areaCode_City = 0
//    private var areaCode_Area = 0
//    private var areaCode_Street = 0
//    private var mProvince = ""
//    private var mCity = ""
//    private var mArea = ""
//    private var mStreet = ""


    private lateinit var areaAdapter: AreaAdapter

    //地区选择事件
    private lateinit var areaSelectListener: (areaSelectedBean: AreaSelectedBean) -> Unit

//    private var provinceList = mutableListOf<String>("湖北省", "安徽省", "北京", "福建省", "广东省")
//    private var cityList = mutableListOf<String>("武汉市", "鄂州市", "恩施土家族苗族自治区", "黄石市", "荆州市")
//    private var areaList = mutableListOf<String>("硚口区", "青山区", "江汉区", "江岸区", "汉阳区")
//    private var streetList = mutableListOf<String>("文华学院", "佳园路", "华中科技大学", "SBI创业街", "森林公园")


    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()
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
        initData()
    }

    override fun onStart() {
        super.onStart()
    }


    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clDialog, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            null, "#FFFFFFFF"
        )
    }


    private fun initData() {
        initAreaList()
        setAreainfo()
        dataEcho()
        shippingAddressViewModel.deliveryAddressAreaList(0, areaType)

        mBinding.apply {


            tvProvince.apply {
                singleClick {
                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mProvince)) {
                        pageNum = 1
                        areaType = AreaType.PROVINCE
                        areaAdapter.clear()
                        shippingAddressViewModel.deliveryAddressAreaList(0, areaType)
                    }
                }
            }


            tvCity.apply {
                singleClick {
                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mCity)) {
                        pageNum = 1
                        areaType = AreaType.CITY
                        areaAdapter.clear()
                        shippingAddressViewModel.deliveryAddressAreaList(
                            areaSelectedBean.areaCode_Province,
                            areaType
                        )
                    }
                }
            }

            tvArea.apply {
                singleClick {
                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mArea)) {
                        pageNum = 1
                        areaType = AreaType.AREA
                        areaAdapter.clear()
                        shippingAddressViewModel.deliveryAddressAreaList(
                            areaSelectedBean.areaCode_City,
                            areaType
                        )
                    }
                }
            }

            tvStreet.apply {
                singleClick {
                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mStreet)) {
                        pageNum = 1
                        areaType = AreaType.STREET
                        areaAdapter.clear()
                        shippingAddressViewModel.deliveryAddressAreaList(
                            areaSelectedBean.areaCode_Area,
                            areaType
                        )
                    }
                }
            }
        }
    }


    fun setAreaSelectListener(areaSelectListener: (areaSelectedBean: AreaSelectedBean) -> Unit) {
        this.areaSelectListener = areaSelectListener
    }

    /**
     * 设置所选地区信息
     */
    private fun setAreainfo() {
        mBinding.apply {
            tvProvince.apply {
                if (PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mProvince)) {
                    text = "请选择"
                    setTextColor(Color.parseColor("#EF874E"))
                    tvCity.visibility = View.GONE
                    tvArea.visibility = View.GONE
                    tvStreet.visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    text = areaSelectedBean.mProvince
                    setTextColor(Color.parseColor("#061925"))
                    tvCity.visibility = View.VISIBLE
                }
            }

            tvCity.apply {
                if (PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mCity)) {
                    text = "请选择"
                    setTextColor(Color.parseColor("#EF874E"))
                    tvArea.visibility = View.GONE
                    tvStreet.visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    text = areaSelectedBean.mCity
                    setTextColor(Color.parseColor("#061925"))
                    tvArea.visibility = View.VISIBLE
                }
            }
            tvArea.apply {
                if (PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mArea)) {
                    text = "请选择"
                    setTextColor(Color.parseColor("#EF874E"))
                    tvStreet.visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    text = areaSelectedBean.mArea
                    setTextColor(Color.parseColor("#061925"))
                    tvStreet.visibility = View.VISIBLE
                }
            }

            tvStreet.apply {
                if (PublicPracticalMethodFromKT.ppmfKT.isBlank(areaSelectedBean.mStreet)) {
                    text = "请选择"
                    setTextColor(Color.parseColor("#EF874E"))
                } else {
                    visibility = View.VISIBLE
                    text = areaSelectedBean.mStreet
                    setTextColor(Color.parseColor("#061925"))
                }
            }
        }
    }

    /**
     * 初始化地区列表
     */
    private fun initAreaList() {
        mBinding.apply {
            areaAdapter = AreaAdapter(mContext)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = areaAdapter

                clearAnimation()
                setLoadingMoreBottomHeight(6f)

                isRefreshEnabled = false
//                isLoadMoreEnabled = false
//                setOnLoadMoreListener {
//                    pageNum++
//                    var area_code = when (areaType) {
//                        AreaType.PROVINCE -> areaSelectedBean.areaCode_Province
//                        AreaType.CITY -> areaSelectedBean.areaCode_Province
//                        AreaType.AREA -> areaSelectedBean.areaCode_City
//                        AreaType.STREET -> areaSelectedBean.areaCode_Area
//                    }
//                    shippingAddressViewModel.deliveryAddressAreaList(
//                        area_code,
//                        areaType,
//                        pageNum
//                    )
//                }

                recyclerView.setOnItemClickListener { v, position ->
                    when (areaType) {
                        AreaType.PROVINCE -> {
                            areaType = AreaType.CITY
                            val bean = areaAdapter.getItemData(position)
                            areaSelectedBean.mProvince = bean.areaName
                            areaSelectedBean.areaCode_Province = bean.areaCode
                            pageNum = 1
                            areaAdapter.clear()
                            shippingAddressViewModel.deliveryAddressAreaList(
                                areaSelectedBean.areaCode_Province,
                                areaType
                            )

                            areaSelectedBean.mCity = ""
                            areaSelectedBean.mArea = ""
                            areaSelectedBean.mStreet = ""
                        }
                        AreaType.CITY -> {
                            areaType = AreaType.AREA
                            val bean = areaAdapter.getItemData(position)
                            areaSelectedBean.mCity = bean.areaName
                            areaSelectedBean.areaCode_City = bean.areaCode
                            pageNum = 1
                            areaAdapter.clear()
                            shippingAddressViewModel.deliveryAddressAreaList(
                                areaSelectedBean.areaCode_City,
                                areaType
                            )
                            areaSelectedBean.mArea = ""
                            areaSelectedBean.mStreet = ""
                        }
                        AreaType.AREA -> {
                            areaType = AreaType.STREET
                            val bean = areaAdapter.getItemData(position)
                            areaSelectedBean.mArea = bean.areaName
                            areaSelectedBean.areaCode_Area = bean.areaCode
                            pageNum = 1
                            areaAdapter.clear()
                            shippingAddressViewModel.deliveryAddressAreaList(
                                areaSelectedBean.areaCode_Area,
                                areaType
                            )
                            areaSelectedBean.mStreet = ""
                        }
                        AreaType.STREET -> {
                            val bean = areaAdapter.getItemData(position)
                            areaSelectedBean.mStreet = bean.areaName
                            areaSelectedBean.areaCode_Street = bean.areaCode
                            areaSelectListener.invoke(areaSelectedBean)
                        }
                    }
                    setAreainfo()
                }
            }
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        shippingAddressViewModel.apply {
            deliveryAddressAreaListLive.observe((mContext as AppCompatActivity)) { beans ->
                if (pageNum == 1) {
                    if (beans.isEmpty()) {
                        bindEmptyView()
                    } else {
                        mBinding.recyclerView.removeEmptyView()
                        areaAdapter.setNewData(beans)
                    }
                } else {
                    areaAdapter.addMoreData(beans)
                }
            }
        }
    }

    /**
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(mContext)
        with(errorPageView) {
            post {
                val recyclerView_height = mBinding.recyclerView.height
                setErrorIcon(
                    R.drawable.ge_error,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent("哦豁，一片荒芜～", 12f, "#A1A7AF")
                changeErrorIconPositionToTOP(
                    (0.4 * recyclerView_height).toInt()
                )
                changeErrorTextPositionToTOP(
                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
                )
            }
        }

        mBinding.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            areaAdapter.setNewData(arrayListOf())
        }
    }

    /**
     * 关闭对话框并且销毁
     */
    fun closeDialog(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            dismissDialog()
            onCallBack()
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