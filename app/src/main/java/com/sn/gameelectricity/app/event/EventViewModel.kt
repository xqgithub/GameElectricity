package com.sn.gameelectricity.app.event

import com.sn.gameelectricity.data.model.bean.AddWishBus
import com.sn.gameelectricity.data.model.bean.GashaponHomepageResponse
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * 描述　:APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 */
class EventViewModel : BaseViewModel() {
    //全局添加心愿购，监听该值的界面都会收到消息
    val addWishEvent = EventLiveData<AddWishBus>()

    val gashaponHomepageEvent = EventLiveData<GashaponHomepageResponse>()

    val choiseIndex = EventLiveData<Int>()

    val choiseHomeTop = EventLiveData<Int>()

    val refreshHomeDaily = EventLiveData<Int>()

    val earncoins = EventLiveData<Int>()

}