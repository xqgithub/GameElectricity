package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import com.sn.gameelectricity.app.weight.customview.CountdownProgressBar
import com.sn.gameelectricity.data.model.bean.GameBuildSceneBean
import com.sn.gameelectricity.data.model.bean.GooseEggsFeedBean
import com.sn.gameelectricity.databinding.DialogManorBinding
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.*
import org.libpag.PAGView
import singleClick

/**
 * Date:2022/5/23
 * Time:14:43
 * author:dimple
 * 庄园弹框
 */
class ManorDialog @JvmOverloads constructor(
    var mContext: Context,
    val mViewModel: MoneyViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog3
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogManorBinding>()

    private var countDownTimer_goose_nest: CountDownTimer? = null
    private var countDownTimer_feed_trough: CountDownTimer? = null

    private var pagview_goose: PAGView? = null
    private var pagview_finger: PAGView? = null

    //鹅蛋数
    private var eggNum = 0

    //鹅蛋最大数
    private var eggNum_Max = 0

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

//        gooseNestTimingTask(
//            ConfigConstants.VARIABLE.cur_time,
//            ConfigConstants.VARIABLE.end_time
//        )
//        FeedTroughTimingTasks(
//            ConfigConstants.VARIABLE.cur_time,
//            ConfigConstants.VARIABLE.end_time
//        )
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
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvNumberAwards,
            ScreenTools.getInstance().dp2px(mContext, 2f, true),
            "#5CFFFFFF",
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            GradientDrawable.Orientation.TOP_BOTTOM,
            "#FFD458", "#FFA52A"
        )

        mBinding.ituiFeed.apply {
            setTaskStatusFromRes(
                R.drawable.ge_manor_feed,
                ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                ScreenTools.getInstance().dp2px(App.instance, 52f, true)
            )
            setTextData("1g", true, 12f)
            changeTextPositionToTOP(ScreenTools.getInstance().dp2px(App.instance, 48f, true))
        }
    }

    /**
     * 初始化鹅的动画
     */
    fun initGooseAnimation(grfBean: GooseEggsFeedBean?) {
        var animationName = "goose_nol.pag"
        if (grfBean?.feedPoolCapacityFlag == 0) animationName = "eating.pag"
        pagview_goose = PagAnimationTools.pagTools.getPagView(
            animationName,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding.rlPagGoose.addView(pagview_goose)
        startGooseAnimation()
    }

    fun changeGooseAnimation(grfBean: GooseEggsFeedBean?) {
        stopFingerAnimation()
        var animationName = "goose_nol.pag"
        if (grfBean?.feedPoolCapacityFlag == 0) animationName = "eating.pag"
        PagAnimationTools.pagTools.changePagView(animationName, pagview_goose!!)
        startGooseAnimation()
    }

    fun startGooseAnimation() {
        mBinding.rlPagGoose.visibility = View.VISIBLE
        pagview_goose?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    fun stopGooseAnimation() {
        mBinding.rlPagGoose.visibility = View.GONE
        pagview_goose?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }


    /**
     * 初始化手指动画
     */
    fun initFingerAnimation(animationName: String) {
        pagview_finger = PagAnimationTools.pagTools.getPagView(
            animationName,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding.rlPagFinger.addView(pagview_finger)
    }

    fun startFingerAnimation() {
        mBinding.rlPagFinger.visibility = View.VISIBLE
        pagview_finger?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    fun stopFingerAnimation() {
        mBinding.rlPagFinger.visibility = View.GONE
        pagview_finger?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    /**
     * 设置遮罩层是否显示
     */
    fun setMaskLayerWhetherDisplay(visibility: Boolean = false) {
        if (visibility) {
            mBinding.clMaskLayer.visibility = View.VISIBLE
            mBinding.clMaskLayer.singleClick { }
        } else {
            mBinding.clMaskLayer.visibility = View.GONE
        }
    }

    /**
     * 设置鹅蛋的数值
     */
    fun setTvNumberAwardsData(num: Int) {
        mBinding.tvNumberAwards.apply {
            if (!CacheUtil.getUser()?.boolGuide!!) {
                text = "x $num"
            } else {
                visibility = if (num > 0) View.VISIBLE else View.GONE
                text = if (num < eggNum_Max) "x $num" else "x $eggNum_Max"
            }
        }
    }

    /**
     * 饲料点击事件
     */
    fun onclickItuiFeed(onCallBack: () -> Unit) {
        if (CacheUtil.getUser()?.boolGuide!!) {
            mBinding.ituiFeed.singleClick {
                onCallBack()
            }
        }
    }

    /**
     * 鹅窝点击事件
     */
    fun onClickivGooseNest(onCallBack: () -> Unit) {
        mBinding.ivGooseNest.singleClick {
            onCallBack()
            if (CacheUtil.getUser()?.boolGuide!!) {
                mViewModel.eggExchange { isSuccess, msg, coinNum, _eggNum ->
                    if (isSuccess) {
                        ToastUtil.showCollectGooseEggRewardsToast(
                            R.drawable.ge_manor_gooseegg,
                            R.drawable.ge_me_gold,
                            _eggNum,
                            coinNum,
                            Gravity.CENTER
                        )
                        eggNum = 0
                        setTvNumberAwardsData(eggNum)


                        countDownTimer_goose_nest.isNotNull({

                        }, {
                            mBinding.pbGooseNest.visibility = View.GONE
                        })
                    } else {
                        ToastUtil.showCenter(msg)
                    }
                }
            }
        }
    }

    /**
     * 设置鹅窝计时任务
     */
    fun gooseNestTimingTask(
        cur_time: Long,
        bean: GameBuildSceneBean,
        grfBean: GooseEggsFeedBean,
        isRepeatTask: Boolean = false
    ) {
        mBinding.apply {
            val start_time = dataNullConvertToLong(grfBean.startTime) as Long / 1000L
            val end_time = dataNullConvertToLong(grfBean.endTime) as Long / 1000L
            val layEggFrequency = dataNullConvertToLong(grfBean.layEggFrequency) as Long / 1000L
            mViewModel.startCountdownTask(cur_time, end_time) {
                val hour = it["hour"]!!.times(60).times(60)
                val minute = it["minute"]!!.times(60)
                val second = it["second"]!!
                val countTime = hour + minute + second
                if (countTime > 0) {
                    pbGooseNest.apply {
                        if (!isRepeatTask) {
                            visibility = View.VISIBLE
                            init(CountdownProgressBar.PageLocation.MoneyFragment)
                            getMagnifierUi().visibility = View.GONE
                            setIcon2FromRes(
                                R.drawable.ge_money_countdown,
                                ScreenTools.getInstance().dp2px(App.instance, 18f, true),
                                ScreenTools.getInstance().dp2px(App.instance, 18f, true)
                            )
                            setPb2Height(ScreenTools.getInstance().dp2px(App.instance, 12f, true))
                        }
                        //多长时间产一个蛋
                        val eggLayingTime = layEggFrequency
                        //总时间
                        val totalTime = feedTroughTotalTime(start_time, end_time)
                        //已经运行的时间
                        val elapsedTime = totalTime - countTime
                        var result: Long

                        if (elapsedTime >= eggLayingTime) {
                            result = elapsedTime % eggLayingTime
                        } else {
                            result = elapsedTime
                        }
                        //设置进度条总值
                        setProgressMax(eggLayingTime.toInt())
                        setProgress(result.toInt())
                        countDownTimer_goose_nest =
                            object : CountDownTimer((eggLayingTime - result) * 1000L, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    visibility = View.VISIBLE
                                    val time = millisUntilFinished / 1000L
                                    val timeleft = eggLayingTime - time

                                    if (eggNum < eggNum_Max) {
                                        setProgress(timeleft.toInt())
                                        setTvText(mViewModel.showCountTimeContent(timeleft))
                                    } else {
                                        setProgress(eggLayingTime.toInt())
                                        setTvText("鹅窝已满")
                                    }
                                }

                                override fun onFinish() {
                                    val time = 0L
                                    eggNum += 1
                                    setTvNumberAwardsData(eggNum)
                                    countDownTimer_goose_nest?.cancel()
                                    setProgress(eggLayingTime.toInt())

                                    if (eggNum < eggNum_Max) {
                                        setTvText(mViewModel.showCountTimeContent(time))
                                    } else {
                                        setTvText("鹅窝已满")
                                    }

                                    gooseNestTimingTask(
                                        System.currentTimeMillis() / 1000L,
                                        bean,
                                        grfBean,
                                        true
                                    )
                                }
                            }
                        countDownTimer_goose_nest?.start()
                    }
                } else {
                    if (eggNum >= eggNum_Max) {
                        pbGooseNest.apply {
                            if (!isRepeatTask) {
                                visibility = View.VISIBLE
                                init(CountdownProgressBar.PageLocation.MoneyFragment)
                                getMagnifierUi().visibility = View.GONE
                                setIcon2FromRes(
                                    R.drawable.ge_money_countdown,
                                    ScreenTools.getInstance().dp2px(App.instance, 18f, true),
                                    ScreenTools.getInstance().dp2px(App.instance, 18f, true)
                                )
                                setPb2Height(
                                    ScreenTools.getInstance().dp2px(App.instance, 12f, true)
                                )
                                setProgressMax(100)
                                setProgress(100)
                                setTvText("鹅窝已满")
                            }
                        }
                    } else {
                        pbGooseNest.visibility = View.GONE
                        countDownTimer_goose_nest?.cancel()
                        countDownTimer_goose_nest = null
                    }
                }
            }
        }
    }


    /**
     * 饲料槽的总时间
     */
    fun feedTroughTotalTime(startTime: Long, endTime: Long): Long {
        var totalTime = 0L
        mViewModel.startCountdownTask(startTime, endTime) {
            val hour = it["hour"]!!.times(60).times(60)
            val minute = it["minute"]!!.times(60)
            val second = it["second"]!!
            totalTime = hour + minute + second
        }
        return totalTime
    }


    /**
     * 设置饲料槽计时任务
     */
    fun FeedTroughTimingTasks(
        cur_time: Long,
        bean: GameBuildSceneBean,
        grfBean: GooseEggsFeedBean
    ) {
        mBinding.apply {
            val startTime = dataNullConvertToLong(grfBean.startTime) as Long / 1000L
            val end_time = dataNullConvertToLong(grfBean.endTime) as Long / 1000L
            mViewModel.startCountdownTask(
                cur_time,
                end_time
            ) {
                val hour = it["hour"]!!.times(60).times(60)
                val minute = it["minute"]!!.times(60)
                val second = it["second"]!!
                val countTime = hour + minute + second

                if (countTime > 0) {
                    pbFeedTrough.apply {
                        init(CountdownProgressBar.PageLocation.MoneyFragment)
                        getMagnifierUi().visibility = View.GONE
                        setIcon2FromRes(
                            R.drawable.ge_money_countdown,
                            ScreenTools.getInstance().dp2px(App.instance, 18f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 18f, true)
                        )
                        setPb2Height(ScreenTools.getInstance().dp2px(App.instance, 12f, true))

                        //设置进度条总值
                        setProgressMax(feedTroughTotalTime(startTime, end_time).toInt())
                        //设置进度条已运行的值
                        var elapsedTime =
                            feedTroughTotalTime(startTime, end_time).toInt() - countTime.toInt()
                        setProgress(
                            elapsedTime
                        )
                        countDownTimer_feed_trough =
                            object : CountDownTimer(countTime * 1000L, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    visibility = View.VISIBLE
                                    val time = millisUntilFinished / 1000L
                                    val timeleft = elapsedTime + countTime - time
                                    setProgress(time.toInt())
                                    setTvText(mViewModel.showCountTimeContent(time))
                                }

                                override fun onFinish() {
                                    visibility = View.GONE
                                    val time = 0L
                                    setProgress(feedTroughTotalTime(startTime, end_time).toInt())
                                    setTvText(mViewModel.showCountTimeContent(time))
                                    ivFeedTrough.load(
                                        bean?.build_slot_6?.buildImg,
                                        R.drawable.ge_manor_feed_trough_empty,
                                        R.drawable.ge_manor_feed_trough_empty
                                    )
                                    countDownTimer_feed_trough?.cancel()
                                    countDownTimer_feed_trough = null
                                }
                            }
                        countDownTimer_feed_trough?.start()
                    }
                }


            }
        }
    }


    /**
     * 设置鹅窝和饲料槽
     */
    fun setGooseNestAndFeedTrough(bean: GameBuildSceneBean?, grfBean: GooseEggsFeedBean?) {
        mBinding.apply {
            grfBean?.let {
                setTvNumberAwardsData(dataNullConvertToInt(it.eggNum) as Int)
                mBinding.ituiFeed.apply {
                    setTaskStatusFromRes(
                        R.drawable.ge_manor_feed,
                        ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                        ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                    )
                    setTextData(
                        if (dataNullConvertToInt(it.totalFeedNum) as Int > 999999) "999999g" else "${
                            dataNullConvertToInt(
                                it.totalFeedNum
                            )
                        }g", true, 12f
                    )

                    changeTextPositionToTOP(
                        ScreenTools.getInstance().dp2px(App.instance, 48f, true)
                    )
                }
                if ((dataNullConvertToInt(it.feedPoolCapacityFlag) as Int) == 0) {
                    //满
                    ivFeedTrough.load(
                        bean?.build_slot_7?.buildImg,
                        R.drawable.ge_manor_feed_trough_full,
                        R.drawable.ge_manor_feed_trough_full
                    )

                } else {
                    //空
                    ivFeedTrough.load(
                        bean?.build_slot_6?.buildImg,
                        R.drawable.ge_manor_feed_trough_empty,
                        R.drawable.ge_manor_feed_trough_empty
                    )
                }

                eggNum = dataNullConvertToInt(grfBean.eggNum) as Int
                val cur_time = System.currentTimeMillis()
                FeedTroughTimingTasks(
                    cur_time / 1000L,
                    bean!!,
                    grfBean
                )
                gooseNestTimingTask(
                    cur_time / 1000L,
                    bean!!,
                    grfBean
                )
            }
            //鹅窝
            ivGooseNest.load(
                bean?.build_slot_9?.buildImg,
                R.drawable.ge_manor_goose_nest,
                R.drawable.ge_manor_goose_nest
            )
        }
    }


    /**
     * 修改鹅的距离
     */
    fun changeGoosePositionToTOP(offsetTop: Int) {
        val layout = ConstraintLayout.LayoutParams(
            ScreenTools.getInstance().dp2px(mContext, 200f, true),
            ScreenTools.getInstance().dp2px(mContext, 200f, true)
        )
        layout.startToStart = R.id.cl_content
        layout.endToEnd = R.id.cl_content
        layout.topToTop = R.id.cl_content
        layout.topMargin = offsetTop
        mBinding.rlPagGoose.layoutParams = layout
    }

    /**
     * 设置鹅蛋最大值
     */
    fun setGooseEggMaxNum(maxNum: Int) {
        this.eggNum_Max = maxNum
    }

    /**
     * 关闭弹框
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
            countDownTimer_goose_nest?.cancel()
            countDownTimer_goose_nest = null

            countDownTimer_feed_trough?.cancel()
            countDownTimer_feed_trough = null

            stopGooseAnimation()
            stopFingerAnimation()



            if (CacheUtil.getUser()?.boolGuide!!) {
                mViewModel.eggFeedQuantity()
                mViewModel.todayBuildTaskList()
                App.appViewModelInstance.getuserInfo()
            }
        }
    }


}