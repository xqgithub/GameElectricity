package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.databinding.DialogGameFriendBinding
import com.sn.gameelectricity.ui.adapter.GameFriendAdapter
import com.sn.gameelectricity.ui.adapter.OrderListAdapter
import com.sn.gameelectricity.viewmodel.state.MainViewModel
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.dataNullConvertToInt
import me.jessyan.autosize.AutoSizeConfig
import singleClick


/**
 * Date:2022/5/19
 * Time:10:51
 * author:dimple
 * 游戏好友和仇人列表 弹框
 */
class GameFriendListDialog @JvmOverloads constructor(
    var mContext: Context,
    val moneyViewModel: MoneyViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogGameFriendBinding>()

    private lateinit var gameFriendAdapter: GameFriendAdapter

    private var pageNum: Int = 1

    //默认显示好人选项卡
    private var relationType = RelationType.FRIEND

    enum class RelationType {
        FRIEND, ENEMMY
    }

    val contributeDialog by lazy {
        ContributeDialog(
            mContext, moneyViewModel,
            ScreenTools.getInstance().dp2px(mContext, 375f, true),
            (0.85 * AutoSizeConfig.getInstance().screenHeight).toInt()
        )
    }
    val shareDialog by lazy {
        ShareDialog(
            mContext,
            ScreenTools.getInstance().dp2px(mContext, 375f, true)
        )
    }

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()
    }

    override fun show() {
//        PublicPracticalMethodFromKT.ppmfKT.hideNavigationBar(window!!)
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
        dataEcho()



        mBinding.apply {
            gameFriendAdapter = GameFriendAdapter(mContext, moneyViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = gameFriendAdapter

                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(mContext, 0f, true),
                            ScreenTools.getInstance().dp2px(mContext, 0f, true),
                            ScreenTools.getInstance().dp2px(mContext, 0f, true),
                            ScreenTools.getInstance().dp2px(mContext, 10f, true)
                        )
                )

                isRefreshEnabled = false
                isLoadMoreEnabled = true
                setOnLoadMoreListener {
                    pageNum++
                    moneyViewModel.friendList(pageNum)
//                    moneyViewModel.eventList(pageNum)
                }

                setOnItemChildClickListener { view, position ->
                    when (view.id) {
                        R.id.iv_receive -> {
                            val bean = gameFriendAdapter.getItemData(position)
                            moneyViewModel.receiveInvitationReward(dataNullConvertToInt(bean.id) as Int) { isSuccess, msg ->
                                if (isSuccess) {
                                    if (dataNullConvertToInt(bean.requestRewardInfoVO.rewardType) as Int == 1) {//金币
                                        ToastUtil.showReceiveInvitationReward(
                                            R.drawable.ge_me_gold,
                                            dataNullConvertToInt(bean.requestRewardInfoVO.rewardNumber) as Int
                                        )
                                    } else {//饲料
                                        ToastUtil.showReceiveInvitationReward(
                                            R.drawable.ge_money_food,
                                            dataNullConvertToInt(bean.requestRewardInfoVO.rewardNumber) as Int
                                        )
                                    }
                                    bean.boolReceiveReward = 1
                                    gameFriendAdapter.notifyItemChanged(position)
                                } else {
                                    ToastUtil.showCenter(msg)
                                }
                            }
                        }
                    }
                }
                clearAnimation()
                setLoadingMoreBottomHeight(16f)
            }
        }
        setTabStatus(relationType)
        moneyViewModel.friendList(pageNum)
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
            mBinding.clContent, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            0f,
            0f,
            null, "#FFF6F0"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clFriendEnemy, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 22f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 22f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 22f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 22f, true).toFloat(),
            null, "#FDEDE4"
        )
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clShare, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )


        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvFriend, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )

        mBinding.ituiShare.apply {
            setTextData("邀请好友一起玩", 16f, "#FFFFFF")
            setAvatarDataFromRes(
                R.drawable.ge_money_add,
                ScreenTools.getInstance().dp2px(App.instance, 20f, true),
                ScreenTools.getInstance().dp2px(App.instance, 20f, true)
            )
        }
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
                        GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
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
                pageNum = 1
                moneyViewModel.friendList(pageNum)
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
                        GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
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
                bindEmptyView()
            }
        }
    }


    fun setMyContribution(content1: String, content2: String) {
        mBinding.tatContribution.apply {
            setDescribeText(content1, 14f, "#57493B")
            setNumText(content2, 16f, "#57493B")
            singleClick {
//                ToastUtil.showCenter("准备进入到我的贡献页面")
//                contributeDialog.apply {
//                    closeDialog {
//                        dismissDialog2()
//                    }
//                    show()
//                }
            }
        }
    }


    fun tvFriendOnclick() {
        mBinding.tvFriend.singleClick {
            relationType = RelationType.FRIEND
            setTabStatus(relationType)
        }
    }

    fun tvEnemyOnclick() {
        mBinding.tvEnemy.singleClick {
            relationType = RelationType.ENEMMY
            setTabStatus(relationType)
        }
    }

    fun toShare() {
        mBinding.clShare.singleClick {
            shareDialog.apply {
                show()
            }
        }
    }


    /**
     * 数据回显
     */
    private fun dataEcho() {
        moneyViewModel.apply {
            friendListLive.observe((mContext as AppCompatActivity)) { bean ->
                setMyContribution("我的贡献", "${dataNullConvertToInt(bean.contribute) as Int}")
                val beans = bean.payloadUserFriendsVOList
                if (pageNum == 1) {
                    if (beans.isEmpty()) {
                        bindEmptyView()
                    } else {
                        mBinding.recyclerView.removeEmptyView()
                        gameFriendAdapter.setNewData(beans)
                    }
                } else {
                    gameFriendAdapter.addMoreData(beans)
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
                    R.drawable.ge_error2,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent("哦豁，一片荒芜～", 12f, "#57493B")
                changeErrorIconPositionToTOP(
                    (0.3 * recyclerView_height).toInt()
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
            gameFriendAdapter.setNewData(arrayListOf())
        }

    }


    fun setonClickIvClose(onCallBack: () -> Unit) {
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
            relationType = RelationType.FRIEND
        }
    }

    fun dismissDialog2() {
        if (isShowing) {
            dismiss()
            relationType = RelationType.FRIEND
        }
    }


}