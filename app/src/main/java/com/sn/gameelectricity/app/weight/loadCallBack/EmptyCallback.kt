package com.sn.gameelectricity.app.weight.loadCallBack


import com.kingja.loadsir.callback.Callback
import com.sn.gameelectricity.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}