package com.sn.gameelectricity.app.util

import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sn.gameelectricity.R


fun ImageView.load(url: String?) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .apply(RequestOptions().apply {
            placeholder(R.drawable.img_loading)
            error(R.drawable.img_empty)
        })
        .into(this)
}

/**
 * 图片加载，可以动态修改placeholder和error
 */
fun ImageView.load(url: String?, @DrawableRes placeholderImg: Int, @DrawableRes errorImg: Int) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .apply(RequestOptions().apply {
            placeholder(placeholderImg)
            error(errorImg)
        })
        .into(this)
}


fun ImageView.load(url: String?, cornerSize: Float = 1.0F) {
    var reqBuilder = Glide.with(context)
        .asBitmap()
        .apply(RequestOptions().apply {
            placeholder(R.drawable.img_loading)
            error(R.drawable.img_empty)
        })
        .load(url)
        .centerCrop()
    val cornerSizePx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PT,
        cornerSize,
        context.resources.displayMetrics
    )
    if (cornerSizePx > 0) {
        reqBuilder = reqBuilder.transform(CenterCrop(), RoundedCorners(cornerSizePx.toInt()))
    }
    reqBuilder.into(this)
}


fun ImageView.loadUserAvatar(url: String?) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .error(R.drawable.default_user_avatar)
        .placeholder(R.drawable.default_user_avatar)
        .into(this)
}