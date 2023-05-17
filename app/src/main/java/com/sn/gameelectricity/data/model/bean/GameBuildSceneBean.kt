package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/1
 * Time:19:46
 * author:dimple
 */
data class GameBuildSceneBean(
    val build_slot_1: BuildSlot1,
    val build_slot_2: BuildSlot2,
    val build_slot_3: BuildSlot3,
    val build_slot_4: BuildSlot4,
    val build_slot_5: BuildSlot5,
    val build_slot_6: BuildSlot6,
    val build_slot_7: BuildSlot7,
    val build_slot_8: BuildSlot8,
    val build_slot_9: BuildSlot9,
    val id: Int,
    val sceneId: Int,
    val sceneImg: String
)

data class BuildSlot1(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot2(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot3(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int

)

data class BuildSlot4(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot5(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot6(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot7(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot8(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)

data class BuildSlot9(
    val buildId: Int,
    val buildImg: String,
    val buildName: String,
    val buildType: Int,
    val topicId: Int
)