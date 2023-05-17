
package com.sn.gameelectricity.app.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Hashtable;

public class AndroidUtilities {
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    public static float density = 1;
    public static TextPaint textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        view.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static int compare(int lhs, int rhs) {
        if (lhs == rhs) {
            return 0;
        } else if (lhs > rhs) {
            return 1;
        }
        return -1;
    }

    // 根据长度截取出剪裁后的文字
    public static String getEllipsizeText(float availableTextWidth, CharSequence text, float textSize) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);

        String ellipsizeStr = (String) TextUtils.ellipsize(text, paint, availableTextWidth, TextUtils.TruncateAt.END);
        return ellipsizeStr;
    }

    // 获取文本长度
    public static float getTextWidth(CharSequence text, float textSize) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        return paint.measureText(text.toString());
    }
}
