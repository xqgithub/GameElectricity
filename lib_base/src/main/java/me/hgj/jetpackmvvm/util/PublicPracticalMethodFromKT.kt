package me.hgj.jetpackmvvm.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.View.OnSystemUiVisibilityChangeListener
import android.view.Window
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.LogUtils
import me.hgj.jetpackmvvm.common.ConfigConstants
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.math.BigDecimal


/**
 * Date:2021/11/12
 * Time:9:55
 * author:dimple
 * 公共实用类 kotlin
 */
class PublicPracticalMethodFromKT {

    companion object {
        val ppmfKT: PublicPracticalMethodFromKT by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PublicPracticalMethodFromKT()
        }
    }

    /**
     * 是否为空白,包括null和""
     */
    fun isBlank(str: Any?): Boolean {
        return str == null ||
                (str is String && str.toString().trim().isEmpty()) ||
                (str is String && "null" == str.toString().toUpperCase())
    }

    fun isBlank(vararg strs: Any?): Boolean {
        for (str in strs) {
            if (isBlank(str)) {
                return true
            }
        }
        return false
    }

    /**
     * 页面跳转
     * @param mActivity   上下文
     * @param cls         目标的Activity
     * @param flag        跳转的方式
     * @param bundle      bundle值
     * @param isFinish    进入页面后，是否结束上一个页面
     * @param enterAnimID 进入动画ID
     * @param exitAnimID  退出动画ID
     */
    fun intentToJump(
        mActivity: Activity, cls: Class<*>,
        bundle: Bundle? = null, isFinish: Boolean = false,
        enterAnimID: Int = -1, exitAnimID: Int = -1,
        flag: Int = -1
    ) {
        val intent = Intent(mActivity, cls)
        intent.apply {
            bundle?.let { putExtras(it) }
            if (flag != -1) flags = flag
            mActivity.startActivity(this)
            if (isFinish) mActivity.finish()
            if (enterAnimID == -1 && exitAnimID == -1) {
                return
            } else {
                mActivity.overridePendingTransition(
                    if (enterAnimID == -1) 0 else enterAnimID,
                    if (exitAnimID == -1) 0 else exitAnimID
                )
            }
        }
    }

    /**
     * 页面跳转 带有requestCode
     */
    fun intentToJump2(
        mActivity: Activity, cls: Class<*>,
        bundle: Bundle? = null, isFinish: Boolean = false,
        enterAnimID: Int = -1, exitAnimID: Int = -1,
        flag: Int = -1, startActivityLauncher: ActivityResultLauncher<Intent>
    ) {
        val intent = Intent(mActivity, cls)
        intent.apply {
            bundle?.let { putExtras(it) }
            if (flag != -1) flags = flag

            startActivityLauncher.launch(this)

            if (isFinish) mActivity.finish()
            if (enterAnimID == -1 && exitAnimID == -1) {
                return
            } else {
                mActivity.overridePendingTransition(
                    if (enterAnimID == -1) 0 else enterAnimID,
                    if (exitAnimID == -1) 0 else exitAnimID
                )
            }
        }
    }

    /**
     * Acticity 页面关闭,可以传动动画文件
     * @param mActivity
     * @param exitAnimID
     */
    fun activityFinish(activity: Activity, enterAnimID: Int = 0, exitAnimID: Int = 0) {
        activity.finish()
        activity.overridePendingTransition(enterAnimID, exitAnimID)
    }


    /**
     * 动态设置Shape  RECTANGLE        背景色或者渐变色
     * @param view                    被设置的对象view
     * @param CornerRadiusLeftTop     左上角度数
     * @param CornerRadiusRightTop    右上角度数
     * @param CornerRadiusLeftBottom  左下角度数
     * @param CornerRadiusRightBottom 右下角度数
     * @param strokeWidth             边线的宽度
     * @param strokeColor             边线的颜色
     * @param orientation             渐变方向
     * @param bgColor                 渐变色
     */
    fun setDynamicShapeRectangle(
        view: View,
        strokeWidth: Int = 0, strokeColor: String = "",
        CornerRadiusLeftTop: Float = 0f, CornerRadiusRightTop: Float = 0f,
        CornerRadiusLeftBottom: Float = 0f, CornerRadiusRightBottom: Float = 0f,
        orientation: GradientDrawable.Orientation?, vararg bgColor: String
    ) {
        val drawable = GradientDrawable()
        drawable.apply {
            //设置shape的形状
            shape = GradientDrawable.RECTANGLE
            //设置shape的圆角度数
            val radius = floatArrayOf(
                CornerRadiusLeftTop,
                CornerRadiusLeftTop,
                CornerRadiusRightTop,
                CornerRadiusRightTop,
                CornerRadiusRightBottom,
                CornerRadiusRightBottom,
                CornerRadiusLeftBottom,
                CornerRadiusLeftBottom
            )
            cornerRadii = radius
            //设置shape的边的宽度和颜色
            if (strokeWidth != -1 && !isBlank(strokeColor)) setStroke(
                strokeWidth,
                Color.parseColor(strokeColor)
            )
            //设置shape的背景色
            if (!isBlank(bgColor)) {
                if (bgColor.size > 1 && !isBlank(orientation)) {
                    val colors = IntArray(bgColor.size)
                    for (i in bgColor.indices) {
                        colors[i] = Color.parseColor(bgColor[i])
                    }
                    setOrientation(orientation)
                    setColors(colors)
                } else {
                    setColor(Color.parseColor(bgColor[0]))
                }
            }
            view.background = this
        }
    }

    fun setDynamicShapeRectangle2(
        view: Array<View>,
        strokeWidth: Int = 0, strokeColor: String = "",
        CornerRadiusLeftTop: Float = 0f, CornerRadiusRightTop: Float = 0f,
        CornerRadiusLeftBottom: Float = 0f, CornerRadiusRightBottom: Float = 0f,
        orientation: GradientDrawable.Orientation?, vararg bgColor: String
    ) {
        val drawable = GradientDrawable()
        drawable.apply {
            //设置shape的形状
            shape = GradientDrawable.RECTANGLE
            //设置shape的圆角度数
            val radius = floatArrayOf(
                CornerRadiusLeftTop,
                CornerRadiusLeftTop,
                CornerRadiusRightTop,
                CornerRadiusRightTop,
                CornerRadiusRightBottom,
                CornerRadiusRightBottom,
                CornerRadiusLeftBottom,
                CornerRadiusLeftBottom
            )
            cornerRadii = radius
            //设置shape的边的宽度和颜色
            if (strokeWidth != -1 && !isBlank(strokeColor)) setStroke(
                strokeWidth,
                Color.parseColor(strokeColor)
            )
            //设置shape的背景色
            if (!isBlank(bgColor)) {
                if (bgColor.size > 1 && !isBlank(orientation)) {
                    val colors = IntArray(bgColor.size)
                    for (i in bgColor.indices) {
                        colors[i] = Color.parseColor(bgColor[i])
                    }
                    setOrientation(orientation)
                    setColors(colors)
                } else {
                    setColor(Color.parseColor(bgColor[0]))
                }
            }

            view.forEach {
                it.background = this
            }
        }
    }

    /**
     * 动态设置Shape OVAL
     * @param view 被设置的对象view
     * @param strokeWidth 边线的宽度
     * @param strokeColor 边线的颜色
     * @param width 圆的大小
     * @param bgColor 圆的背景色
     */
    fun setDynamicShapeOVAL(
        view: View,
        width: Int = 0, bgColor: String,
        strokeWidth: Int = 0, strokeColor: String = ""
    ) {
        val drawable = GradientDrawable()
        drawable.apply {
            //设置shape的形状
            shape = GradientDrawable.OVAL
            //设置shape的边的宽度和颜色
            if (strokeWidth != -1 && !isBlank(strokeColor)) setStroke(
                strokeWidth,
                Color.parseColor(strokeColor)
            )
            //设置shape的背景色
            if (!isBlank(bgColor)) setColor(Color.parseColor(bgColor))
            //设置圆的size大小
            setSize(width, width)


            view.background = this
        }
    }

    /**
     * 判断是否安装过该应用
     * @param packageName 被检测的应用包名
     */
    fun isInstallApp(context: Context, packageName: String): Boolean {
        //获取packagemanager
        val packageManager = context.packageManager
        //获取所有已安装程序的包信息
        val packageInfos = packageManager.getInstalledPackages(0)
        if (!isBlank(packageInfos, packageName)) {
            for (packageinfo in packageInfos) {
                if (packageName == packageinfo.packageName) {
                    return true
                }
            }
        }
        return false
    }


    /**
     * 运行handler
     */
    fun runHandlerFun(handler: Handler, whatcode: Int, delayMillis: Long) {
        with(Message.obtain()) {
            what = whatcode
            handler.sendMessageDelayed(this, delayMillis)
        }
    }

    fun runHandlerFun(handler: Handler, whatcode: Int, delayMillis: Long, param: Any?) {
        with(Message.obtain()) {
            what = whatcode
            obj = param
            handler.sendMessageDelayed(this, delayMillis)
        }
    }

    /**
     * 显示HTML内容
     */
    fun showHTMLContent(textView: TextView, content: String) {
        if (isBlank(textView)) return
        // 溢出滚动
        textView.movementMethod = ScrollingMovementMethod.getInstance()

        var result = content.replace("<p>", "", true).replace("</p>", "", true)

        var charSequence: CharSequence
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(result, Html.FROM_HTML_MODE_LEGACY)
        } else {
            charSequence = Html.fromHtml(result)
        }
        textView.text = charSequence
    }

    /**
     * 文本显示限定长度
     */
    fun textViewShowLimitedLength(content: String, maxLen: Int): String {
        content.isNotNull({
            var count = 0
            var endIndex = 0
            val itemArray = content.toCharArray()
            for (i in content.indices) {
                val item = itemArray[i]
                if (item.code < 128) {
                    count += 1
                } else {
                    count += 2
                }
                if (maxLen == count || (item.code >= 128 && maxLen + 1 == count)) {
                    endIndex = i
                }
            }
            return if (count <= maxLen) {
                content
            } else {
                "${content.substring(0, endIndex)}..."
            }

        }, {
            return content
        })
        return ""
    }


    /**
     * 图片压缩---质量压缩
     * 图片的大小是没有变的，因为质量压缩不会减少图片的像素，它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的，这也是为什么该方法叫质量压缩方法
     * 图片的长，宽，像素都不变，那么bitmap所占内存大小是不会变
     * 看到bytes.length是随着quality变小而变小的。这样适合去传递二进制的图片数据，比如微信分享图片，要传入二进制数据过去，限制32kb之内
     * png格式，quality就没有作用了，bytes.length不会变化，因为png图片是无损的，不能进行压缩
     */
    fun qualityCompress(context: Context, @DrawableRes DrawableId: Int): Bitmap {
        var bitmap = BitmapFactory.decodeResource(context.resources, DrawableId)
        LogUtils.iTag(
            ConfigConstants.CONSTANT.TAG_ALL,
            "压缩前图片的大小 =-= ${(bitmap.byteCount / 1024 / 1024)}",
            "宽度 =-= ${bitmap.width}",
            "高度 =-= ${bitmap.height}"
        )

        for (i in 1..100 step 10) {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, i, baos);
            val bytes = baos.toByteArray()
            val isBm = ByteArrayInputStream(bytes) // 把压缩后的数据baos存放到ByteArrayInputStream中
            bitmap = BitmapFactory.decodeStream(isBm, null, null);
            LogUtils.iTag(
                ConfigConstants.CONSTANT.TAG_ALL, "压缩后图片的大小" + (bitmap.getByteCount() / 1024 / 1024)
                        + "M 宽度为" + bitmap.getWidth() + " 高度为" + bitmap.getHeight()
                        + " bytes.length=" + (bytes.size / 1024) + "KB"
                        + " quality=" + i
            )
        }
        return bitmap
    }

    /**
     * 图片压缩---采样率压缩
     * 置inSampleSize的值(int类型)后，假如设为2，则宽和高都为原来的1/2，宽高都减少了，自然内存也降低了
     * 代码没用过options.inJustDecodeBounds = true; 因为我是固定来取样的数据，为什么这个压缩方法叫采样率压缩，是因为配合inJustDecodeBounds，先获取图片的宽、高【这个过程就是取样】，然后通过获取的宽高，动态的设置inSampleSize的值
     * inJustDecodeBounds设置为true的时候，BitmapFactory通过decodeResource或者decodeFile解码图片时，将会返回空(null)的Bitmap对象，这样可以避免Bitmap的内存分配，但是它可以返回Bitmap的宽度、高度以及MimeType
     */
    fun samplingRateCompress(context: Context, DrawableId: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val inputStream: InputStream = context.resources.openRawResource(DrawableId)
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        val imageType = options.outMimeType
        //        LogUtils.i("压缩前图片的大小" + (bitmap.getByteCount() / 1024 / 1024)
//                + "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
        options.inSampleSize = 1
        options.inJustDecodeBounds = false
        val bitmap1 =
            BitmapFactory.decodeStream(context.resources.openRawResource(DrawableId), null, options)
        LogUtils.iTag(
            ConfigConstants.CONSTANT.TAG_ALL,
            "压缩后图片的大小" + bitmap1!!.byteCount / 1024 / 1024
                    + "M 宽度为" + bitmap1.width + " 高度为" + bitmap1.height
        )
        return bitmap1
    }


    /**
     * 隐藏虚拟栏 ，显示的时候再隐藏掉
     *
     * @param window
     */
    fun hideNavigationBar(window: Window) {
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        window.decorView
            .setOnSystemUiVisibilityChangeListener(OnSystemUiVisibilityChangeListener {
                var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                uiOptions = if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                } else {
                    uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
                }
                window.getDecorView().setSystemUiVisibility(uiOptions)
            })
    }

    /**
     * double计算加法
     */
    fun doubleAdd(double1: Double, double2: Double): Double {
        val b1 = BigDecimal(double1.toString())
        val b2 = BigDecimal(double2.toString())
        return b1.add(b2).toDouble()
    }



}