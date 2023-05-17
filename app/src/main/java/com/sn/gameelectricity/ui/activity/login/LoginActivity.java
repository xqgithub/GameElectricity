package com.sn.gameelectricity.ui.activity.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sn.gameelectricity.R;


/**
 * 登录页
 */
public class LoginActivity extends Base2Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        setupData();
    }

    private void setupData() {
        initializeWithFragment(R.id.layout_fragment_container, new LoginBySmsFragment(), LoginBySmsFragment.class.getSimpleName());
    }
}