package com.sn.gameelectricity.ui.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.network.TokenInterceptorHelper;
import com.sn.gameelectricity.app.util.CacheUtil;
import com.sn.gameelectricity.app.util.ClipboardUtil;
import com.sn.gameelectricity.data.model.bean.UserInfo;
import com.sn.gameelectricity.ui.activity.MainActivity;
import com.sn.gameelectricity.viewmodel.request.RequestLoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import me.hgj.jetpackmvvm.util.ToastUtil;

public class LoginBySmsFragment extends BaseLoginRegisterFragment implements View.OnClickListener {
    TextView mTvGetCode;
    TextView mTvRegetCode;
    TextView mTvLoginByPwd;
    FrameLayout mEtUserCode;
    EditText mEtUsCode;
    TextView mTvTip;
    private GetCodeBean mGetCodeBean;
    String userCode = "";


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login_register_sms, container, false);
        rootView.setOnClickListener(v -> {
            ((Base2Activity) getActivity()).hideKeyboard(mEtPhone);
            ((Base2Activity) getActivity()).hideKeyboard(mEtCode);
        });
        setupView(rootView);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .navigationBarColorInt(Color.WHITE)
                .navigationBarColor(R.color.colorPrimary)
                .init();

        return rootView;
    }

    @Override
    protected void setupView(View rootView) {
        //请输入手机号
        mEtPhone = (EditText) rootView.findViewById(R.id.et_login_register_sms_phone);
        //请输入验证码
        mEtCode = (EditText) rootView.findViewById(R.id.et_login_register_sms_code);
        //登录
        mBtnNext = (Button) rootView.findViewById(R.id.btn_login_register_sms_next);
        mBtnNext.setOnClickListener(this::onClick);
        //同意
        mTvPrivacyPolicy = rootView.findViewById(R.id.tv_privacy_policy);
        mCbSure = (CheckBox) rootView.findViewById(R.id.cb_sure);
        //获取验证码
        mTvGetCode = (TextView) rootView.findViewById(R.id.tv_login_register_sms_getcode);
        mTvGetCode.setOnClickListener(this::onClick);
        //重发验证码
        mTvRegetCode = (TextView) rootView.findViewById(R.id.tv_login_register_sms_regetcode);
        //请输入邀请码
        mEtUserCode = (FrameLayout) rootView.findViewById(R.id.fl_user_code);
        mEtUsCode = (EditText) rootView.findViewById(R.id.etUsCode);
        mTvTip = (TextView) rootView.findViewById(R.id.tvTip);
        //账号密码登录
        mTvLoginByPwd = (TextView) rootView.findViewById(R.id.tv_login_register_sms_loginpwd);
        mTvLoginByPwd.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_register_sms_getcode:
                //获取验证码
                clickGetCode();
                break;
            case R.id.btn_login_register_sms_next:
                //点击登录
                next();
                break;
            case R.id.tv_login_register_sms_loginpwd:
                //账号密码登录
                String tag = LoginByPasswordFragment.class.getSimpleName();
                Base2Fragment fragment = ((Base2Activity) getActivity()).findFragmentByTag(tag);
                if (null == fragment) {
                    ((Base2Activity) getActivity()).pushFragment(new LoginByPasswordFragment(), tag);
                } else {
                    ((Base2Activity) getActivity()).showFragment(tag);
                }
                break;
            default:
                break;
        }
    }

    public void clickGetCode() {
        LogUtils.e("123", ClipboardUtil.getText(getActivity()));
        boolean phoneValid = checkPhoneValidate();
        if (phoneValid) {
            String clipboardText = ClipboardUtil.getText(getActivity());
            if (!TextUtils.isEmpty(clipboardText) && clipboardText.contains("userCode")) {
                try {
                    userCode = new JSONObject(clipboardText).getString("userCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            final String phone = mEtPhone.getText().toString().trim();
            new RequestLoginViewModel().boolUserRegister(phone, new Function1<Integer, Unit>() {
                @Override
                public Unit invoke(Integer integer) {
                    if (integer == 0 && TextUtils.isEmpty(userCode)) {
                        mEtUserCode.setVisibility(View.VISIBLE);
                        mTvTip.setVisibility(View.VISIBLE);
                    } else {
                        mEtUserCode.setVisibility(View.GONE);
                        mTvTip.setVisibility(View.GONE);
                    }

                    return null;
                }
            });

            new RequestLoginViewModel().sendSmsCode(phone, 0, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    if (null == mGetCodeBean) {
                        mGetCodeBean = new GetCodeBean(mTvGetCode, mTvRegetCode, 60);
                    }
                    mGetCodeBean.setupTimer();
                    mTvRegetCode.setText(String.format("重发验证码 (%1$sS)", getFormatTimeSecond(60 * 1000)));
                    mTvRegetCode.setVisibility(View.VISIBLE);
                    mTvGetCode.setVisibility(View.INVISIBLE);
                    ToastUtil.INSTANCE.showCenter("验证码已发送!");
                    return null;
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mGetCodeBean) {
            mGetCodeBean.destroy();
            mGetCodeBean = null;
        }
    }

    private static final long SECOND = 1 * 1000;

    public String getFormatTimeSecond(long time) {
        return String.valueOf((int) Math.ceil(time * 1.0f / SECOND));
    }

    @Override
    protected void sendRequest() {

        ((Base2Activity) getActivity()).hideKeyboard(mEtPhone);
        ((Base2Activity) getActivity()).hideKeyboard(mEtCode);

        if (mEtUserCode.getVisibility() == View.VISIBLE){
            userCode = mEtUsCode.getText().toString().trim();
            ((Base2Activity) getActivity()).hideKeyboard(mEtUsCode);
        }

        final String phone = mEtPhone.getText().toString().trim();
        final String passcode = mEtCode.getText().toString().trim();
        new RequestLoginViewModel().userRegisterOrLoginBySmsCode(phone, passcode,userCode, new Function1<UserInfo, Unit>() {
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
