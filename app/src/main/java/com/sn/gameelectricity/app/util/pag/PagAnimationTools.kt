package com.sn.gameelectricity.app.util.pag

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import org.libpag.PAGFile
import org.libpag.PAGImage
import org.libpag.PAGView

/**
 * Date:2022/6/7
 * Time:9:24
 * author:dimple
 * pag动画工具类
 */
class PagAnimationTools {

    companion object {
        val pagTools: PagAnimationTools by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PagAnimationTools()
        }
    }

    fun getPagView(fileName: String, width: Int, height: Int): PAGView {
        val pagView = PAGView(App.instance)
        pagView.layoutParams = RelativeLayout.LayoutParams(width, height)
        val pagFile = PAGFile.Load(App.instance.assets, fileName)
        pagView.composition = pagFile
        return pagView
    }

    fun changePagView(fileName: String, pagView: PAGView) {
        val pagFile = PAGFile.Load(App.instance.assets, fileName)
        pagView.composition = pagFile
    }

    /**
     * 可以替换图层 PagView 初始化
     */
    fun getPagView2(
        fileName: String,
        width: Int,
        height: Int,
        picRes: MutableList<Int> = mutableListOf()
    ): PAGView {
        val pagView = PAGView(App.instance)
        pagView.layoutParams = RelativeLayout.LayoutParams(width, height)
        val pagFile = PAGFile.Load(App.instance.assets, fileName)
        pagView.composition = pagFile

        //添加图片替换方案
        if (picRes.isNotEmpty()) {
            for (i in picRes.indices) {
                val pAGImage = PAGImage.FromBitmap(
                    PublicPracticalMethodFromKT.ppmfKT.samplingRateCompress(
                        App.instance,
                        picRes[i]
                    )
                )
                pagFile.replaceImage(i, pAGImage)
            }
        }
        return pagView
    }


}