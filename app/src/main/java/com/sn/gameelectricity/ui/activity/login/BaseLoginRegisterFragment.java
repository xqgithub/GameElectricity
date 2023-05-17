package com.sn.gameelectricity.ui.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.util.permission.PermissionCallback;
import com.sn.gameelectricity.app.util.permission.PermissionRequestManager;

import java.lang.ref.WeakReference;
import java.util.List;

import com.sn.gameelectricity.ui.activity.web.WebActivity;
import me.hgj.jetpackmvvm.util.ToastUtil;

public abstract class BaseLoginRegisterFragment extends Base2Fragment {

    protected EditText mEtPhone; // 手机号码
    protected EditText mEtCode; // 验证码 || 密码
    protected TextView mTvErrorTips; // 异常信息
    protected Button mBtnNext; // 下一步
    protected TextView mTvPrivacyPolicy;
    protected CheckBox mCbSure;
    protected EditText et_login_password_code;
    protected CheckBox switch_password;

    protected abstract void setupView(View rootView);

    // 发送请求 下一步
    protected abstract void sendRequest();

    @Override
    public void initData() {
        // 初始化数据
        setupData();
        // 绑定监听器
        setupListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 初始化数据
    protected void setupData() {
        if (mTvPrivacyPolicy != null) {
            initTermPrivacy();
        }
    }

    // 绑定监听器
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
                updateErrorText("");
            }
        };

        mEtPhone.addTextChangedListener(watcher);
        mEtCode.addTextChangedListener(watcher);

        mEtCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    next();
                    return true;
                }
                return false;
            }
        });

    }

    // 判断所有权限是否已授权
    protected void checkPermissionIsAllGranted() {
        // 先检测手机权限，后检测外部存储权限
        checkPhoneStoragePermission();
    }

    protected void next() {
        KeyboardUtils.hideSoftInput(mEtCode);

        if (!checkPhoneValidate()) {
            return;
        }

        if (!mCbSure.isChecked()) {
            ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).setTextSize(14).show("请仔细阅读并同意《用户协议》和《隐私政策》");
            return;
        }

        sendRequest();
    }

    // 检测手机号码的格式
    protected boolean checkPhoneValidate() {
        String phone = mEtPhone.getText().toString().trim();
        if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtil.INSTANCE.showCenter("请输入正确的手机号");
            return false;
        }
        return true;
    }

    // 检测验证码 || 密码是否有效
    protected boolean checkCodeValidate() {
        return true;
    }

    // 更新异常信息
    protected void updateErrorText(String text) {
        if (null != mTvErrorTips) {
            if (TextUtils.isEmpty(text)) {
                mTvErrorTips.setVisibility(View.GONE);
            } else {
                mTvErrorTips.setText(text);
                mTvErrorTips.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPhoneStoragePermission() {
        PermissionRequestManager.request(this, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS)
                .request(new PermissionCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        next();
                    }
                });
    }


    protected String getTermPolicy() {
        return "我已仔细阅读并同意";
    }

    private void initTermPrivacy() {
        SpannableStringBuilder privacyPolicy = new SpannableStringBuilder(getTermPolicy());
        SpannableString termText = new SpannableString(getResources().getString(R.string.UserTerm));
        termText.setSpan(new LoginClickableSpan(requireContext(), 0, getActivity()), 0, termText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString privacyText = new SpannableString(getResources().getString(R.string.PrivacyPolicy));
        privacyText.setSpan(new LoginClickableSpan(requireContext(), 1, getActivity()), 0, privacyText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        privacyPolicy.append(termText).append(getResources().getString(R.string.and)).append(privacyText);
        mTvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        mTvPrivacyPolicy.setText(privacyPolicy);
    }


    private static class LoginClickableSpan extends ClickableSpan {

        private WeakReference<Context> weakContext;
        private int type;
        private Activity activity;

        public LoginClickableSpan(Context context, int type, Activity activity) {
            this.weakContext = new WeakReference<>(context);
            this.type = type;
            this.activity = activity;
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
            }
            if (type == 0) {
                jumpToWeb("https://itest.aifun.com/#/Useragreement", "用户协议", 4);
            } else {
                jumpToWeb("https://itest.aifun.com/#/Privacy", "隐私政策", 2);
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.bgColor = Color.TRANSPARENT;
            ds.setColor(Color.parseColor("#EF874E"));
            ds.setUnderlineText(false);
        }

        //跳向网页
        private void jumpToWeb(String url, String title, int keyPage) {
            Context context = weakContext.get();
            if (context != null) {
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                context.startActivity(intent);
            }
        }
    }
}
