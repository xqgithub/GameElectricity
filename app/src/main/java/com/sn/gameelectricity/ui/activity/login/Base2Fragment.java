package com.sn.gameelectricity.ui.activity.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.util.LayoutHelper;
import com.sn.gameelectricity.app.weight.dialog.ConfirmDialog;


public abstract class Base2Fragment extends Fragment {
    public Toolbar mHeaderView;
    private View mFragmentView;            //子布局
    private LinearLayout mParentView;      //父布局

    private boolean isHideHeader = true;   //是否需要显示头部

    private ConfirmDialog mConfirmDialog;

    private TextView tvTitle;//标题

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = initView(inflater, container, savedInstanceState);
        }
        if (mParentView == null) {
            mParentView = new LinearLayout(getActivity());
            mParentView.setLayoutParams(LayoutHelper.createFrame(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mParentView.setOrientation(LinearLayout.VERTICAL);
        } else {
            return mParentView;
        }
        mParentView.addView(mFragmentView, LayoutHelper.createLinear(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initData();
        return mParentView;
    }

    /**
     * 最通用的标题栏设置
     * 在子类initData中调用
     *
     * @param title 标题字符串的
     */
    protected void setupHeaderView(String title) {
        mHeaderView.setNavigationIcon(R.drawable.main_chats_back);
        updateTitle(title);

        addOptionsMenu();
    }

    protected void addOptionsMenu() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mHeaderView);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 更换标题
     */
    protected void updateTitle(String title) {
        if (null == tvTitle) {
            tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_header_title);
        }
        tvTitle.setText(title);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 在这个方法里面初始化Fragment的UI界面，且只能在这里初始化UI界面
     * 防止Fragment被其他的Fragment替换掉又换回来时UI界面被重复创建
     * 子类必须重写这个方法
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState);

    /**
     * 在这里进行数据的初始化
     * 防止Fragment被其他的Fragment替换掉又换回来时数据被重复初始化
     * 子类必须重写这个方法
     */
    public abstract void initData();

    /**
     * 子类如果要消耗back事件 请重写这个方法并返回true
     *
     * @return 返回true表示消耗了back事件，返回false表示不消耗back事件
     */
    public boolean onBackPressed() {
        try {
            ((Base2Activity) getActivity()).popBackFragment(this);
        } catch (Exception ignored) {
        }
        return true;
    }

    /**
     * ############# 权限管理相关 如果你的Fragmment需要申请敏感权限 #######################
     * ###########请先申请权限 再通过回调进行接下来的处理###############
     */

    private final static int CHECK_PERMISSION_CODE = 10086;


    /**
     * 跳转到应用程序信息详情页面
     */
    protected void jump2PermissionSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }

    public void onPermissionDenied(String permission) {
    }

    public void onPermissionFailure() {
    }

    public void onPermissionGranted(String permission) {
    }

    // 显示正在加载的提示框
    public void showLoadingProgressDialog(final String text) {
        Base2Activity baseActivity = (Base2Activity) getActivity();
        if (null != baseActivity && !baseActivity.isFinishing()) {
            baseActivity.showLoadingProgressDialog(text);
        }
    }

    // 显示正在加载的提示框
    public void showLoadingProgressDialog(boolean cancelOnTouchOutside, boolean cancelable, String text) {
        Base2Activity baseActivity = (Base2Activity) getActivity();
        if (null != baseActivity && !baseActivity.isFinishing()) {
            baseActivity.showLoadingProgressDialog(cancelOnTouchOutside, cancelable, text);
        }
    }

    // 隐藏正在加载的提示框
    public void hideLoadingProgressDialog() {
        Base2Activity baseActivity = (Base2Activity) getActivity();
        if (null != baseActivity && !baseActivity.isFinishing()) {
            baseActivity.hideLoadingProgressDialog();
        }
    }

}
