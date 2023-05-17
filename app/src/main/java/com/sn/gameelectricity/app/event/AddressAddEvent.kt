package com.sn.gameelectricity.app.event

data class AddressAddEvent(
    var actionType: Int,
    var actionPosition: Int
) {

    companion object {
        val EVENT_ADDRESSADD_CODE = 0x1001
    }
}