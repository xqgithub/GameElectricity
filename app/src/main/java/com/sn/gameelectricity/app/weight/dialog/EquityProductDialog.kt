package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.just.agentweb.AgentWeb
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.GoodsRightDetailResponse
import com.sn.gameelectricity.databinding.DialogEquityProductBinding
import com.sn.gameelectricity.ui.activity.EquityPickGoodsActivity
import com.sn.gameelectricity.ui.activity.RecoverGoodActivity
import com.sn.gameelectricity.ui.activity.web.WebActivity
import com.sn.gameelectricity.viewmodel.request.RequestEquityViewModel
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick
import java.text.DecimalFormat


/**
 * 权益商品
 */
class EquityProductDialog @JvmOverloads constructor(
    var mContext: Context,
    var goodsId: Int,
    var userId: Int,
    val requestEquityViewModel: RequestEquityViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogEquityProductBinding>()

    private var relationType = RelationType.FRIEND

    private var goodsRightDetailResponse: GoodsRightDetailResponse? = null
    private var mAgentWeb: AgentWeb? = null
    private var preWeb: AgentWeb.PreAgentWeb? = null

    enum class RelationType {
        FRIEND, ENEMMY
    }

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()

        preWeb = AgentWeb.with(mContext as AppCompatActivity)
            .setAgentWebParent(mBinding.webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()

    }

    override fun show() {
        super.show()
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
        setTabStatus(relationType)
        requestEquityViewModel.goodsRightDetail(goodsId,userId) { bean ->
            LogUtils.e("789", bean.toJson())
            goodsRightDetailResponse = bean
            mBinding.ivIcon.load(bean.goodsIcon)
            mBinding.tvGoodsName.text = bean.goodsName
            mBinding.tvDiscountPrice.text =
                "¥" + DecimalFormat("#.##").format(bean.discountPrice)
            mBinding.tvDefaultPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            mBinding.tvDefaultPrice.text =
                "原价 ¥" + DecimalFormat("#.##").format(bean.defaultPrice)
            mBinding.tvRecoverScore.text = "回收积分${bean.recoverScore}"
            if (bean.number == null) {
                mBinding.tvDistributionValue.text = "${0}"
            } else {
                mBinding.tvDistributionValue.text = "${bean.number}"
            }

            if (bean.lastExchangeTime == null) {
                mBinding.tvOrderTimeValue.text = ""
            } else {
                mBinding.tvOrderTimeValue.text = "${bean.lastExchangeTime}"
            }


            if (TextUtils.isEmpty(bean.goodsUrl)) {
                mBinding.clMore.visibility = View.GONE
            } else {
                mBinding.clMore.visibility = View.VISIBLE
            }

            mBinding.clMore.singleClick {
                bean.goodsUrl.let {
                    val intent = Intent(mContext, WebActivity::class.java)
                    intent.putExtra("url", bean.goodsUrl as String)
                    intent.putExtra("title", bean.goodsName)
                    mContext.startActivity(intent)
                }
            }
        }
    }

    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clBg, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            GradientDrawable.Orientation.LEFT_RIGHT, "#6F71F5", "#C073FB"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clContent, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            0f,
            0f,
            null, "#E5ECFE"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvFriend, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            GradientDrawable.Orientation.LEFT_RIGHT, "#6C72FD", "#C973FE"
        )

    }


    /**
     * 设置选项卡状态
     */
    fun setTabStatus(type: RelationType) {
        when (type) {
            RelationType.FRIEND -> {
                mBinding.tvFriend.apply {
                    textSize = 16f
                    setTextColor(Color.parseColor("#ffffff"))
                    setTypeface(null, Typeface.BOLD)
                    PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                        this, -1, "",
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        GradientDrawable.Orientation.LEFT_RIGHT, "#6C72FD", "#C973FE"
                    )
                }

                mBinding.tvEnemy.apply {
                    textSize = 14f
                    setTextColor(Color.parseColor("#233556"))
                    setTypeface(null, Typeface.NORMAL)
                    PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                        this, -1, "",
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        null, "#00000000"
                    )
                }
            }
            RelationType.ENEMMY -> {
                mBinding.tvEnemy.apply {
                    textSize = 16f
                    setTextColor(Color.parseColor("#ffffff"))
                    setTypeface(null, Typeface.BOLD)
                    PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                        this, -1, "",
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        GradientDrawable.Orientation.LEFT_RIGHT, "#6C72FD", "#C973FE"
                    )
                }

                mBinding.tvFriend.apply {
                    textSize = 14f
                    setTextColor(Color.parseColor("#233556"))
                    setTypeface(null, Typeface.NORMAL)
                    PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                        this, -1, "",
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                        null, "#00000000"
                    )
                }
            }
        }
    }


    fun tvFriendOnclick() {
        mBinding.tvFriend.singleClick {
            relationType = RelationType.FRIEND
            setTabStatus(relationType)
            mBinding.clproductinfo.visibility = View.VISIBLE
            mBinding.webcontent.visibility = View.GONE
        }
    }

    fun tvEnemyOnclick() {
        mBinding.tvEnemy.singleClick {
            relationType = RelationType.ENEMMY
            setTabStatus(relationType)

            mBinding.clproductinfo.visibility = View.GONE
            mBinding.webcontent.visibility = View.VISIBLE


            mAgentWeb = preWeb?.go(goodsRightDetailResponse?.storyUrl as String?)
        }
    }

    fun toRecover() {
        mBinding.btnRecover.singleClick {
            with(Bundle()) {
                goodsRightDetailResponse?.goodsId?.let { it1 -> putInt("goodsId", it1) }
                putInt("recoverType", 1)
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    mContext as AppCompatActivity,
                    RecoverGoodActivity::class.java,
                    this,
                    false
                )
            }

            dismissDialog()
        }
    }

    fun toSure() {
        mBinding.btnSure.singleClick {
            LogUtils.e("789", goodsRightDetailResponse.toJson())
            with(Bundle()) {
                goodsRightDetailResponse?.goodsId?.let {
                        it1 -> putInt("goodsId", it1)
                }
                putInt("userId", userId)
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    mContext as AppCompatActivity,
                    EquityPickGoodsActivity::class.java,
                    this,
                    false
                )
            }
            dismissDialog()
        }
    }

    fun setonClickIvClose(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            dismissDialog()
            onCallBack()
        }
    }

    /**
     * 设置是否隐藏提货和回收 栏
     */
    fun isShowCLShare(visibility: Int) {
        mBinding.clShare.visibility = visibility
    }

    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
            mAgentWeb?.webLifeCycle?.onDestroy()
            relationType = RelationType.FRIEND
        }
    }


}