package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.ViewAvatarsviewBinding
import me.hgj.jetpackmvvm.util.load
import singleClick

/**
 * Date:2022/3/28
 * Time:16:45
 * author:dimple
 * 头像自定义View
 */
class AvatarsView @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewAvatarsviewBinding

    //图像点击
    private lateinit var avatarOnClickListener: () -> Unit

    init {
        val root = View.inflate(context, R.layout.view_avatarsview, this)
        mBinding = ViewAvatarsviewBinding.bind(root)
    }

    /**
     * 设置图片
     */
    fun setAvatarData(
        img: String,
        default_avatar: Int = R.drawable.ge_default_avatar_add,
        avatarOnClickListener: () -> Unit
    ) {
//        mBinding.sivAvatar.setImageDrawable(ContextCompat.getDrawable(mContext, img))
        if (img == "ge_default_avatar_more") {
            mBinding.sivAvatar.load(
                img,
                R.drawable.ge_default_avatar_more,
                R.drawable.ge_default_avatar_more
            )
        } else {
            mBinding.sivAvatar.load(img, default_avatar, default_avatar)
        }
        this.avatarOnClickListener = avatarOnClickListener
        mBinding.sivAvatar.singleClick {
            this.avatarOnClickListener.invoke()
        }
    }

    /**
     * 设置图像的大小
     */
    fun setAvatarWHData(width: Int, height: Int) {
        val params = mBinding.sivAvatar.layoutParams
//        LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "height =-=  ${params.height}",
//                "width =-=  ${params.width}"
//        )
        params.height = height
        params.width = width
        mBinding.sivAvatar.layoutParams = params
    }
}