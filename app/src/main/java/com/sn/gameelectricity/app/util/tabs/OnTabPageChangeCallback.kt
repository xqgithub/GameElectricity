package com.sn.gameelectricity.app.util.tabs

import android.view.View
import android.widget.FrameLayout

interface OnTabPageChangeCallback {

    fun onTabSelected(view: View)

    fun onTabUnselected(view: View)

    fun onPageScrolled(selectedChild: View, nextTabView: View, positionOffset: Float)
}