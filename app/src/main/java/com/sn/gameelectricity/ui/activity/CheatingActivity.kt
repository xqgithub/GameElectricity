package com.sn.gameelectricity.ui.activity

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.*
import com.sn.gameelectricity.app.util.permission.PermissionCallback
import com.sn.gameelectricity.app.util.permission.PermissionRequestManager
import com.sn.gameelectricity.data.model.bean.GetAssResponse
import com.sn.gameelectricity.data.model.bean.GetAssistResponse
import com.sn.gameelectricity.databinding.ActivityCheateingBinding
import com.sn.gameelectricity.ui.adapter.CheatingEmpAdapter
import com.sn.gameelectricity.ui.adapter.CheatingPicsAdapter
import com.sn.gameelectricity.viewmodel.request.RequestCheatingViewModel
import com.sn.gameelectricity.viewmodel.state.CheatingViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cheateing.view.*
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick
import java.io.*
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*

/**
 * 邀请助力
 */
class CheatingActivity :
    BaseActivity1<CheatingViewModel, ActivityCheateingBinding>() {

    private val requestCheatingViewModel: RequestCheatingViewModel by viewModels()
    private lateinit var cheatingPicsAdapter: CheatingPicsAdapter
    private lateinit var cheatingEmpAdapter: CheatingEmpAdapter
    private lateinit var getAssistResponse: GetAssistResponse

    private var mSetupQrCodeCacheImg = false // 是否已经保存
    private var targetPath: String? = null
    private var getAssResponseList: MutableList<GetAssResponse> = mutableListOf()
    private lateinit var drawingView: View

    private val groupCode: String by lazy {
        intent.getStringExtra("groupCode") as String
    }


    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()



        mViewBind.apply {

            imgClose.singleClick {
                finish()
            }


            recyclerView.apply {
                cheatingPicsAdapter = CheatingPicsAdapter(this@CheatingActivity)
                cheatingEmpAdapter = CheatingEmpAdapter(this@CheatingActivity)
                layoutManager = LinearLayoutManager(
                    this@CheatingActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            tvSaveCode.singleClick {
                clickSaveCode()
            }

            tvQq.singleClick {
                LogUtils.e(targetPath)
                ShareManager.shareManager.shareImage2QQ(saveBitmapFileFile(createBitmapByView(drawingView)))
            }
            tvWx.singleClick {
                LogUtils.e(targetPath)
                ShareManager.shareManager.shareImage2WxFriend(
                    saveBitmapFileFile(createBitmapByView(drawingView))
                )
            }
            tvWb.singleClick {
                LogUtils.e(targetPath)
                ShareManager.shareManager.shareImage2Weibo(
                    "前方有羊毛可薅",
                    saveBitmapFileFile(createBitmapByView(drawingView))
                )
            }
        }
    }

    override fun createObserver() {
        CacheUtil.getUser()?.userId?.let { requestCheatingViewModel.getAssist(groupCode, it) }
        requestCheatingViewModel.getAssistLiveData.observe(this, {
            getAssistResponse = it
            loadProductData(it)
            drawingView = LayoutInflater.from(this@CheatingActivity)
                .inflate(R.layout.activity_drawing, null, false)
            loadBitmapByView(drawingView)

        })
    }

    fun loadProductData(getAssistResponse: GetAssistResponse) {
        mViewBind.apply {
            getAssistResponse.inviteCode?.let {
                tvGroupCode.text = getAssistResponse.inviteCode.toString()
            }
        }
        for (index in 1..getAssistResponse.successMemberNum) {
            getAssResponseList.add(GetAssResponse("", "邀请好友"))
        }

        if (!getAssistResponse.memberInfo.isNullOrEmpty()) {
            for ((index2, value) in getAssistResponse.memberInfo.withIndex()) {
                getAssResponseList[index2] = GetAssResponse(value.avatarUrl, value.nickName)
            }
        }
//        getAssResponseList[0] = GetAssResponse(CacheUtil.getUser()?.avatarUrl,CacheUtil.getUser()?.nickName)
        mViewBind.recyclerView.adapter = cheatingPicsAdapter
        cheatingPicsAdapter.setNewData(getAssResponseList)


//        if (getAssistResponse.memberInfo.isNullOrEmpty()) {
//            mViewBind.recyclerView.adapter = cheatingEmpAdapter
//            cheatingEmpAdapter.setNewData(List(getAssistResponse.successMemberNum, { "" }))
//        } else {
//            mViewBind.recyclerView.adapter = cheatingPicsAdapter
//            cheatingPicsAdapter.setNewData(getAssistResponse.memberInfo)
//
//        }

        val differ = TimeUtils.string2Millis(getAssistResponse.endTime) - System.currentTimeMillis()
        getCountDownTime(differ)
    }

    private var timer: CountDownTimer? = null
    private fun getCountDownTime(time: Long) {
        if (time > 0) {
            timer = MakeCountDown(mViewBind.root, time, 1000)
            timer?.start()
        }
    }

    private class MakeCountDown(
        targetView: View,
        millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        private val weakTargetView: WeakReference<View> = WeakReference(targetView)
        private val numberFormat = DecimalFormat("00")

        override fun onTick(millisUntilFinished: Long) {
            val targetView = weakTargetView.get()
            if (targetView != null) {
                val day = millisUntilFinished / (1000 * 24 * 60 * 60) //单位天
                val hour =
                    (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60) //单位时
                val minute =
                    (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60) //单位分
                val second =
                    (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 //单位秒
                targetView.tv31.text =
                    numberFormat.format(minute)
                targetView.tv33.text =
                    numberFormat.format(second)
            }
        }

        override fun onFinish() {
        }

    }

    fun clickSaveCode() {
        PermissionRequestManager.request(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).toSetting()
            .request(PermissionCallback { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    saveQrCodeCacheImg().map(object : Function<Boolean, Boolean> {
                        @Throws(java.lang.Exception::class)
                        override fun apply(@NonNull isSuccess: Boolean): Boolean? {
                            LogUtils.e("isSuccess=====" + isSuccess)
                            return if (isSuccess) {
                                val srcPath = targetPath!!
                                val destPath = SGFileLoader.getInstance()
                                    .getDirectoryPath(SGFileLoader.FileParams.DIR_SAVE) + UUID.randomUUID() + ".jpg"
                                val copyResult = FileUtil.copy(File(srcPath), File(destPath))
                                if (copyResult) {
                                    CommonUtil.updateSystemAlbum(this@CheatingActivity, destPath)
                                }
                                copyResult
                            } else {
                                false
                            }
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Consumer<Boolean> {
                            @Throws(java.lang.Exception::class)
                            override fun accept(@NonNull isSuccess: Boolean) {
                                if (isSuccess) {
                                    ToastUtil.showCustomSmallToast("图片已保存至相册")
                                } else {
                                    ToastUtil.showCustomSmallToast("图片保存失败")
                                }
                            }
                        })
                }
            })
    }

    private fun saveQrCodeCacheImg(): Observable<Boolean> {
        val cache: Bitmap = createBitmapByView(drawingView)
        return Observable.create(ObservableOnSubscribe<Boolean> { e ->
            if (!mSetupQrCodeCacheImg) {
                if (null != cache && !cache.isRecycled) {
                    try {
                        targetPath = SGFileLoader.getInstance()
                            .getTemporaryDir(SGFileLoader.FileParams.DIR_TEMPORARY) + "_" + System.currentTimeMillis()
                        BitmapUtils.writeBitmapToUri(
                            this@CheatingActivity,
                            cache,
                            Uri.fromFile(File(targetPath)),
                            Bitmap.CompressFormat.PNG, 100
                        )
                        LogUtils.e(targetPath)
                        mSetupQrCodeCacheImg = true
                    } catch (exception: FileNotFoundException) {
                        exception.printStackTrace()
                    }
                }
            }
            e.onNext(mSetupQrCodeCacheImg)
        }).subscribeOn(Schedulers.io())
    }

    fun saveBitmapFileFile(bitmap: Bitmap): File {
        val file: File = File(
            SGFileLoader.getInstance()
                .getTemporaryDir(SGFileLoader.FileParams.DIR_TEMPORARY) + "_" + System.currentTimeMillis()
        ) //将要保存图片的路径
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        LogUtils.e("789", file.absolutePath)
        return file
    }


    /**
     * view转bitmap
     *
     * @param view view
     * @return Bitmap
     */
    fun createBitmapByView(view: View): Bitmap {
        //计算设备分辨率
        val manager: WindowManager = this.getWindowManager()
        val metrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        //测量使得view指定大小
        val measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measureHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        view.measure(measureWidth, measureHeight)
        //调用layout方法布局后，可以得到view的尺寸
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    fun loadBitmapByView(view: View) {
        val ivQrcode = view.findViewById<ImageView>(R.id.ivQrcode)
        val tv0005 = view.findViewById<TextView>(R.id.tv0005)
        val tvGroupCode001 = view.findViewById<TextView>(R.id.tvGroupCode001)
        var sivImg0001 = view.findViewById<ImageView>(R.id.sivImg0001)
        tv0005.text = CacheUtil.getUser()?.nickName
//        sivImg0001.loadUserAvatar(getAssistResponse.ownerIcon)
        Glide.with(this).load(getAssistResponse.ownerIcon).apply(RequestOptions.circleCropTransform()).error(R.drawable.default_user_avatar)
            .placeholder(R.drawable.default_user_avatar).into(sivImg0001)
        getAssistResponse.inviteCode?.let {
            tvGroupCode001.text = getAssistResponse.inviteCode.toString()
        }
        createQrCode(
            ivQrcode,
            CacheUtil.getUser()?.loadingUrl + "?groupCode=" + groupCode + "&userCode=" + getAssistResponse.inviteCode
        )
    }

    /**
     * 创建二维码
     *
     * @param imageView 显示的组件
     * @param qrcodeStr 二维码的内容
     */
    private fun createQrCode(imageView: ImageView, qrcodeStr: String) {
        if (TextUtils.isEmpty(qrcodeStr)) {
            return
        }
        val width = 600 // 图像宽度
        val height = 600 // 图像高度
        val format = "png" // 图像类型
        val hints: HashMap<EncodeHintType, Any> = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.MARGIN] = 1
        try {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                qrcodeStr,
                BarcodeFormat.QR_CODE, width, height, hints
            ) // 生成矩阵
            val pixels = IntArray(width * height)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = -0x1000000
                    } else {
                        pixels[y * width + x] = -0x1
                    }
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            imageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}