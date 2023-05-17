package com.sn.gameelectricity.ui.activity.web

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.just.agentweb.AgentWeb
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.databinding.ActivityWebBinding
import kotlinx.android.synthetic.main.include_toolbar.*


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebActivity : BaseActivity1<WebViewModel, ActivityWebBinding>() {

    private val url: String by lazy {
        intent?.getSerializableExtra("url") as String
    }
    private val title: String by lazy {
        intent?.getSerializableExtra("title") as String
    }

    private var mAgentWeb: AgentWeb? = null

    private var preWeb: AgentWeb.PreAgentWeb? = null

    override fun initView(savedInstanceState: Bundle?) {
        imgBack.setOnClickListener { finish() }
        tvTitle.setText(title)

        preWeb = AgentWeb.with(this)
            .setAgentWebParent(mViewBind.webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()

        //加载网页
        mAgentWeb = preWeb?.go(url)
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let { web ->
                        if (web.webCreator.webView.canGoBack()) {
                            web.webCreator.webView.goBack()
                        } else {
                            finish()
                        }
                    }
                }
            })
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        setSupportActionBar(null)
        super.onDestroy()
    }

}