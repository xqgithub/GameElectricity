package com.sn.gameelectricity.app.weight.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sn.gameelectricity.R;


public class ProgressBarDialog2 extends ProgressBarDialog {

    public ProgressBarDialog2(Context context, String txt) {
        super(context, txt);
    }

    @Override
    protected void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_pb2, null);
        tvMsg = (TextView) view.findViewById(R.id.tv_pb_dialog);
        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
        }
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }
}
