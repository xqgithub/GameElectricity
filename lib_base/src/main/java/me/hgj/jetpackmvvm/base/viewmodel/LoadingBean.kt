package me.hgj.jetpackmvvm.base.viewmodel

import me.hgj.jetpackmvvm.R


/**
 * Date:2022/7/1
 * Time:17:54
 * author:dimple
 * loading实体类
 */
data class LoadingBean(
    var type: Int = 3,
    var icon: Int = R.drawable.ge_loading_chong,
    var content: String = "正在拼尽全力冲...",
    var dialogBg: String = "#00000000",
    var barColor: Int = R.color.white
)
