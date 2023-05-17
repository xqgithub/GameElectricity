package com.sn.gameelectricity.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.baidu.location.*
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.app.App
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants

/**
 *
 * @ProjectName:    AIFUN-ANDROID
 * @Package:        com.snai.aifun.ui.scriptkillinggame.model
 * @ClassName:      LocationViewModel
 * @Description:     java类作用描述
 * @Author:         czhen
 * @CreateDate:     2021/5/18 11:50
 */
class LocationViewModel : BaseViewModel() {
    private val client = LocationClient(App.instance)
    private val locationListener = LocationListener()
    val locationLiveData = MutableLiveData<BDLocation>()


    /**
     *
     * @param scanSpan Int 定位时间间隔，默认为0即为一次
     */
    fun fetchLocation(scanSpan: Int = 0): MutableLiveData<BDLocation> {
        client.registerLocationListener(locationListener)
        client.locOption = getClientOption(scanSpan)
        client.start()
        return locationLiveData
    }

    override fun onCleared() {
        super.onCleared()
        client.unRegisterLocationListener(locationListener)
    }

    /**
     * 停止定位
     */
    private fun stopPositioning() {
        client.unRegisterLocationListener(locationListener)
        client.stop()
    }

    private fun getClientOption(scanSpan: Int): LocationClientOption {
        val option = LocationClientOption()
        option.locationMode =
            LocationClientOption.LocationMode.Hight_Accuracy //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll") //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setScanSpan(scanSpan) //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true) //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true) //可选，设置是否需要地址描述
        option.setNeedDeviceDirect(true) //可选，设置是否需要设备方向结果
        option.openGps = true
        option.isLocationNotify = false //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true) //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true) //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true) //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false) //可选，默认false，设置是否收集CRASH信息，默认收集

        return option
    }

    inner class LocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(p0: BDLocation?) {
            p0?.let {
                LogUtils.iTag(
                    ConfigConstants.CONSTANT.TAG_ALL,
                    "onReceiveLocation:${it.locType} ---- ${it.latitude} - ${it.longitude}"
                )
                if (!TextUtils.isEmpty(it.addrStr)) {
                    ConfigConstants.VARIABLE.longitude = it.longitude.toString()
                    ConfigConstants.VARIABLE.latitude = it.latitude.toString()
                    locationLiveData.postValue(it)
                }
                stopPositioning()
            }
        }

    }
}