package com.sn.gameelectricity.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.network.TokenInterceptorHelper;
import com.sn.gameelectricity.app.util.CacheUtil;
import com.sn.gameelectricity.app.util.CommonUtil;
import com.sn.gameelectricity.data.model.bean.UserInfo;
import com.sn.gameelectricity.ui.activity.MainActivity;
import com.sn.gameelectricity.viewmodel.request.RequestLoginViewModel;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.hgj.jetpackmvvm.util.ToastUtil;

/**
 * 账号密码登录
 */
public class LoginByPasswordFragment extends BaseLoginRegisterFragment implements View.OnClickListener {

    private TextView mTvLoginBySms;
    private TextView mTvForgotPwd;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_login_password, container, false);
        rootView.setOnClickListener(v -> {
            ((Base2Activity) getActivity()).hideKeyboard(mEtCode);
            ((Base2Activity) getActivity()).hideKeyboard(mEtPhone);
        });
        setupView(rootView);

        return rootView;
    }

    @Override
    protected void setupView(View rootView) {
        //请输入手机号
        mEtPhone = (EditText) rootView.findViewById(R.id.et_login_password_phone);
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
        //登录
        mBtnNext = (Button) rootView.findViewById(R.id.btn_login_password_next);
        mBtnNext.setOnClickListener(this::onClick);
        //同意
        mTvPrivacyPolicy = rootView.findViewById(R.id.tv_privacy_policy);
        //同意
        mCbSure = (CheckBox) rootView.findViewById(R.id.cb_sure);

//        mCbSure.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                ToastUtil.INSTANCE.showCenter("我已仔细阅读并同意《用户协议》和《隐私政策》");
//            }
//        });
        //短信验证码登录
        mTvLoginBySms = (TextView) rootView.findViewById(R.id.tv_login_password_loginsms);
        mTvLoginBySms.setOnClickListener(this::onClick);
        //忘记密码
        mTvForgotPwd = (TextView) rootView.findViewById(R.id.tv_login_password_forgotpwd);
        mTvForgotPwd.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_password_next:
                // 点击 登录
                KeyboardUtils.hideSoftInput(mEtCode);
                String password = mEtCode.getText().toString().trim();
//                if (TextUtils.isEmpty(password)  || (!CommonUtil.isLetterDigit(password))) {
////                    || (!CommonUtil.isLetterDigit(password))
//                    ToastUtil.INSTANCE.showCenter("密码由6-16位字母和数字组成");
//                    return;
//                }
                next();
                break;
            case R.id.tv_login_password_loginsms:
                // 点击 短信验证码登录
                String tag = LoginBySmsFragment.class.getSimpleName();
                Base2Fragment fragment = ((Base2Activity) getActivity()).findFragmentByTag(tag);
                if (null == fragment) {
                    ((Base2Activity) getActivity()).pushFragment(new LoginBySmsFragment(), tag);
                } else {
                    ((Base2Activity) getActivity()).showFragment(tag);
                }
                break;
            case R.id.tv_login_password_forgotpwd:
                // 点击 忘记密码
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    protected void sendRequest() {
        ((Base2Activity) getActivity()).hideKeyboard(mEtPhone);
        ((Base2Activity) getActivity()).hideKeyboard(mEtCode);

        final String phone = mEtPhone.getText().toString().trim();
        final String password = mEtCode.getText().toString().trim();

        new RequestLoginViewModel().userRegisterOrLoginByPassword(phone, password, new Function1<UserInfo, Unit>() {
            @Override
            public Unit invoke(UserInfo userInfo) {
                CacheUtil.INSTANCE.setUser(userInfo);
                CacheUtil.INSTANCE.setIsLogin(true);
                TokenInterceptorHelper.getInstance().saveAppToken(getContext(), userInfo.getToken());

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

                return null;
            }
        });
    }
}
