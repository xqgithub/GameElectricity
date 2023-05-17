package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/5/27
 * Time:17:09
 * author:dimple
 * 所选择的区域信息 实体类
 */
data class AreaSelectedBean(
    var areaCode_Province: Int = -1,
    var areaCode_City: Int = -1,
    var areaCode_Area: Int = -1,
    var areaCode_Street: Int = -1,
    var mProvince: String = "",
    var mCity: String = "",
    var mArea: String = "",
    var mStreet: String = ""
)