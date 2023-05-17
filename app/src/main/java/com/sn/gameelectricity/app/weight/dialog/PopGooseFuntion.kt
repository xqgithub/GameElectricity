package com.sn.gameelectricity.app.weight.dialog

import android.content.Context
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.PositionPopupView
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogSelectionToolTipBinding
import com.sn.gameelectricity.databinding.PopupGooseFunctionBinding
import kotlinx.android.synthetic.main.popup_goose_function.view.*
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.inflateBinding
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick

/**
 * Date:2022/5/18
 * Time:14:44
 * author:dimple
 * 鹅动画 弹出框
 */
class PopGooseFuntion(context: Context) : PositionPopupView(context) {

    override fun getImplLayoutId(): Int {
        return R.layout.popup_goose_function
    }

    fun feedOnClick(onCallBack: () -> Unit) {
        iv_feed.singleClick {
            onCallBack()
        }
    }

    fun eggCollectionOnclick(onCallBack: () -> Unit) {
        iv_egg_collection.singleClick {
            onCallBack()
        }
    }


}