package com.sn.gameelectricity.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.MaxTextTwoLengthFilter
import com.sn.gameelectricity.databinding.ActivityEditUserNicknameBinding
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick

/**
 * Date:2022/5/16
 * Time:20:26
 * author:dimple
 * 编写用户昵称页面
 */
class EditUserNickNameActivity :
    BaseActivity1<PersonalSettingViewModel, ActivityEditUserNicknameBinding>() {

    val nickname by lazy {
        intent?.getStringExtra("nickname")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            etNickname.text = Editable.Factory.getInstance().newEditable(nickname)
            etNickname.filters = arrayOf(MaxTextTwoLengthFilter(this@EditUserNickNameActivity, 24))

            ivBack.singleClick {
                finish()
            }

            ivClearnickname.singleClick {
                etNickname.text = Editable.Factory.getInstance().newEditable("")
            }
            tvConfirm.singleClick {
                mViewModel.userEdit(
                    PersonalInfoEditActivity.PersonalInfoEditType.nickName,
                    etNickname.text.toString().trim()
                ) { isSuccess, msg ->
                    if (isSuccess) {
                        CacheUtil.getUser()?.let {
                            it.nickName = etNickname.text.toString().trim()
                            CacheUtil.setUser(it)
                        }
                        val intent =
                            Intent(
                                this@EditUserNickNameActivity,
                                PersonalInfoEditActivity::class.java
                            )
                        intent.putExtra("nickname", etNickname.text.toString().trim())
                        setResult(ConfigConstants.CONSTANT.RESULTCODE_NICKNAME, intent)
                        finish()
                    }
                    ToastUtil.showCenter(msg)
                }
            }
        }
    }
}