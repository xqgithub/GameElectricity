package com.sn.gameelectricity.app.weight.loadCallBack

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.kingja.loadsir.callback.Callback
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import org.libpag.PAGView


class LoadingCallback : Callback() {


    private var pag_loading: PAGView? = null

    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun onReloadEvent(context: Context, view: View): Boolean {
        return true
    }

    override fun onAttach(context: Context, view: View) {
        super.onAttach(context, view)
        strartLoadingAnimation(view)
    }

    override fun onDetach() {
        super.onDetach()
        stopLoadingAnimation()
    }

    override fun getSuccessVisible(): Boolean {
        return false
    }

    /**
     * 开始动画
     */
    private fun strartLoadingAnimation(view: View) {
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(pag_loading)) {
            pag_loading = PagAnimationTools.pagTools.getPagView(
                "chong.pag",
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val rl_pag_loading = view.findViewById<RelativeLayout>(R.id.rl_pag_loading)
            rl_pag_loading.addView(pag_loading)
        }
        pag_loading?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) it.play()
        }
    }

    /**
     * 停止动画
     */
    private fun stopLoadingAnimation() {
        pag_loading?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }
}