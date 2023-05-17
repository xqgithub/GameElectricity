package com.sn.gameelectricity.ui.activity

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.lxj.xpopup.XPopup
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.weight.customview.LabelsView
import com.sn.gameelectricity.databinding.ActivityFeedbackBinding
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick

/**
 * Date:2022/7/5
 * Time:14:32
 * author:dimple
 * 意见反馈页面
 */
class FeedBackActivity : BaseActivity1<PersonalSettingViewModel, ActivityFeedbackBinding>() {


    override fun initView(savedInstanceState: Bundle?) {

        checkNextButton()
        initData()
        initListener()
    }

    private fun initData() {
        val labelList = mutableListOf<String>()
        labelList.add("订单问题")
        labelList.add("发货问题")
        labelList.add("支付问题")
        labelList.add("投诉与建议")
        labelList.add("体验问题")
        labelList.add("账号问题")
        mViewBind.labels.apply {
            selectType = LabelsView.SelectType.SINGLE
            setLabels(labelList)
            clearAllSelect()
            setSelects(0)
        }

    }

    private fun initListener() {
        mViewBind.apply {
            etIdea.doOnTextChanged { text, start, before, count ->
                checkNextButton()
            }

            tvConfirm.singleClick {
                if (mViewBind.labels.getSelectLabelDatas<String>().size == 0) {
                    ToastUtil.showCenter("请选择所属分类")
                    return@singleClick
                }
                XPopup.Builder(this@FeedBackActivity)
                    .dismissOnBackPressed(true)
                    .dismissOnTouchOutside(false)
                    .isDestroyOnDismiss(true)
                    .asConfirm(
                        "", "非常感谢您的反馈和建议，我们会尽快处理哦～",
                        "", "确定",
                        { finish() }, null, true
                    ).show()
            }

            ivBack.singleClick {
                finish()
            }
        }
    }

    /**
     * 检查是否可以点击确认按钮
     */
    private fun checkNextButton() {
        mViewBind.apply {
            var next_bg_color = "#66EF874E"
            val labelsList = mViewBind.labels.selectLabels
            if (etIdea.text.toString().trim().isNotEmpty()) {
                tvConfirm.isEnabled = true
                next_bg_color = "#EF874E"
            } else {
                tvConfirm.isEnabled = false
            }
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvConfirm,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@FeedBackActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@FeedBackActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@FeedBackActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@FeedBackActivity, 24f, true)
                    .toFloat(),
                null,
                next_bg_color
            )
        }
    }
}