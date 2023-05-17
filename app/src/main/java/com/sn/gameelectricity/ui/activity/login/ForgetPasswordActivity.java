package com.sn.gameelectricity.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sn.gameelectricity.R;


/**
 * 找回密码
 */
public class ForgetPasswordActivity extends Base2Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        setupData();
    }

    private void setupData() {
        ForgetPwdPhoneFragment fragment = new ForgetPwdPhoneFragment();
        Intent data = getIntent();
        if (null != data) {
//            String phone = data.getStringExtra(Consts.KEY_USER_PHONE);
//            String langCode = data.getStringExtra(Consts.KEY_USER_LANGCODE);
//            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(langCode)) {
//                Bundle args = new Bundle();
//                args.putString(Consts.KEY_USER_PHONE, phone);
//                args.putString(Consts.KEY_USER_LANGCODE, langCode);
//                fragment.setArguments(args);
//            }
        }
        initializeWithFragment(R.id.layout_fragment_container, fragment, ForgetPwdPhoneFragment.class.getSimpleName());
    }
}