package com.sn.gameelectricity.app.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sn.gameelectricity.R;


public class ConfirmDialog extends Dialog {

    private Context context;
    private String title;
    private CharSequence message;
    private String okBtnText;
    private String cancelBtnText;
    private TextView tvConfirm;
    private TextView tvMessage;
    private TextView tvCancel;
    private ConfirmClickListener confirmClickListener;
    private CancelClickListener cancelClickListener;
    private boolean noCancleBtn;
    private boolean isDismissOnBackPressed = true;

    public interface CancelClickListener {
        void doCancel();
    }

    public interface ConfirmClickListener {
        void doConfirm();
    }

    public interface DismissListener {
        void onDismiss();
    }

    public ConfirmDialog(Context context, String title, CharSequence message, String okBtnText, String cancelBtnText, CancelClickListener cancelClickListener, ConfirmClickListener confirmClickListener) {
        super(context, R.style.TransparentDialogNoAnimation);
        this.context = context;
        this.title = title;
        this.message = message;
        this.confirmClickListener = confirmClickListener;
        this.cancelClickListener = cancelClickListener;
        this.okBtnText = okBtnText;
        this.cancelBtnText = cancelBtnText;
    }

    @Override
    public void show() {
        super.show();
    }

    public void showWithoutCancelBtn() {
        noCancleBtn = true;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_confirm_dialog, null);
        setContentView(view);

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        tvMessage = (TextView) view.findViewById(R.id.message);
        tvConfirm = (TextView) view.findViewById(R.id.confirm);
        tvCancel = (TextView) view.findViewById(R.id.cancel);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        //保证ClickableSpan可点击
        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
        tvMessage.setText(message);

        if (!TextUtils.isEmpty(okBtnText)) {
            tvConfirm.setText(okBtnText);
        }
        if (!TextUtils.isEmpty(cancelBtnText)) {
            tvCancel.setText(cancelBtnText);
        }

        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancelClickListener != null) {
                    cancelClickListener.doCancel();
                } else {
                    dismiss();
                }
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        if (noCancleBtn) {
            tvCancel.setVisibility(View.GONE);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
            if (v == tvConfirm) {
                if (confirmClickListener != null) {
                    confirmClickListener.doConfirm();
                }
            } else if (v == tvCancel) {
                if (cancelClickListener != null) {
                    cancelClickListener.doCancel();
                }
            }
        }

    }

    public void dismissOnBackPressed(Boolean dismissOnBackPressed) {
        isDismissOnBackPressed = dismissOnBackPressed;
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && !isDismissOnBackPressed) {
                    return true;
                }
                return false;
            }
        });
    }

}
