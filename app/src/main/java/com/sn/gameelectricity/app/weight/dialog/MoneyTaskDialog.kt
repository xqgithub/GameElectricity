package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.SharedpreferencesUtil
import com.sn.gameelectricity.app.weight.customview.CountdownProgressBar
import com.sn.gameelectricity.data.model.bean.GameBuildSceneBean
import com.sn.gameelectricity.data.model.bean.PayloadGameTaskVO
import com.sn.gameelectricity.databinding.DialogMoneyTaskBinding
import com.sn.gameelectricity.ui.activity.BrowseRewardActivity
import com.sn.gameelectricity.ui.activity.PrizePoolActivity
import com.sn.gameelectricity.ui.activity.ShowPhoneActivity
import com.sn.gameelectricity.ui.fragment.money.MoneyFragment
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.*
import org.greenrobot.eventbus.EventBus
import singleClick

/**
 * Date:2022/5/18
 * Time:19:36
 * author:dimple
 * 游戏任务弹框
 */
class MoneyTaskDialog @JvmOverloads constructor(
    var mContext: Context,
    val moneyViewModel: MoneyViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 265f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogMoneyTaskBinding>()

    private var countDownTimer: CountDownTimer? = null


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
        layoutParams.gravity = Gravity.CENTER
//        window!!.setWindowAnimations(R.style.BottomShowAnimation)
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
//        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
//            mBinding.clBg, -1, "",
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            null, "#FFF6F0"
//        )
//
//        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
//            mBinding.clContent, -1, "",
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            null, "#ffffff"
//        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvCancel, ScreenTools.getInstance().dp2px(mContext, 1f, true), "#EF874E",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            null, "#00000000"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvSure, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvPrizesNumber, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            null, "#FFF6F0"
        )

    }

    /**
     * 设置数据
     */
    fun setData(
        buildTask: List<PayloadGameTaskVO>,
        buildTaskType: MoneyFragment.BuildTaskType,
        taskNumber: Int,
        gameBuildSceneBean: GameBuildSceneBean?
    ) {
        val bean = buildTask[taskNumber]
        setTitle(
            when (buildTaskType) {
                MoneyFragment.BuildTaskType.Makeups -> {
                    "美妆任务"
                }
                MoneyFragment.BuildTaskType.Leisure -> {
                    "休闲任务"
                }
                MoneyFragment.BuildTaskType.MotherBaby -> {
                    "母婴任务"
                }
                MoneyFragment.BuildTaskType.Digital -> {
                    "数码任务"
                }
            }
        )
        setIcon(
            when (buildTaskType) {
                MoneyFragment.BuildTaskType.Makeups -> {
                    R.drawable.ge_money_dialog_makeups
                }
                MoneyFragment.BuildTaskType.Leisure -> {
                    R.drawable.ge_money_dialog_leisure
                }
                MoneyFragment.BuildTaskType.MotherBaby -> {
                    R.drawable.ge_money_dialog_mother_baby
                }
                MoneyFragment.BuildTaskType.Digital -> {
                    R.drawable.ge_money_dialog_digital
                }
            }
        )
        setTitle2(dataNullConvertToString(bean.taskName) as String)
        setContent(dataNullConvertToString(bean.taskContent) as String)
//        mBinding.tvTask.text = dataNullConvertToString(bean.taskTargetContent) as String
        PublicPracticalMethodFromKT.ppmfKT.showHTMLContent(
            mBinding.tvTask,
            dataNullConvertToString(bean.taskTargetContent) as String
        )


        if (bean.requestRewardInfoVOList.size == 1) {
            if (bean.requestRewardInfoVOList[0].rewardType == 1) {
                setTaskPic(R.drawable.ge_money_task_placeholder_gold)
            } else {
                setTaskPic(R.drawable.ge_money_task_placeholder_feed)
            }
            mBinding.tvPrizesNumber.text =
                "x${bean.requestRewardInfoVOList[0].rewardNumber}"
        } else {
            setTaskPic(R.drawable.ge_money_task_placeholder_gold_feed)
            var gold_num = 0
            var feed_num = 0
            bean.requestRewardInfoVOList.forEach {
                if (it.rewardType == 1) {
                    gold_num = dataNullConvertToInt(it.rewardNumber) as Int
                } else if (it.rewardType == 3) {
                    feed_num = dataNullConvertToInt(it.rewardNumber) as Int
                }
            }
            mBinding.tvPrizesNumber.text =
                "金币x${gold_num} 饲料x${feed_num}"
        }

        mBinding.tvCancel.apply {
            visibility =
                if (dataNullConvertToInt(bean.taskType) as Int == 7) {
                    View.GONE
                } else {
                    if (dataNullConvertToInt(bean.completeStatus) as Int == 3) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }
//            if (dataNullConvertToInt(bean.completeStatus) as Int == 3) View.GONE else View.VISIBLE
            text =
                if (dataNullConvertToInt(bean.completeStatus) as Int == 0 && dataNullConvertToInt(
                        bean.taskType
                    ) as Int == 3
                ) "放弃" else "换一换"
            setTextColor(Color.parseColor("#EF874E"))
            textSize = 14f
            singleClick {
                if (mBinding.tvCancel.text == "换一换" || mBinding.tvCancel.text == "放弃") {
                    moneyViewModel.giveUpGameTask(dataNullConvertToInt(bean.id) as Int) { isSuccess, msg ->
                        if (isSuccess) {
                            if (mBinding.tvCancel.text == "换一换") {
                                changeTasks(
                                    taskNumber,
                                    buildTask,
                                    buildTaskType,
                                    gameBuildSceneBean
                                )
                            } else {
                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
                                        this@MoneyTaskDialog
                                    )
                                )
                                ToastUtil.showCenter(msg)
                            }
                        } else {
                            ToastUtil.showCenter(msg)
                        }
                    }

                }
//                else if (mBinding.tvCancel.text == "放弃") {
//                    moneyViewModel.giveUpGameTask(dataNullConvertToInt(bean.id) as Int) { isSuccess, msg ->
//                        if (isSuccess) {
//                            EventBus.getDefault().post(
//                                UniversalEvent(
//                                    UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
//                                    this@MoneyTaskDialog
//                                )
//                            )
//                        }
//                        ToastUtil.showCenter(msg)
//                    }
//                }
            }
        }

        mBinding.tvSure.apply {
            visibility = View.VISIBLE
            var start_color = ""
            var end_color = ""
            text =
                if (dataNullConvertToInt(bean.completeStatus) as Int == 0
                    && dataNullConvertToInt(bean.taskType) as Int == 3
                ) {
                    start_color = "#33F19B3F"
                    end_color = "#33ED7A57"
                    isEnabled = false
                    "进行中"
                } else if (dataNullConvertToInt(bean.taskType) as Int == 7 &&
                    dataNullConvertToInt(bean.completeStatus) as Int != 3
                ) {
                    start_color = "#33F19B3F"
                    end_color = "#33ED7A57"
                    isEnabled = false
                    "领取奖励"
                } else {
                    start_color = "#F19B3F"
                    end_color = "#ED7A57"
                    isEnabled = true
                    if (mBinding.tvCancel.visibility == View.GONE) {
                        "领取奖励"
                    } else {
                        "去完成"
                    }
                }
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                mBinding.tvSure, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                GradientDrawable.Orientation.LEFT_RIGHT, start_color, end_color
            )
            setTextColor(Color.parseColor("#FFFFFF"))
            textSize = 14f
            singleClick {
                if (mBinding.tvSure.text == "去完成") {
                    moneyViewModel.receiveTask(dataNullConvertToInt(bean.id) as Int) { isSuccess, msg ->
                        if (isSuccess) {
                            if (dataNullConvertToInt(bean.taskType) as Int == 1) {//浏览类
                                with(Bundle()) {
                                    putSerializable("PayloadGameTaskVO", bean)
                                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                        mContext as AppCompatActivity,
                                        BrowseRewardActivity::class.java, this, false
                                    )

                                    EventBus.getDefault().post(
                                        UniversalEvent(
                                            UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
                                            this@MoneyTaskDialog
                                        )
                                    )
                                }
                            } else if (dataNullConvertToInt(bean.taskType) as Int == 3) {//打工类
                                moneyViewModel.startTimingTask(
                                    dataNullConvertToLong(bean.receiveTime) as Long,
                                    dataNullConvertToLong(bean.taskEndTime) as Long,
                                    mBinding.pbCountdown,
                                    buildTaskType
                                ) {
                                }
                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
                                        this@MoneyTaskDialog
                                    )
                                )
                            } else {
                                val _taskType = dataNullConvertToInt(bean.taskType) as Int
                                val _eventType = dataNullConvertToInt(bean.eventType) as Int
//                                ToastUtil.showCenter("领取任务成功")
                                if (_taskType == 4 && _eventType == 1) {//开启扭蛋
                                    EventBus.getDefault().post(
                                        EventBus.getDefault().post(
                                            UniversalEvent(
                                                UniversalEvent.EVENT_JUMP_HOMEFRAGMENT2,
                                                null
                                            )
                                        )
                                    )
                                } else if ((_taskType == 2 &&
                                            _eventType == 2) || (_taskType == 4 &&
                                            (_eventType == 2 || _eventType == 3 || _eventType == 4))
                                ) {//积分兑换,金币抵扣,半价购
                                    var topicId = -99
                                    topicId = when (buildTaskType) {
                                        MoneyFragment.BuildTaskType.Makeups -> {
                                            gameBuildSceneBean?.build_slot_2!!.topicId
                                        }
                                        MoneyFragment.BuildTaskType.Leisure -> {
                                            gameBuildSceneBean?.build_slot_3!!.topicId
                                        }
                                        MoneyFragment.BuildTaskType.MotherBaby -> {
                                            gameBuildSceneBean?.build_slot_4!!.topicId
                                        }
                                        MoneyFragment.BuildTaskType.Digital -> {
                                            gameBuildSceneBean?.build_slot_5!!.topicId
                                        }
                                    }
                                    moneyViewModel.doTaskJump(topicId)
                                } else if (_taskType == 4 && _eventType == 5) {//刷新扭蛋奖池
                                    with(Bundle()) {
                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                            mContext as AppCompatActivity,
                                            PrizePoolActivity::class.java, this, false
                                        )
                                    }
                                } else if (
                                    (_taskType == 2 && _eventType == 1) || (_taskType == 5 &&
                                            (_eventType == 1 || _eventType == 2 || _eventType == 3))
                                ) {//邀请好友，鼓励,雇佣,投食,
                                    EventBus.getDefault().post(
                                        UniversalEvent(
                                            UniversalEvent.EVENT_OPEN_DYNAMICDIALOG,
                                            null
                                        )
                                    )
                                } else if (_taskType == 5 && _eventType == 4) {//逛一逛

                                } else if (_taskType == 6 && (_eventType == 1 || _eventType == 2)) {//收蛋,喂养
                                    EventBus.getDefault().post(
                                        UniversalEvent(
                                            UniversalEvent.EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION,
                                            null
                                        )
                                    )
                                }

                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
                                        this@MoneyTaskDialog
                                    )
                                )
                            }
                        }
                    }
                } else if (mBinding.tvSure.text == "领取奖励") {
                    moneyViewModel.receiveGameTaskRewards(
                        dataNullConvertToInt(bean.id) as Int,
                        dataNullConvertToInt(bean.userReceiveId) as Int
                    ) { isSuccess, msg ->
                        if (isSuccess) {
                            SharedpreferencesUtil.putInt(
                                App.instance,
                                when (buildTaskType) {
                                    MoneyFragment.BuildTaskType.Makeups -> {
                                        ConfigConstants.CONSTANT.MAKEUPS_TASKNUMBER
                                    }
                                    MoneyFragment.BuildTaskType.Leisure -> {
                                        ConfigConstants.CONSTANT.LEISURE_TASKNUMBER
                                    }
                                    MoneyFragment.BuildTaskType.MotherBaby -> {
                                        ConfigConstants.CONSTANT.MOTHERBABY_TASKNUMBER
                                    }
                                    MoneyFragment.BuildTaskType.Digital -> {
                                        ConfigConstants.CONSTANT.DIGITAL_TASKNUMBER
                                    }
                                },
                                if (taskNumber + 1 < buildTask.size) taskNumber + 1 else 0
                            )

                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
                                    this@MoneyTaskDialog
                                )
                            )
                        }
                        ToastUtil.showCenter(msg)
                    }
                }
            }
        }

        if (dataNullConvertToInt(bean.taskType) as Int == 3 &&
            dataNullConvertToInt(bean.completeStatus) as Int == 0
        ) {
            mBinding.pbCountdown.visibility = View.VISIBLE
            if (mBinding.tvSure.text == "进行中") {
                moneyViewModel.startTimingTask(
                    dataNullConvertToLong(bean.receiveTime) as Long,
                    dataNullConvertToLong(bean.taskEndTime) as Long,
                    mBinding.pbCountdown,
                    buildTaskType
                ) {
                    EventBus.getDefault().post(
                        UniversalEvent(
                            UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS,
                            this@MoneyTaskDialog
                        )
                    )
                }
            }
        } else {
            mBinding.pbCountdown.visibility = View.GONE
        }


    }

    /**
     * 换一换 任务
     */
    fun changeTasks(
        taskNumber: Int,
        buildTask: List<PayloadGameTaskVO>,
        buildTaskType: MoneyFragment.BuildTaskType,
        gameBuildSceneBean: GameBuildSceneBean?
    ) {
        var taskNumber_after = taskNumber + 1
        if (taskNumber_after < buildTask.size) {
            val bean = buildTask[taskNumber_after]
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.completeStatus)) {
                if (dataNullConvertToInt(
                        bean.completeStatus
                    ) as Int == 0 || dataNullConvertToInt(
                        bean.completeStatus
                    ) as Int == 1 || dataNullConvertToInt(
                        bean.completeStatus
                    ) as Int == 3
                ) {
                    changeTasks(taskNumber_after, buildTask, buildTaskType, gameBuildSceneBean)
                    return
                }
            }
        } else {
            changeTasks(-1, buildTask, buildTaskType, gameBuildSceneBean)
            return
        }
        SharedpreferencesUtil.putInt(
            App.instance,
            when (buildTaskType) {
                MoneyFragment.BuildTaskType.Makeups -> {
                    ConfigConstants.CONSTANT.MAKEUPS_TASKNUMBER
                }
                MoneyFragment.BuildTaskType.Leisure -> {
                    ConfigConstants.CONSTANT.LEISURE_TASKNUMBER
                }
                MoneyFragment.BuildTaskType.MotherBaby -> {
                    ConfigConstants.CONSTANT.MOTHERBABY_TASKNUMBER
                }
                MoneyFragment.BuildTaskType.Digital -> {
                    ConfigConstants.CONSTANT.DIGITAL_TASKNUMBER
                }
            },
            taskNumber_after
        )
        setData(buildTask, buildTaskType, taskNumber_after, gameBuildSceneBean)
    }

    /**
     * 设置标题内容
     */
    fun setTitle(
        content: String = "",
        textcolor: String = "#ffffff",
        textsize: Float = 16f
    ) {
        mBinding.tvTitle.apply {
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }
    }

    /**
     * 设置图像
     */
    fun setIcon(picRes: Int) {
        mBinding.ivIcon.setImageDrawable(ContextCompat.getDrawable(mContext, picRes))
    }

    /**
     * 设置任务图片
     */
    fun setTaskPic(picRes: Int) {
        mBinding.sivTask.setImageDrawable(ContextCompat.getDrawable(mContext, picRes))
    }


    /**
     * 设置任务标题
     */
    fun setTitle2(
        content: String = "",
        textcolor: String = "#57493B",
        textsize: Float = 16f
    ) {
        mBinding.tvTitle2.apply {
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }
    }

    /**
     * 设置任务内容
     */
    fun setContent(
        content: String = "",
        textcolor: String = "#6A7079",
        textsize: Float = 11f,
    ) {
        mBinding.atvIntroduce.text = content
        val a = mBinding.atvIntroduce.text.toString()
        mBinding.atvIntroduce.apply {
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }

    }


    /**
     * 关闭按钮点击事件
     */
    fun onClickIvClose(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            dismissDialog()
        }
        onCallBack()
    }


    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
            countDownTimer?.let {
                it.cancel()
            }

            if (CacheUtil.getUser()?.boolGuide!!) {
                moneyViewModel.todayBuildTaskList()
                moneyViewModel.eggFeedQuantity()
                App.appViewModelInstance.getuserInfo()
            }
        }
    }


}