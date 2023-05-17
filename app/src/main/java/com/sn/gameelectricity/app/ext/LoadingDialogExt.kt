package com.sn.gameelectricity.app.ext

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.CommonUtil
import com.sn.gameelectricity.app.util.SettingUtil
import com.sn.gameelectricity.app.weight.dialog.LoadingDialog
import me.hgj.jetpackmvvm.base.viewmodel.LoadingBean
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jessyan.autosize.AutoSizeConfig


//loading框
//private var loadingDialog: MaterialDialog? = null

private var loadingDialog: LoadingDialog? = null

/**
 * 打开等待框
 */

fun AppCompatActivity.showLoadingExt(loadingBean: LoadingBean?) {
    if (!this.isFinishing) {
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(loadingDialog)) {
            loadingDialog = LoadingDialog(
                this@showLoadingExt,
                WindowManager.LayoutParams.MATCH_PARENT,
                (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
            )
        }
        loadingDialog?.apply {
            loadingBean?.let {
                setDialogType(it.type)
                if (it.type == 2) {
                    setIcon(it.icon)
                    setContent(it.content)
                }
                setDialogBg(it.dialogBg)
            } ?: let {
                setDialogType(3)
                setIcon()
                setDialogBg()
            }
            show()
        }
    }
}
//fun AppCompatActivity.showLoadingExt(message: String = "请求网络中") {
//    if (!this.isFinishing) {
//        if (loadingDialog == null) {
//            loadingDialog = MaterialDialog(this)
//                .cancelable(true)
//                .cancelOnTouchOutside(false)
//                .cornerRadius(12f)
//                .customView(R.layout.layout_custom_progress_dialog_view)
//                .lifecycleOwner(this)
//            loadingDialog?.getCustomView()?.run {
//                this.findViewById<TextView>(R.id.loading_tips).text = message
//                this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList =
//                    SettingUtil.getOneColorStateList(this@showLoadingExt)
//            }
//        }
//        loadingDialog?.show()
//    }
//}

/**
 * 打开等待框
 */
fun Fragment.showLoadingExt(loadingBean: LoadingBean?) {
    activity?.let { fragmentActivity ->
        if (!fragmentActivity.isFinishing) {
            if (PublicPracticalMethodFromKT.ppmfKT.isBlank(loadingDialog)) {
                loadingDialog = LoadingDialog(
                    fragmentActivity,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
                )
            }
            loadingDialog?.apply {
                loadingBean?.let {
                    setDialogType(it.type)
                    if (it.type == 2) {
                        setIcon(it.icon)
                        setContent(it.content)
                    }
                    setDialogBg(it.dialogBg)
                } ?: let {
                    setDialogType(3)
                    setIcon()
                    setDialogBg()
                }
                show()
            }
        }
    }
}

//fun Fragment.showLoadingExt1(message: String = "请求网络中") {
//    activity?.let {
//        if (!it.isFinishing) {
//            if (loadingDialog == null) {
//                loadingDialog = MaterialDialog(it)
//                    .cancelable(true)
//                    .cancelOnTouchOutside(false)
//                    .cornerRadius(12f)
//                    .customView(R.layout.layout_custom_progress_dialog_view)
//                    .lifecycleOwner(this)
//                loadingDialog?.getCustomView()?.run {
//                    this.findViewById<TextView>(R.id.loading_tips).text = message
//                    this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList =
//                        SettingUtil.getOneColorStateList(it)
//                }
//            }
//            loadingDialog?.show()
//        }
//    }
//}

/**
 * 关闭等待框
 */
fun Activity.dismissLoadingExt() {
    loadingDialog?.dismissDialog()
    loadingDialog = null
}

/**
 * 关闭等待框
 */
fun Fragment.dismissLoadingExt() {
    loadingDialog?.dismissDialog()
    loadingDialog = null
}
