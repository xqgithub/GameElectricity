package me.hgj.jetpackmvvm.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import me.hgj.jetpackmvvm.R

/**
 * Date:2022/5/9
 * Time:16:19
 * author:dimple
 * Toast 工具类
 */
object ToastUtil {

    private lateinit var appContext: Context

    fun init(appContext: Context) {
        this.appContext = appContext
    }

    /**
     * 短时间显示Toast【居下】
     * @param message 显示的内容-字符串*/
    fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        createToast(length).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0);
            setText(message)
            show()
        }
    }

    /**
     * 短时间显示Toast【居中】
     * @param message 显示的内容-字符串*/
    fun showCenter(message: String, length: Int = Toast.LENGTH_SHORT) {
        createToast(length).apply {
            setGravity(Gravity.CENTER, 0, 0);
            setText(message)
            show()
        }
    }

    fun showCenter(message: String) {
        createToast(Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER, 0, 0);
            setText(message)
            show()
        }
    }

    fun showCustomSmallToast(message: String) {
        createSmallToast().apply {
            val textHintView = view?.findViewById<TextView>(R.id.textHintView)
            textHintView?.text = message
            show()
        }
    }

    @SuppressLint("ShowToast")
    private fun createSmallToast(): Toast {
        val customToast = Toast(appContext)
        val inflate =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(R.layout.layout_toast_suc, null, false)
        val successView = toastView.findViewById<ImageView>(R.id.successView)
        successView.setImageResource(R.drawable.af_pup_icon_success_gou)
        val container = toastView.findViewById<RelativeLayout>(R.id.container)
        container.layoutParams =
            RelativeLayout.LayoutParams(dip2px(appContext, 144F), dip2px(appContext, 128F))
        customToast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = toastView
        }
        return customToast
    }

    fun showCustomSmall2Toast(message: String) {
        createSmallToast2().apply {
            val textHintView = view?.findViewById<TextView>(R.id.textHintView)
            textHintView?.text = message
            show()
        }
    }

    @SuppressLint("ShowToast")
    private fun createSmallToast2(): Toast {
        val customToast = Toast(appContext)
        val inflate =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(R.layout.layout_toast_gold, null, false)
        val successView = toastView.findViewById<ImageView>(R.id.successView)
        successView.setImageResource(R.drawable.ic_order_jifen)
        val container = toastView.findViewById<RelativeLayout>(R.id.container)
        container.layoutParams =
            RelativeLayout.LayoutParams(dip2px(appContext, 125F), dip2px(appContext, 48F))
        customToast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = toastView
        }
        return customToast
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    @SuppressLint("ShowToast")
    private fun createToast(length: Int): Toast {
        return Toast.makeText(appContext, "", length)
    }

    /**
     * 自定义Toast显示
     */
    fun showCustomizeToast(
        icon: Int,
        content: String,
        position: Int = Gravity.BOTTOM,
        length: Int = Toast.LENGTH_SHORT,
        XOffset: Int = 0,
        yOffset: Int = 0
    ) {
        val customToast = Toast(appContext)
        val inflate =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(R.layout.layout_toast, null, false)
        val cl_main = toastView.findViewById<ConstraintLayout>(R.id.cl_main)
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            cl_main, -1, "",
            ScreenTools.getInstance().dp2px(appContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 16f, true).toFloat(),
            null, "#E5666666"
        )

        val successView = toastView.findViewById<ImageView>(R.id.iv_success)
        successView.setImageDrawable(ContextCompat.getDrawable(appContext, icon))

        val tv_content = toastView.findViewById<TextView>(R.id.tv_content)
        tv_content.text = content

        customToast.setGravity(position, XOffset, yOffset)
        customToast.duration = length
        customToast.view = toastView
        customToast.show()
    }


    /**
     * 自定义Toast显示
     */
    fun showCollectGooseEggRewardsToast(
        icon: Int,
        icon2: Int,
        num: Int = 0,
        num2: Int = 0,
        position: Int = Gravity.BOTTOM,
        length: Int = Toast.LENGTH_SHORT,
        XOffset: Int = 0,
        yOffset: Int = 0
    ) {
        val customToast = Toast(appContext)
        val inflate =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(R.layout.layout_toast2, null, false)
        val cl_main = toastView.findViewById<ConstraintLayout>(R.id.cl_main)
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            cl_main, -1, "",
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            null, "#E5666666"
        )

        val iv_icon = toastView.findViewById<ImageView>(R.id.iv_icon)
        iv_icon.setImageDrawable(ContextCompat.getDrawable(appContext, icon))
        val iv_icon2 = toastView.findViewById<ImageView>(R.id.iv_icon2)
        iv_icon2.setImageDrawable(ContextCompat.getDrawable(appContext, icon2))
        val tv_num = toastView.findViewById<TextView>(R.id.tv_num)
        tv_num.text = "x${num}"
        val tv_num2 = toastView.findViewById<TextView>(R.id.tv_num2)
        tv_num2.text = "x${num2}"

        customToast.setGravity(position, XOffset, yOffset)
        customToast.duration = length
        customToast.view = toastView
        customToast.show()
    }

    /**
     * 自定义Toast显示
     */
    fun showReceiveInvitationReward(
        icon: Int,
        num: Int = 0,
        position: Int = Gravity.BOTTOM,
        length: Int = Toast.LENGTH_SHORT,
        XOffset: Int = 0,
        yOffset: Int = 0
    ) {
        val customToast = Toast(appContext)
        val inflate =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(R.layout.layout_toast3, null, false)
        val cl_main = toastView.findViewById<ConstraintLayout>(R.id.cl_main)
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            cl_main, -1, "",
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            null, "#E5666666"
        )

        val iv_icon = toastView.findViewById<ImageView>(R.id.iv_icon)
        iv_icon.setImageDrawable(ContextCompat.getDrawable(appContext, icon))
        val tv_num = toastView.findViewById<TextView>(R.id.tv_num)
        tv_num.text = "x${num}"

        customToast.setGravity(position, XOffset, yOffset)
        customToast.duration = length
        customToast.view = toastView
        customToast.show()
    }


    /**
     * 自定义Toast显示
     */
    fun showCollectCoinsFeedRewardsToast(
        icon: Int,
        icon2: Int,
        num: Int = 0,
        num2: Int = 0,
        position: Int = Gravity.BOTTOM,
        length: Int = Toast.LENGTH_SHORT,
        XOffset: Int = 0,
        yOffset: Int = 0
    ) {
        val customToast = Toast(appContext)
        val inflate =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(R.layout.layout_toast4, null, false)
        val cl_main = toastView.findViewById<ConstraintLayout>(R.id.cl_main)
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            cl_main, -1, "",
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            ScreenTools.getInstance().dp2px(appContext, 8f, true).toFloat(),
            null, "#E5666666"
        )

        val iv_icon = toastView.findViewById<ImageView>(R.id.iv_icon)
        iv_icon.setImageDrawable(ContextCompat.getDrawable(appContext, icon))
        val iv_icon2 = toastView.findViewById<ImageView>(R.id.iv_icon2)
        iv_icon2.setImageDrawable(ContextCompat.getDrawable(appContext, icon2))
        val tv_num = toastView.findViewById<TextView>(R.id.tv_num)
        tv_num.text = "x${num}"
        val tv_num2 = toastView.findViewById<TextView>(R.id.tv_num2)
        tv_num2.text = "x${num2}"

        customToast.setGravity(position, XOffset, yOffset)
        customToast.duration = length
        customToast.view = toastView
        customToast.show()
    }


}