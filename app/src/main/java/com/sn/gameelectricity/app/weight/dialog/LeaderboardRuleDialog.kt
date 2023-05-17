package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.just.agentweb.AgentWeb
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogRuleBinding
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.util.toHtml
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/7/7
 * Time:9:42
 * author:dimple
 * 排行榜规则
 */
class LeaderboardRuleDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogRuleBinding>()

    private var mAgentWeb: AgentWeb? = null

    private var preWeb: AgentWeb.PreAgentWeb? = null

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
//        mBinding.atvRule.text =
//            mContext.getString(R.string.leaderboard_rule_content).toHtml()

//        val ruleContent =
//            "1、榜单仅展示前100名用户信息。<br>" +
//                    "2、统计时统计前10000名排用户排行信息。<br>" +
//                    "3、超过10000名玩家排名信息均为未上榜。<br>" +
//                    "4、排行榜每小时更新1次统计结果。<br>" +
//                    "5、排行榜每日20：00结算并发放奖券。<br>" +
//                    "6、每日结算时由于数据量较大在发放奖券时可能会一定时间的延迟，请您耐心等待。<br>" +
//                    "7、奖券根据排名先后不同获得不同奖券，具体如下：<br>" +
//                    " &nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#EF874E\">(1) 1-5名获得        黄金奖券x1</font><br>" +
//                    " &nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#EF874E\">(2) 6-50名获得      白银奖券x1</font><br>" +
//                    " &nbsp;&nbsp;&nbsp;&nbsp;<font color=\"#EF874E\">(3) 51-100名获得    青铜奖券x1</font><br>" +
//                    "8、不同奖券可兑换的奖励不同，具体以权益物品兑换列表为准。<br>" +
//                    "9、奖券一旦获得有效期为24小时，超过24小时会自动失效，请及时使用。"


        preWeb = AgentWeb.with(mContext as AppCompatActivity)
            .setAgentWebParent(mBinding.webRule, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
        //加载网页
        mAgentWeb = preWeb?.go(ConfigConstants.VARIABLE.URL_LEADERBOARD_RULE)
        (mContext as AppCompatActivity).onBackPressedDispatcher.addCallback(mContext as AppCompatActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let { web ->
                        if (web.webCreator.webView.canGoBack()) {
                            web.webCreator.webView.goBack()
                        } else {
                            dismissDialog()
                        }
                    }
                }
            })

        mBinding.ivClose.singleClick {
            dismissDialog()
        }
    }

    override fun onStart() {
        super.onStart()
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
            GradientDrawable.Orientation.LEFT_RIGHT, "#6C72FD", "#C973FE"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clContent, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            0f,
            0f,
            null, "#FFFFFF"
        )
    }


    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
            mAgentWeb?.webLifeCycle?.onDestroy()
        }
    }


}