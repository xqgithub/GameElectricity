package com.sn.gameelectricity.ui.activity.login;

import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * 获取验证码
 * Created by cyj on 2017/04/20
 */
public class GetCodeBean {
    private static final int PERIOD_UPDATE_TIME = 1000; // 1秒更新时间
    private long mLeftTime; // 获取验证码剩余时间
    private Timer mTimer;
    private TimerTask mTimerTask;

    private TextView mTvRegetCode;
    private TextView mTvGetCode;
    private long mTimeoutMills;

    public GetCodeBean(TextView tvGetCode, TextView tvRegetCode, int timeoutSeconds) {
        mTvGetCode = tvGetCode;
        mTvRegetCode = tvRegetCode;
        mTimeoutMills = timeoutSeconds * 1000;
    }

    // 初始化定时器
    public void setupTimer() {
        // 获取验证码间隔时间180s, 显示180
        mLeftTime = mTimeoutMills;

        if (null == mTimer) {
            mTimer = new Timer();
        }

        if (null == mTimerTask) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Observable.just(mLeftTime).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long time) throws Exception {
                            if (null != mTvRegetCode) {
                                mTvRegetCode.setText(String.format("重发验证码 (%1$sS)", getFormatTimeSecond(time)));
                            }

                            mLeftTime = time - PERIOD_UPDATE_TIME;

                            if (mLeftTime <= 0) {
                                mTimerTask.cancel();
                                mTimerTask = null;

                                if (null != mTvGetCode) {
                                    mTvGetCode.setVisibility(View.VISIBLE);
                                }

                                if (null != mTvRegetCode) {
                                    mTvRegetCode.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                }
            };
        }

        mTimer.schedule(mTimerTask, 0, PERIOD_UPDATE_TIME);
    }

    private static final long SECOND = 1 * 1000;

    public String getFormatTimeSecond(long time) {
        return String.valueOf((int) Math.ceil(time * 1.0f / SECOND));
    }

    // 终止定时器
    public void destroy() {
        if (null != mTimerTask) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }

        mTvGetCode = null;
        mTvRegetCode = null;
    }
}
