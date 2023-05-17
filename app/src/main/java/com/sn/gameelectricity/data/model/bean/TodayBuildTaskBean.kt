package com.sn.gameelectricity.data.model.bean

import java.io.Serializable

/**
 * Date:2022/6/11
 * Time:17:05
 * author:dimple
 *  今日任务实体类
 */
data class TodayBuildTaskBean(
    val buildType: Int,
    val payloadGameTaskListVO: List<PayloadGameTaskVO>
)

data class PayloadGameTaskVO(
    val adminId: Int?,//管理员id
    val boolReceiveReward: Int?,//是否领取奖励: -1.未领取 1.已领取
    val buildType: Int?,//建筑类型 1.逛一逛 2.鹅窝 3.饲料槽 4.美妆任务 5.休闲任务 6.母婴任务 7.数码任务
    val completeStatus: Int?,//0.已领取 1.进行中 2.放弃 3.完成 4.超时/取消
    val completionTime: String?,//完成时间
    val difficultyLevel: Int?,//难度级别: 1-5级难度
    val eventType: Int?,//事件类型: 1.浏览类 1.1 每日必抢事件 1.2 热门事件 1.3 美妆达人事件 1.4 休闲娱乐事件 1.5 母婴用品事件 1.6 潮流数码事件2.邀请类2.1邀请好友子事件 2.2 邀请助力3.打工类3.1 打工事件4.消费类4.1 开启扭蛋 4.2 积分兑换 4.3 金币抵扣 4.4 半价购 4.5刷新扭蛋奖池5.交互类5.1 助力 5.2 雇佣 5.3 投食 5.4 逛一逛6.养成类6.1 收蛋 6.2 喂养7.领奖类7.1 07:00-11:59事件 7.2 12:00-19:59事件 7.3 20:00-21:59事件 7.4 22:00-23:59事件
    val id: Int?,
    val receiveTime: Long?,
    val taskEndTime: Long?,
    val requestRewardInfoVOList: List<RequestRewardInfoVO>,
    val taskClass: Int?,//任务分类: 1.每日任务 2.成就任务
    val taskContent: String?,//内容
    val taskEffect: String?,//任务动画
    val taskName: String?,//任务名称
    val taskProgress: String?,//任务进度
    val taskTarget: String?,//任务目标
    val taskTargetContent: String?,//任务目标描述
    val taskType: Int?,//任务类型: 1.浏览类 2.邀请类 3.打工类 4.消费类 5.交互类 6.养成类 7.领奖类
    val taskWeight: Int?,//随机权重
    val timeLimit: Int?,//时限: 单位秒 0 为无时间限制
    val userReceiveId: Int?//用户领取任务id
) : Serializable

data class RequestRewardInfoVO(
    val rewardNumber: Int?,//奖励数量
    val rewardType: Int?//奖励类型: 1.金币 2.积分 3.饲料 4.代币 5.贡献
) : Serializable