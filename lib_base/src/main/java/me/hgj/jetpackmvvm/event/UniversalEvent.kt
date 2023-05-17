package me.hgj.jetpackmvvm.event

/**
 * Date:2022/5/16
 * Time:9:25
 * author:dimple
 * EventBUG 对象传递
 */
data class UniversalEvent(
    var actionType: Int,
    var message: Any?
) {

    companion object {
        val EVENT_VERIFICATION_CODE = 0x001
        val EVENT_DELIVERYADDRESSLIST_REFRESH = 0x002
        val EVENT_DELIVERY_ADDRESS = 0x003
        val EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION = 0x004
        val EVENT_MAINACTIVITY_ENDTRANSITIONS_ANIMATION = 0x005
        val EVENT_ORDERLIST_REFRESH = 0x006
        val EVENT_MAINACTIVITY_REFRESH_CLMASKLAYER = 0x007
        val EVENT_JUMP_HOMEFRAGMENT = 0x008
        val EVENT_NEWER_GUIDE_STEP1 = 0x009
        val EVENT_NEWER_GUIDE_STEP2 = 0x010
        val EVENT_NEWER_GUIDE_STEP3 = 0x011
        val EVENT_NEWER_GUIDE_STEP4 = 0x012
        val EVENT_MONEYTASKDIALOG_DISSMISS = 0x013
        val EVENT_TODAY_BUILDTASKLIST_REFRESH = 0x015
        val EVENT_JUMP_HOMEFRAGMENT2 = 0x016
        val EVENT_OPEN_DYNAMICDIALOG = 0x017
        val EVENT_HOMEHOTFRAGMENT_HOMEDAILYROBFRAGMENT_BYRECYCLERVIEW_SLIDE = 0x018
        val event_mainactivity_start_cloud_working_animation = 0x019
        val event_feed_refresh = 0x020
    }
}