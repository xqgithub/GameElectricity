package com.sn.gameelectricity.app.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.util.CommonUtil;


public class PrivacyPolicyDialog extends Dialog {

    private Context mContext;
    TextView messageText;
    Button updateButton;

    private CharSequence message;
    private OnConfirmClickListener confirmClickListener;
    private OnCancelClickListener cancelClickListener;


    public interface OnCancelClickListener {
        void doCancel();
    }

    public interface OnConfirmClickListener {
        void doConfirm();
    }


    public PrivacyPolicyDialog(@NonNull Context context, CharSequence message, OnConfirmClickListener confirmClickListener, OnCancelClickListener cancelClickListener) {
        super(context, R.style.TransparentUpdateDialog);
        this.message = message;
        this.confirmClickListener = confirmClickListener;
        this.cancelClickListener = cancelClickListener;
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_privacy2_dialog, null);
        setContentView(view);
        messageText = (TextView) view.findViewById(R.id.tv_message);
        updateButton = (Button) view.findViewById(R.id.btn_update);
        view.findViewById(R.id.btn_update).setOnClickListener(v -> {
            if (confirmClickListener != null) {
                confirmClickListener.doConfirm();
            }
            dismiss();
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.doCancel();
            }
            dismiss();
        });
        messageText.setMovementMethod(LinkMovementMethod.getInstance());
        messageText.setText(message);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) Math.min(CommonUtil.dip2px(mContext, 302), (d.widthPixels * 0.8)); // 高度设置为屏幕的0.8
        lp.height = (int) CommonUtil.dip2px(mContext, 387); // 高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }
}
