package me.hgj.jetpackmvvm.initializer;

import android.util.Log;

import androidx.core.os.TraceCompat;

import com.caij.app.startup.Task;
import com.caij.app.startup.TaskListener;

/**
 *
 */
public class MonitorTaskListener implements TaskListener {

    private String tag;
    private boolean isLog;

    public MonitorTaskListener(String tag, boolean isLog) {
        this.tag = tag;
        this.isLog = isLog;
    }

    @Override
    public void onWaitRunning(Task task) {
    }

    @Override
    public void onStart(Task task) {
        if (isLog) {
            Log.d(tag + "-START-" + "mainThread:" + task.isMustRunMainThread(), "task start :" + task.getTaskName());
        }
        TraceCompat.beginSection(task.getTaskName());
        if (task.isWaitOnMainThread()) {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        }
    }

    @Override
    public void onFinish(Task task, long dw, long df) {
        if (task.isWaitOnMainThread()) {
            android.os.Process.setThreadPriority(ThreadManager.DEFAULT_PRIORITY);
        }
        TraceCompat.endSection();
        if (isLog) {
            Log.d(tag + "-END-" + "mainThread:" + task.isMustRunMainThread(), "task end :" + task.getTaskName() + " wait " + dw + " cost " + df);
        }
    }

}
