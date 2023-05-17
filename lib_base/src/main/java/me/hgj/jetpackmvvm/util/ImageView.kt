package me.hgj.jetpackmvvm.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Date:2022/5/9
 * Time:14:14
 * author:dimple
 */


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