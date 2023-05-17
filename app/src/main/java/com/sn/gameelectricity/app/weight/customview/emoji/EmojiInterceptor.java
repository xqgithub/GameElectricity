package com.sn.gameelectricity.app.weight.customview.emoji;

import android.text.Spannable;

import com.sn.gameelectricity.app.weight.customview.Target;

/**
 * Created by nickyang on 2017/4/5.
 */

public interface EmojiInterceptor {
    Target intercept(Spannable text, int startIndex);
}
