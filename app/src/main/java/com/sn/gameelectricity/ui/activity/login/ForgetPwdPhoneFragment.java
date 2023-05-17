package com.sn.gameelectricity.ui.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sn.gameelectricity.R;
import com.sn.gameelectricity.viewmodel.request.RequestLoginViewModel;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import me.hgj.jetpackmvvm.util.ToastUtil;

public class ForgetPwdPhoneFragment extends Base2Fragment {
    public static final String KEY_USER_PHONE = "USER.KEY_PHONE";
    public static final String KEY_USER_LANGCODE = "USER.KEY_LANGCODE";
    TextView mTvError;
    Button mBtnNext;
    EditText mEtPhone;
    ImageView imgBack;
    EditText mEtCode;
    TextView mTvGetCode;
    TextView mTvRegetCode;
    private GetCodeBean mGetCodeBean;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_forgetpwd_phone, container, false);
        setupView(rootView);
        return rootView;
    }

    @Override
    public void initData() {
        // 绑定监听器
        setupListener();
    }

    // 初始化控件
    private void setupView(View rootView) {
        //请输入手机号
        mEtPhone = (EditText) rootView.findViewById(R.id.et_login_register_sms_phone);
        //请输入验证码
        mEtCode = (EditText) rootView.findViewById(R.id.et_login_register_sms_code);
        //获取验证码
        mTvGetCode = (TextView) rootView.findViewById(R.id.tv_login_register_sms_getcode);
        mTvGetCode.setOnClickListener(v -> {
            //获取验证码
            clickGetCode();
        });
        //重发验证码
        mTvRegetCode = (TextView) rootView.findViewById(R.id.tv_login_register_sms_regetcode);

        mBtnNext = (Button) rootView.findViewById(R.id.btn_forgetpwd_phone_next);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(KEY_USER_PHONE, mEtPhone.getText().toString().trim());
                args.putString(KEY_USER_LANGCODE, mEtCode.getText().toString().trim());
                ForgetPwdChoiceFragment fragment = new ForgetPwdChoiceFragment();
                fragment.setArguments(args);
                ((Base2Activity) getActivity()).pushFragment(fragment, ForgetPwdChoiceFragment.class.getSimpleName());
            }
        });
        imgBack = (ImageView) rootView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
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
                mBtnNext.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mEtPhone.addTextChangedListener(watcher);
        mEtCode.addTextChangedListener(watcher);

    }

    public void clickGetCode() {
        boolean phoneValid = checkPhoneValidate();
        if (phoneValid) {
            final String phone = mEtPhone.getText().toString().trim();
            new RequestLoginViewModel().sendSmsCode(phone, 1, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    if (null == mGetCodeBean) {
                        mGetCodeBean = new GetCodeBean(mTvGetCode, mTvRegetCode, 60);
                    }
                    mGetCodeBean.setupTimer();
                    mTvRegetCode.setText(String.format("重发验证码 (%1$sS)", getFormatTimeSecond(60 * 1000)));
                    mTvRegetCode.setVisibility(View.VISIBLE);
                    mTvGetCode.setVisibility(View.INVISIBLE);
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).setTextSize(12).show("验证码已发送!");
                    ToastUtil.INSTANCE.showCenter("验证码已发送!");
                    return null;
                }
            });
        }
    }

    protected boolean checkPhoneValidate() {
        String phone = mEtPhone.getText().toString().trim();
        if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtil.INSTANCE.showCenter("请输入正确的手机号");
            return false;
        }
        return true;
    }

    private static final long SECOND = 1 * 1000;

    public String getFormatTimeSecond(long time) {
        return String.valueOf((int) Math.ceil(time * 1.0f / SECOND));
    }
}