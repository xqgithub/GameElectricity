package com.sn.gameelectricity.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.sn.gameelectricity.R;
import com.sn.gameelectricity.app.base.BaseActivity1;
import com.sn.gameelectricity.app.util.pag.PagAnimationTools;
import com.sn.gameelectricity.databinding.ActivityTestpagBinding;
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel;

import org.jetbrains.annotations.Nullable;
import org.libpag.PAGView;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2022/6/6
 * Time:20:46
 * author:dimple
 */
public class TestPag extends BaseActivity1<PersonalSettingViewModel, ActivityTestpagBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initPAGView();
    }


    private void initPAGView() {
//        final PAGView pagView = new PAGView(this);
//        pagView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mViewBind.rlPag.addView(pagView);
//        PAGFile pagFile1 = PAGFile.Load(getAssets(), "goose_nol.pag");
//        pagView.setComposition(pagFile1);
//        pagView.setRepeatCount(0);
//        pagView.play();

//        PAGView pagView = (PAGView) PagAnimationTools.Companion.getPagTools().getPagView("goose_nol.pag",
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);

        List a = new ArrayList();
        a.add(R.drawable.ge_me_gold);
        a.add(R.drawable.ge_me_order);
        PAGView pagView = (PAGView) PagAnimationTools.Companion.getPagTools().getPagView2("goose_nol.pag",
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, a);
        mViewBind.rlPag.addView(pagView);
        pagView.setRepeatCount(0);
        pagView.play();


        pagView.addListener(new PAGView.PAGViewListener() {
            @Override
            public void onAnimationStart(PAGView view) {
            }

            @Override
            public void onAnimationEnd(PAGView view) {
            }

            @Override
            public void onAnimationCancel(PAGView view) {
            }

            @Override
            public void onAnimationRepeat(PAGView view) {
            }
        });
    }

}
