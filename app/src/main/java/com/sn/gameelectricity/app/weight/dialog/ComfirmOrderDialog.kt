package com.snai.aifun.ui.dialog

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sn.gameelectricity.R


class ComfirmOrderDialog(
    context: Context,
    val fragmentManager: FragmentManager,
    val onClose: (orderNum: Int) -> Unit,
) :
    BottomSheetDialog(context, R.style.TransparentBottomDialog) {

    init {
//        setContentView(binding.root)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}