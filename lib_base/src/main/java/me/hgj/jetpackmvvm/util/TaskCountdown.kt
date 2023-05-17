package me.hgj.jetpackmvvm.util

import android.os.CountDownTimer
import me.hgj.jetpackmvvm.event.UniversalEvent
import org.greenrobot.eventbus.EventBus

/**
 * Date:2022/5/16
 * Time:9:32
 * author:dimple
 * 任务倒计时
 */
class TaskCountdown(millisInFuture: Long, countDownInterval: Long) :
    CountDownTimer(millisInFuture, countDownInterval) {

    private lateinit var universalEvent: UniversalEvent

    override fun onTick(millisUntilFinished: Long) {
        val time = millisUntilFinished / 1000L
        universalEvent = UniversalEvent(UniversalEvent.EVENT_VERIFICATION_CODE, time)
        EventBus.getDefault().post(universalEvent)
    }

    override fun onFinish() {
        val time = -1L
        universalEvent.message = time
        EventBus.getDefault().post(universalEvent)
    }
}