package com.sn.gameelectricity.ui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.util.permission.PermissionRequestManager
import com.sn.gameelectricity.databinding.ActivityPersonalInfoEditBinding
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.util.*
import me.shaohui.bottomdialog.BottomDialog
import singleClick
import java.text.SimpleDateFormat

/**
 * Date:2022/5/16
 * Time:20:06
 * author:dimple
 * 个人信息编辑页面
 */
class PersonalInfoEditActivity :
    BaseActivity1<PersonalSettingViewModel, ActivityPersonalInfoEditBinding>() {

    //性别 0 保密 1男 2女
    private var gender: Int = 0

    //日期选择器
    private var pvCustomTime: TimePickerView? = null
    private var mYear = -1
    private var mMonth = -1
    private var mDay = -1


    enum class PersonalInfoEditType {
        avatarurl,
        birthday,
        gender,
        idCard,
        mobile,
        nickName,
        realName
    }


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            tvGenderValue.text = when (CacheUtil.getUser()?.gender) {
                1 -> {
                    "男"
                }
                2 -> {
                    "女"
                }
                else -> {
                    "保密"
                }
            }
            tvNicknameValue.text = PublicPracticalMethodFromKT.ppmfKT.textViewShowLimitedLength(
                CacheUtil.getUser()?.nickName!!,
                24
            )
            tvBirthdayValue.text = CacheUtil.getUser()?.birthday
            sivAvatar.load(
                CacheUtil.getUser()?.avatarUrl,
                R.drawable.default_user_avatar,
                R.drawable.default_user_avatar
            )

            ivBack.singleClick {
                finish()
            }

            clNickname.singleClick {
                with(Bundle()) {
                    putString("nickname", CacheUtil.getUser()?.nickName)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump2(
                        this@PersonalInfoEditActivity,
                        EditUserNickNameActivity::class.java,
                        this,
                        false,
                        -1, -1,
                        -1, startActivityLauncher
                    )
                }
            }

            clGender.singleClick {
                with(BottomDialog.create(supportFragmentManager)) {
                    setViewListener {
                        val tv_man = it.findViewById<TextView>(R.id.tv_man)
                        val tv_women = it.findViewById<TextView>(R.id.tv_women)
                        val tv_cancel = it.findViewById<TextView>(R.id.tv_cancel)
                        val cl_main = it.findViewById<ConstraintLayout>(R.id.cl_main)

                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            cl_main,
                            -1,
                            "",
                            ScreenTools.getInstance()
                                .dp2px(this@PersonalInfoEditActivity, 20f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@PersonalInfoEditActivity, 20f, true)
                                .toFloat(),
                            0f,
                            0f,
                            null,
                            "#F7F9FE"
                        )

                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            tv_man,
                            -1,
                            "",
                            ScreenTools.getInstance()
                                .dp2px(this@PersonalInfoEditActivity, 20f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@PersonalInfoEditActivity, 20f, true)
                                .toFloat(),
                            0f,
                            0f,
                            null,
                            "#ffffff"
                        )

                        tv_man.singleClick {
                            gender = 1
                            changeGender(gender)
                            dismiss()
                        }
                        tv_women.singleClick {
                            gender = 2
                            changeGender(gender)
                            dismiss()
                        }
                        tv_cancel.singleClick {
                            dismiss()
                        }
                    }.setLayoutRes(R.layout.dialog_bottom_update_gender).setCancelOutside(false)
                        .show()
                }
            }

            clBirthday.singleClick {
                initCustomTimePicker()
                pvCustomTime?.show()
            }

            clAvatar.singleClick {
                val dialog = BottomDialog.create(supportFragmentManager)
                dialog.setViewListener {
                    initBottomView(dialog, it)
                }.setLayoutRes(R.layout.dialog_bottom_open_photo_album)
                    .setCancelOutside(false)
                    .show()
            }
        }
    }

    // 控制头像
    private fun initBottomView(dialog: BottomDialog, v: View) {
        val mCancelTv = v.findViewById<TextView>(R.id.mCancelTv)
        val mAlbumTv = v.findViewById<TextView>(R.id.mAlbumTv)
        val mTakePhotosTv = v.findViewById<TextView>(R.id.mTakePhotosTv)

        mAlbumTv.singleClick {
            dialog.dismiss()
            PermissionRequestManager.request(
                this@PersonalInfoEditActivity,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .toSetting()
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        PictureSelector.create(this@PersonalInfoEditActivity)
                            .openGallery(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .maxSelectNum(1)
                            .isPreviewEggs(true)
                            .isCamera(false)
                            .isCompress(true)
                            .minimumCompressSize(100) // 小于多少kb的图片不压缩
                            .forResult(ConfigConstants.CONSTANT.RESULTCODE_PHONEALBUM)
                    }
                }
        }

        mTakePhotosTv.singleClick {
            dialog.dismiss()
            PermissionRequestManager.request(
                this@PersonalInfoEditActivity,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .toSetting()
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        PictureSelector.create(this@PersonalInfoEditActivity)
                            .openCamera(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .maxSelectNum(1)
                            .isPreviewEggs(true)
                            .isCamera(false)
                            .isCompress(true)
                            .minimumCompressSize(100) // 小于多少kb的图片不压缩
                            .forResult(ConfigConstants.CONSTANT.RESULTCODE_PHOTOGRAPH)
                    }
                }
        }




        mCancelTv.singleClick {
            dialog.dismiss()
        }


    }


    private fun changeGender(gender: Int) {
        mViewModel.userEdit(PersonalInfoEditType.gender, gender) { isSuccess, msg ->
            if (isSuccess) {
                if (gender == 2) {
                    mViewBind.tvGenderValue.text = "女"
                } else if (gender == 1) {
                    mViewBind.tvGenderValue.text = "男"
                }

                CacheUtil.getUser()?.let {
                    it.gender = gender
                    CacheUtil.setUser(it)
                }
            }
            ToastUtil.showCenter(msg)
        }
    }

    /**
     * 初始化时间选择器
     */
    fun initCustomTimePicker() {
        pvCustomTime = null
        var selectedDate: java.util.Calendar = java.util.Calendar.getInstance() //系统当前时间

        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(
                mViewBind.tvBirthdayValue.text.toString().trim()
            )
        ) {
            mViewBind.tvBirthdayValue.text.toString().trim().apply {
                this.split(".").apply {
                    mYear = this[0].toInt()
                    mMonth = this[1].toInt()
                    mDay = this[2].toInt()
                }
            }
        } else {
            mYear = selectedDate.get(java.util.Calendar.YEAR)
            mMonth = selectedDate.get(java.util.Calendar.MONTH) + 1
            mDay = selectedDate.get(java.util.Calendar.DAY_OF_MONTH)
        }

        selectedDate.set(mYear, mMonth - 1, mDay)
        val startDate = java.util.Calendar.getInstance()
        startDate.set(1900, 0, 1)
        val endDate = java.util.Calendar.getInstance()
        endDate.set(2042, 11, 31)
        //时间选择器 ，自定义布局
        pvCustomTime = TimePickerBuilder(this@PersonalInfoEditActivity) { date, v -> //选中事件回调
            val format: String = SimpleDateFormat("yyyy-MM-dd").format(date)
            format.split("-").apply {
                mYear = this[0].toInt()
                mMonth = this[1].toInt()
                mDay = this[2].toInt()
            }
            mViewModel.userEdit(
                PersonalInfoEditType.birthday,
                "${mYear}.${mMonth}.${mDay}"
            ) { isSuccess, msg ->
                mViewBind.tvBirthdayValue.text = "${mYear}.${mMonth}.${mDay}"
                CacheUtil.getUser()?.let {
                    it.birthday = "${mYear}.${mMonth}.${mDay}"
                    CacheUtil.setUser(it)
                }
                ToastUtil.showCenter(msg)
            }
        }.setDate(selectedDate)
            .setRangDate(startDate, endDate)
            .setLayoutRes(R.layout.view_picker_time) { v ->
                val cl_main: ConstraintLayout = v.findViewById(R.id.cl_main) as ConstraintLayout
                val tv_confirm: TextView = v.findViewById(R.id.tv_confirm) as TextView
                val tv_cancel: TextView = v.findViewById(R.id.tv_cancel) as TextView
                val tv_month_day: TextView = v.findViewById(R.id.tv_month_day) as TextView
                val iv_close: ImageView = v.findViewById(R.id.iv_close) as ImageView
                //设置背景
                PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                    cl_main,
                    -1,
                    "",
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 16f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 16f, true)
                        .toFloat(),
                    0f,
                    0f,
                    null,
                    "#FFFFFF"
                )
                PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                    tv_cancel, -1, "",
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    null, "#ECF0F8"
                )
                PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                    tv_confirm, -1, "",
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    ScreenTools.getInstance().dp2px(this@PersonalInfoEditActivity, 29f, true)
                        .toFloat(),
                    GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
                )

                tv_month_day.text = "${mYear}年${mMonth}月${mDay}日"

                tv_confirm.setOnClickListener {
                    pvCustomTime!!.returnData()
                    pvCustomTime!!.dismiss()
                }
                tv_cancel.setOnClickListener {
                    pvCustomTime!!.dismiss()
                }
                iv_close.setOnClickListener {
                    pvCustomTime!!.dismiss()
                }
            }
            .setContentTextSize(16)
            .setType(booleanArrayOf(true, true, true, false, false, false))
            .setLabel("", "", "", "时", "分", "秒")
            .setLineSpacingMultiplier(20f)
            .setTextXOffset(0, 0, 0, 40, 0, -40)
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setDividerColor(Color.WHITE)
            .build()
    }


    /**
     * activity 回调
     */
    private val startActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == ConfigConstants.CONSTANT.RESULTCODE_NICKNAME) {
                it.data?.let { _intent ->
                    mViewBind.tvNicknameValue.text =
                        PublicPracticalMethodFromKT.ppmfKT.textViewShowLimitedLength(
                            _intent.getStringExtra("nickname"), 24
                        )
                }
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == ConfigConstants.CONSTANT.RESULTCODE_PHONEALBUM ||
                    requestCode == ConfigConstants.CONSTANT.RESULTCODE_PHOTOGRAPH)
        ) {
            val localMedia = PictureSelector.obtainMultipleResult(data)
            if (localMedia != null && localMedia.size > 0) {
                mViewModel.apply {
                    cosCredential {
                        val disposable =
                            uploadAvatar(this@PersonalInfoEditActivity, localMedia, it)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ piclist ->
                                    piclist.forEach {
                                        LogUtils.iTag(
                                            ConfigConstants.CONSTANT.TAG_ALL,
                                            "图片地址 =-= $it"
                                        )
                                        userEdit(
                                            PersonalInfoEditType.avatarurl,
                                            it
                                        ) { isSuccess, msg ->
                                            if (isSuccess) {
                                                mViewBind.sivAvatar.load(
                                                    it,
                                                    R.drawable.default_user_avatar,
                                                    R.drawable.default_user_avatar
                                                )
                                            }
                                            ToastUtil.showCenter(msg)
                                        }
                                    }
                                }, {
                                    ToastUtil.showCenter("发布失败")
                                })
                        saveDisposable(disposable)
                    }
                }
            }
        }
    }
}