package com.sn.gameelectricity.app.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sn.gameelectricity.R;


public class ProgressBarDialog extends Dialog {

    protected Context mContext;
    protected String msg;
    protected TextView tvMsg;

    public ProgressBarDialog(Context context, String txt) {
        super(context, R.style.TransparentDialog);
        mContext = context;
        msg = txt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    protected void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_pb, null);
        tvMsg = (TextView) view.findViewById(R.id.tv_pb_dialog);
        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
        }
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.6);
        dialogWindow.setAttributes(lp);
    }

    // 更新文本
    public void updateMsg(String msg) {
        this.msg = msg;

        if (null != tvMsg) {
            tvMsg.setText(msg);
        }
    }
}
