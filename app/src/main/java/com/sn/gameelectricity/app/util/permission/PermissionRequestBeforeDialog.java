package com.sn.gameelectricity.app.util.permission;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sn.gameelectricity.R;

import java.util.List;

/**
 * @ProjectName: AIFUN-ANDROID
 * @Package: org.sugram.foundation.permission
 * @ClassName: PermissionRationaleDialog
 * @Description: java类作用描述
 * @Author: czhen
 * @CreateDate: 2021/8/21 10:47
 */
public class PermissionRequestBeforeDialog extends Dialog {
    private TextView messageText;
    private TextView bodyText;
    private Button goSetting;
    private TextView cancelText;
    private List<String> permissions;
    private View.OnClickListener onClickListener;
    private View.OnClickListener dismissClickListener;

    public PermissionRequestBeforeDialog(@NonNull Context context, List<String> permissions, View.OnClickListener onClickListener, View.OnClickListener dismissClickListener) {
        super(context, R.style.TransparentPermissionDialog);
        setContentView(R.layout.layout_permission_dialog);
        setCanceledOnTouchOutside(false);
        this.permissions = permissions;
        this.onClickListener = onClickListener;
        this.dismissClickListener = dismissClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageText = findViewById(R.id.messageText);
        bodyText = findViewById(R.id.bodyText);
        goSetting = findViewById(R.id.btn_update);
        cancelText = findViewById(R.id.btn_cancel);
        goSetting.setText("允许");

        goSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onClickListener.onClick(v);
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dismissClickListener.onClick(v);
            }
        });
        if (permissions != null) {
            if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) || permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                messageText.setText("允许“AIFUN”访问您的位置？");
                bodyText.setText("AIFUN想访问您的位置，为了给您就近推荐周边活动、发送位置给好友。拒绝不影响使用其他服务");
            } else if (permissions.contains(Manifest.permission.CAMERA)) {
                messageText.setText("允许“AIFUN”访问您的相机？");
                bodyText.setText("AIFUN 想访问您的相机，为了您可以发布动态或和朋友分享照片或视频、给您提供扫码功能。拒绝不影响使用其他服务");
            } else if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                messageText.setText("允许“AIFUN”读写您的存储卡？");
                bodyText.setText("AIFUN 想访问您的存储卡");
            } else if (permissions.contains(Manifest.permission.RECORD_AUDIO)) {
                messageText.setText("允许“AIFUN”访问您的麦克风？");
                bodyText.setText("AIFUN 想访问您的麦克风，为了确保您在使用应用期间，能发送语音消息或与好友进行语音聊天。拒绝不影响使用其他服务。");
            } else if (permissions.contains(Manifest.permission.READ_CONTACTS)) {
                messageText.setText("允许“AIFUN”访问您的通讯录？");
                bodyText.setText("AIFUN 想访问您的通讯录，AIFUN将上传手机通讯录至AIFUN服务器以匹配朋友。（上传通讯录仅用于匹配，不会保存资料，亦不会用作它用）");
            } else if (permissions.contains(Manifest.permission.READ_PHONE_STATE)) {
                messageText.setText("允许“AIFUN”获取设备信息？");
                bodyText.setText("AIFUN 想获取设备信息，以保障您的账号和交易安全。拒绝不影响使用服务。");
            } else if (permissions.contains(Manifest.permission.WRITE_CALENDAR) || permissions.contains(Manifest.permission.READ_CALENDAR)) {
                messageText.setText("允许“AIFUN”获取日历信息？");
                bodyText.setText("AIFUN 想获取日历信息，以保障您能正常设置提醒。");
            }
        }
    }
}
