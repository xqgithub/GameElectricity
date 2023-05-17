package com.sn.gameelectricity.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.weight.dialog.ConfirmDialog;
import com.sn.gameelectricity.app.weight.dialog.ProgressBarDialog;
import com.sn.gameelectricity.app.weight.dialog.ProgressBarDialog2;

import java.util.List;

public class Base2Activity extends AppCompatActivity {
    //Fragment管理器
    private FragmentManager mFragmentManager;
    private int mContainerResourceId;
    private Base2Fragment mCurrentFragment;

    // 正在加载窗
    private ProgressBarDialog mProgressDialog;
    private ConfirmDialog mConfirmDialog;

    //是否跳转过应用程序信息详情页
    private boolean mIsJump2Settings = false;
    private final static int REQUEST_PERMISSION = 10086;

    private boolean isInit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setOrientation();
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void setContentView(int layoutResID) {
        View rootView = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(rootView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    protected void lazyInit() {

    }

    protected void setOrientation() {
        //设置强制竖屏 Manifest.xml文件里不应写过多的重复配置 像一些主题 横竖屏控制等属性在这里设置就好
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        // 兼容androidX在部分手机切换语言失败问题
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInit) {
            isInit = true;
            lazyInit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }


    /**
     * 跳转到应用程序信息详情页面
     */
    public void jump2PermissionSettings() {
        mIsJump2Settings = true;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    /**
     * 如果你的Activity中使用到了Fragment，那么请使用此方法初始化第一个Fragment。
     * 在Activity中请不要保留对Fragment的引用
     * ，因为FragmentManager自动会处理Fragment的生命周期和状态保存，我们创建好Fragment之后
     * ，它的生死就应该全部交给FragmentManager去管理
     * ，我们不用太多插足，当我们需要重新获取某个Fragment对象时，可以通过findFragmentByTag重新找回Fragment
     *
     * @param resourceId 承载Fragment的容器的资源id，
     * @param fragment   我们的第一个Fragment对象的引用
     * @param tag        标记我们的Fragment的tag
     */
    protected void initializeWithFragment(int resourceId, Base2Fragment fragment, String tag) {
        mContainerResourceId = resourceId;
        mCurrentFragment = fragment;

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        // 为什么要加listener？每当堆栈改变时，要获得最新的可见Fragment，并赋给mCurrentFragment
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                List<Fragment> fragmentList = mFragmentManager.getFragments();
                for (Fragment fragment : fragmentList) {
                    if (fragment != null && fragment.isVisible()) {
                        mCurrentFragment = (Base2Fragment) fragment;
                    }
                }
            }
        });

        // 如果有相同tag的fragment已经存在，则什么都不做，直接返回
        if (mFragmentManager.findFragmentByTag(tag) != null) {
            return;
        }

        try {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(resourceId, fragment, tag);
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当你需要新加入一个Fragment，并且新的Fragment是以堆栈（点击返回键可以取消）的形式覆盖老的Fragment，这种情况下，
     * 请使用这个方法，因为在initializeWithFragment中已经指定了resourceId，所以这里不用再次指定
     *
     * @param fragment 新的Fragment对象的引用
     * @param tag      标记Fragment的tag
     */
    public void pushFragment(Base2Fragment fragment, String tag) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        try {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.open_enter, R.anim.open_exit, R.anim.close_enter, R.anim.close_exit);
            ft.hide(mCurrentFragment);
            ft.add(mContainerResourceId, fragment, tag);
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
            // 被覆盖的fragment的visible状态设置为false，新添加的fragment默认是true，不用设置(其实设置了会因为fragment还没有createView而崩溃)
            ft.setMaxLifecycle(mCurrentFragment, Lifecycle.State.STARTED);
            mCurrentFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空回退栈
     */
    public void clearFragmentBackStack() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }


    /**
     * 此方法是pushFragment的逆操作，用于主动从Fragment堆栈中弹起最上面的Fragment
     */
    public void popFragment() {
        mFragmentManager.popBackStack();
    }

    /**
     * 针对fragment的回退操作，当fragment栈见底时能退出activity
     */
    public void popBackFragment(Base2Fragment fragment) {
        List<Fragment> fragments = mFragmentManager.getFragments();
        int index = fragments.indexOf(fragment);
        if (index == 0) {
            onBackPressed();
        } else {
            popFragment();
        }
    }

    /**
     * 当你需要添加一个Fragment并把老的Fragment遮住时，请使用此方法。使用此方法添加的Fragment不能用返回键撤销
     *
     * @param fragment 新的Fragment对象的引用
     * @param tag      标记Fragment的tag
     */
    public void addFragment(Base2Fragment fragment, String tag) {
        if (findFragmentByTag(tag) != null) {
            return;
        }

        try {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            List<Fragment> fragmentList = mFragmentManager.getFragments();
            for (Fragment f : fragmentList) {
                if (f != null) {
                    ft.hide(f);
                    f.setUserVisibleHint(false);
                }
            }
            ft.add(mContainerResourceId, fragment, tag);
            ft.commitAllowingStateLoss();
            mCurrentFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示tag指定的Fragment，并且把其他的Fragment隐藏掉
     *
     * @param tag             fragment标记
     * @param animationEnable 切换Fragment是否需要动画
     */
    public void showFragment(String tag, boolean animationEnable) {
        try {
            Fragment fragment = mFragmentManager.findFragmentByTag(tag);
            if (fragment != null) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (animationEnable) {
                    ft.setCustomAnimations(R.anim.open_enter, R.anim.open_exit, R.anim.close_enter, R.anim.close_exit);
                }
                List<Fragment> fragments = mFragmentManager.getFragments();

                // 两边循环，第一遍先隐藏，第二遍再显示，不然resumeTimer()和pauseTimer()时序有问题
                for (Fragment f : fragments) {
                    if ((f != null) && (f != fragment)) {
                        ft.hide(f);
                        f.setUserVisibleHint(false);
                    }
                }

                for (Fragment f : fragments) {
                    if ((f != null) && (f == fragment)) {
                        ft.show(f);
                        f.setUserVisibleHint(true);
                        mCurrentFragment = (Base2Fragment) f;
                    }
                }

                ft.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示tag指定的Fragment，并且把其他的Fragment隐藏掉
     *
     * @param tag fragment标记
     */
    public void showFragment(String tag) {
        showFragment(tag, true);
    }

    /**
     * 通过tag找对应的Fragment对象
     */
    public Base2Fragment findFragmentByTag(String tag) {
        // 本项目的所有Fragment都必须继承自BaseFragment，如果发现不是？直接返回null
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment instanceof Base2Fragment) {
            return (Base2Fragment) fragment;
        }
        return null;
    }

    /**
     * 如果有Activity要屏蔽KEY_BACK事件，直接在这个方法中return true就行
     *
     * @return false：不屏蔽KEY_BACK事件，true：屏蔽
     */
    protected boolean shouldInterceptBackEvent() {
        return false;
    }


    // 显示输入键盘
    public void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    // 判断输入键盘是否显示
    public boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputManager.isActive(view);
    }

    // 隐藏输入键盘
    public void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
            //先判断Activity中的fragment是否需要消耗back事件
            if (mCurrentFragment != null && mCurrentFragment.onBackPressed()) {
                return true;
            }
            if (shouldInterceptBackEvent()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    /**
     * 以设计图为750*1334尺寸做适配,适配为mdpi,参考:<p>
     * android屏幕适配终结者 https://blankj.com/2018/12/18/android-adapt-screen-killer/}
     *
     * @return Resources
     */
    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(),
                375);
    }

    // 显示正在加载的提示框 {cancelOnTouchOutside:是否允许点击外部取消弹窗, cancelable:是否允许取消}
    public void showLoadingProgressDialog(final boolean cancelOnTouchOutside,
                                          final boolean cancelable, String... text) {
        final String content;
        if (text != null && text.length == 1 && !TextUtils.isEmpty(text[0])) {
            content = text[0];
        } else {
            content = "加载中...";
        }
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressDialog(cancelOnTouchOutside, cancelable, content);
            }
        });

    }

    // 显示正在加载的提示框
    public void showLoadingProgressDialog(String... text) {
        showLoadingProgressDialog(true, true, text);
    }

    // 显示正在加载的提示框
    private void showProgressDialog(boolean cancelOnTouchOutside, boolean cancelable, String
            text) {
        if (null == mProgressDialog) {
            if (isFinishing()) {
                return;
            }
            if (!TextUtils.isEmpty(text) && text.length() > 10) {
                mProgressDialog = new ProgressBarDialog2(this, text);
            } else {
                mProgressDialog = new ProgressBarDialog(this, text);
            }
        }

        mProgressDialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        mProgressDialog.setCancelable(cancelable);

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.updateMsg(text);
            mProgressDialog.show();
        }
    }

    // 隐藏正在加载的提示框
    public void hideLoadingProgressDialog() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        });
    }

    // 隐藏正在加载的提示框
    private void hideProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }

        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
        }
    }
}
