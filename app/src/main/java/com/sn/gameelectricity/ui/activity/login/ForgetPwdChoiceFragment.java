package com.sn.gameelectricity.ui.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.util.CommonUtil;
import com.sn.gameelectricity.data.model.bean.UserInfo;
import com.sn.gameelectricity.viewmodel.request.RequestLoginViewModel;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.hgj.jetpackmvvm.util.ToastUtil;


/**
 * 忘记密码 选择验证方式
 * Created by cyj on 2017/04/06
 */
public class ForgetPwdChoiceFragment extends Base2Fragment {
    ImageView imgBack;
    protected EditText mEtPhone;
    protected EditText mEtCode, mEtCodeAgain;
    protected CheckBox switch_password, switch_passwordagain;
    protected Button mBtnNext;
    private String smsCode;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_forgetpwd_choice, container, false);
        setupView(rootView);
        return rootView;
    }

    @Override
    public void initData() {
        Bundle args = getArguments();
        if (null != args) {
            mEtPhone.setText(args.getString("USER.KEY_PHONE"));
            smsCode = args.getString("USER.KEY_LANGCODE");

        }
    }

    // 初始化控件
    private void setupView(View rootView) {
        imgBack = (ImageView) rootView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            ((Base2Activity) getActivity()).finish();
        });

        //请输入手机号
        mEtPhone = (EditText) rootView.findViewById(R.id.et_login_register_sms_phone);

        //请输入密码
        mEtCode = (EditText) rootView.findViewById(R.id.et_login_password_code);
        //请输入密码 显示隐藏
        switch_password = (CheckBox) rootView.findViewById(R.id.switch_password);
        switch_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEtCode.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                mEtCode.setSelection(mEtCode.getText().length());
            }
        });
        mEtCodeAgain = (EditText) rootView.findViewById(R.id.et_login_password_code2);

        //请输入密码 显示隐藏
        switch_passwordagain = (CheckBox) rootView.findViewById(R.id.switch_password2);
        switch_passwordagain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEtCodeAgain.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                mEtCodeAgain.setSelection(mEtCodeAgain.getText().length());
            }
        });

        //登录
        mBtnNext = (Button) rootView.findViewById(R.id.btn_login_password_next);
        mBtnNext.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(mEtCodeAgain);
            // 点击 确定
            String phone = mEtPhone.getText().toString().trim();
            String password = mEtCode.getText().toString().trim();
            String passwordAgain = mEtCodeAgain.getText().toString().trim();
            if (TextUtils.isEmpty(password) || (!CommonUtil.isLetterDigit(password))) {
                ToastUtil.INSTANCE.showCenter("密码由6-16位字母(区分大小写)和数字组成");
                return;
            }
            if (!password.equals(passwordAgain)) {
                ToastUtil.INSTANCE.showCenter("密码不一致，密码由6-16位字母(区分大小写)和数字组成");
                return;
            }

            ((Base2Activity) getActivity()).hideKeyboard(mEtPhone);
            ((Base2Activity) getActivity()).hideKeyboard(mEtCode);
            ((Base2Activity) getActivity()).hideKeyboard(mEtCodeAgain);

            new RequestLoginViewModel().userRresetPassword(phone, password,smsCode, new Function1<String, Unit>() {
                @Override
                public Unit invoke(String msg) {
                    ToastUtil.INSTANCE.showCenter("密码设置成功");
                    getActivity().finish();

                    return null;
                }
            });

        });

        setupListener();

    }

    private void setupListener() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = mEtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    mBtnNext.setEnabled(false);
                    return;
                }
                String code = mEtCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    mBtnNext.setEnabled(false);
                    return;
                }
                String code2 = mEtCodeAgain.getText().toString().trim();
                if (TextUtils.isEmpty(code2)) {
                    mBtnNext.setEnabled(false);
                    return;
                }
                mBtnNext.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mEtPhone.addTextChangedListener(watcher);
        mEtCode.addTextChangedListener(watcher);
        mEtCodeAgain.addTextChangedListener(watcher);

    }

}
