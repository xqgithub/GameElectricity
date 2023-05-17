package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.ShareManager
import com.sn.gameelectricity.databinding.DialogShareBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/23
 * Time:20:21
 * author:dimple
 * 分享弹框
 */
class ShareDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogShareBinding>()

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
            null, "#FFFFFF"
        )

        mBinding.apply {
            ituiWeixin.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_dialog_weixin,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("微信", 12f, "#233556")
                setItemOnClickListener {
                    ShareManager.shareManager.shareWebpage2WxFriend(
                        "前方有羊毛可薅",
                        "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！",
                        "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                        PublicPracticalMethodFromKT.ppmfKT.samplingRateCompress(
                            mContext,
                            R.drawable.ge_friends_share
                        )
                    )
                }
            }

            ituiCircleFriends.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_dialog_circle_friends,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("朋友圈", 12f, "#233556")
                setItemOnClickListener {
                    ShareManager.shareManager.shareWebpage2WxMoments(
                        "前方有羊毛可薅",
                        "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！",
                        "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                        PublicPracticalMethodFromKT.ppmfKT.samplingRateCompress(
                            mContext,
                            R.drawable.ge_friends_share
                        )
                    )
                }
            }

            ituiQq.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_dialog_qq,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("QQ", 12f, "#233556")
                setItemOnClickListener {
                    ShareManager.shareManager.shareWebpage2QQ(
                        "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                        "前方有羊毛可薅",
                        "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！"
                    )
                }
            }

            ituiWeibo.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_dialog_weibo,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("微博", 12f, "#233556")
                setItemOnClickListener {
                    ShareManager.shareManager.shareWebpage2Weibo(
                        "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                        "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！"
                    )
                }
            }
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