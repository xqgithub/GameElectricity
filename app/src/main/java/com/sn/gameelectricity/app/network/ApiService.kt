package com.sn.gameelectricity.app.network

import com.sn.gameelectricity.BuildConfig
import com.sn.gameelectricity.data.model.bean.*
import io.reactivex.rxjava3.core.Observable
import me.hgj.jetpackmvvm.network.BaseDataBean
import me.hgj.jetpackmvvm.network.BaseEntity
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*

/**
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val BASE_TEST_YAPI = "http://yapi.aifun.com/"

        //        const val BASE_URL = "https://gmalltest.aifun.com/"
        const val BASE_URL = BuildConfig.baseurl
    }

    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopAritrilList(): ApiResponse<ArrayList<AriticleResponse>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getAritrilList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>


    @POST("mock/181/activity/v1/homepage/my/focus/list")
    fun myFocusList(
        @Query("keyword") keyword: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseEntity<List<FocusListBean>>>

    /**
     * 获取COS临时密钥
     */
    @GET("cos/credential")
    fun cosCredential(): Observable<BaseEntity<CosBean>>

    /**
     * 收货地址列表
     */
    @GET("user/delivery/address/list")
    fun deliveryAddressList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseEntity<List<DeliveryAddressBean>>>

    /**
     * 地区列表
     */
    @GET("user/delivery/address/address/areaList")
    fun deliveryAddressAreaList(
        @Query("areaCode") areaCode: Int,
        @Query("areaLevel") areaLevel: Int,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
    ): Observable<BaseEntity<List<AreaBean>>>

    /**
     * 保存收货地址
     */
    @PUT("user/delivery/address/save")
    fun deliveryAddressSave(@Body body: RequestBody): Observable<BaseEntity<AddressSaveResponse>>

    /**
     * 更新收货地址
     */
    @PUT("user/delivery/address/updateById")
    fun deliveryAddressupdateById(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 更改收货地址
     */
    @GET("delivery/updateAddress")
    fun updateOrderAddressId(
        @Query("addressId") addressId: Int,
        @Query("orderId") orderId: Int
    ): Observable<BaseEntity<Any>>

    /**
     * 删除收货地址
     */
    @DELETE("user/delivery/address/delete/{id}")
    fun deliveryAddressDelete(@Path("id") id: Int): Observable<BaseEntity<Any>>


    /**
     * 收货地址详情
     */
    @GET("user/delivery/address/address/{id}")
    fun deliveryAddressDetails(@Path("id") id: Int): Observable<BaseEntity<DeliveryAddressDetails>>


    /**
     * 用户短信发送, 发送短信到用户已绑定的手机号码
     */
    @POST("user/userSendSmsCode")
    fun userSendSmsCode(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 短信发送，手机号需要传入
     */
    @POST("user/sendSmsCode")
    fun userSendSmsCode2(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 校验验证码
     */
    @POST("user/verifySmsCode")
    fun verifySmsCode(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 重设密码
     */
    @PUT("user/resetPassword")
    fun resetPassword(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 获取用户信息
     */
    @GET("user/userInfo")
    fun getuserInfo(): Observable<BaseEntity<PersonalInfo>>

    /**
     * 编辑用户
     */
    @PUT("user/edit")
    fun userEdit(@Body body: RequestBody): Observable<BaseEntity<Any>>


    /**
     * 修改手机号码
     */
    @PUT("user/changeMobile")
    fun changeMobile(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 登出
     */
    @POST("user/userLoginOut")
    fun userLoginOut(): Observable<BaseEntity<Any>>

    /**
     * 订单列表
     */
    @GET("user/order/list")
    fun orderList(@QueryMap options: Map<String, Int>): Observable<BaseEntity<List<OrderListbean>>>

    /**
     * 通过id获取订单
     */
    @GET("user/order/getUserOrderById")
    fun orderById(@Query("orderId") orderId: Int): Observable<BaseEntity<OrderDetailsBean>>

    /**
     * 订单搜索
     */
    @GET("user/order/searchUserOrder")
    fun searchUserOrder(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("searchKeywords") searchKeywords: String
    ): Observable<BaseEntity<List<OrderListbean>>>


    /**
     * 创建支付订单
     */
    @POST("user/order/createTransactionOrder")
    fun createTransactionOrder(@Body body: RequestBody): Observable<BaseEntity<CreatOrderResponse>>

    /**
     * 特殊订单完成
     */
    @POST("user/order/specialOrderCompletion")
    fun specialOrderCompletion(@Body body: RequestBody): Observable<BaseEntity<CreatOrderResponse>>

    /**
     * 删除订单
     */
    @PUT("user/order/deleteOrderById")
    fun deleteOrderById(@Body body: RequestBody): Observable<BaseEntity<Any>>


    /**
     * 统计订单
     */
    @GET("user/order/countUserOrder")
    fun countUserOrder(): Observable<BaseEntity<StatisticsOrderBean>>

    /**
     * 取消订单
     */
    @PUT("user/order/cancelOrderById")
    fun cancelOrderById(@Body body: RequestBody): Observable<BaseEntity<Any>>


    /**
     * 物流信息
     */
    @GET("delivery/byDeliveryNo")
    fun deliveryByDeliveryNo(
        @Query("deliveryNo") deliveryNo: String,
        @Query("orderNo") orderNo: String
    ): Observable<BaseEntity<LogisticsInformationBean>>

    /**
     * 游戏建筑
     */
    @GET("game/build/currentGameBuildScene")
    fun gameCurrentGameBuildScene(): Observable<BaseEntity<GameBuildSceneBean>>

    /**
     * 获取启用的新人有礼活动
     */
    @GET("new/user/activity/getByEnabled")
    fun newcomerCeremony(): Observable<BaseEntity<NewbiePoliteBean>>

    /**
     * 做新手引导
     */
    @PUT("new/user/guide/{guideStageId}")
    fun doUserGuide(@Path("guideStageId") guideStageId: Int): Observable<BaseEntity<NewerGuideBean>>

    /**
     * 通过新手引导步骤编号获取明细
     */
    @GET("new/user/guide/{guideStageId}")
    fun userGuideDetails(@Path("guideStageId") guideStageId: Int): Observable<BaseEntity<NewerGuideBean>>

    /**
     * 新手引导兑换
     */
    @POST("new/user/activity/exchange")
    fun exchange(): Observable<BaseEntity<NewbieRedeemGiftsBean>>

    /**
     * 心跳
     */
    @POST("heart")
    fun heart(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 获取离线奖励
     */
    @GET("heart")
    fun getOfflineRewards(): Observable<BaseEntity<Any>>

    /**
     * 获取鹅蛋和饲料
     */
    @GET("game/egg/get")
    fun eggFeedQuantity(): Observable<BaseEntity<GooseEggsFeedBean>>

    /**
     * 投喂饲料
     */
    @GET("game/egg/feed")
    fun feed(): Observable<BaseEntity<GooseEggsFeedBean>>

    /**
     * 兑换鹅蛋
     */
    @GET("game/egg/exchange")
    fun eggExchange(): Observable<BaseEntity<Any>>

    /**
     * 今日任务
     */
    @GET("game/task/todayBuildTaskList")
    fun todayBuildTaskList(): Observable<BaseEntity<List<TodayBuildTaskBean>>>

    /**
     * 领取任务
     */
    @PUT("user/game/task/receiveTask")
    fun receiveTask(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 任务完成
     */
    @PUT("user/game/task/simpleTaskCompletion")
    fun simpleTaskCompletion(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 放弃任务
     */
    @PUT("user/game/task/giveUpGameTask")
    fun giveUpGameTask(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 领取奖励
     */
    @PUT("user/game/task/receiveGameTaskRewards")
    fun receiveGameTaskRewards(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 动态事件列表
     */
    @GET("user/friend/dynamic/event/list")
    fun eventList(
        @Query("userId") userId: Int,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseEntity<List<DynamicEventBean>>>

    /**
     * 好友列表
     */
    @GET("user/friend/list")
    fun friendList(
        @Query("userId") userId: Int,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseEntity<friendBean>>

    /**
     * 领取邀请奖励
     */
    @PUT("user/friend/receive/invitation/reward")
    fun receiveInvitationReward(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 账号注销
     */
    @PUT("user/cancellationAccount")
    fun cancellationAccount(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 排行榜
     */
    @GET("user/coupon/getRankList")
    fun getRankList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseEntity<List<LeaderBoardBean>>>

    /**
     * 获取本人排行
     */
    @GET("user/coupon/rank")
    fun getMyRank(): Observable<BaseEntity<LeaderBoardBean>>


    /**
     * 获取权益中心列表
     */
    @GET("user/coupon/right/list")
    fun couponRightList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("userId") userId: Int
    ): Observable<BaseEntity<CouponRightBean>>


    /**  ================================================================================================================================  **/

    /**
     * 短信发送
     */
    @POST("user/sendSmsCode")
    fun sendSmsCode(@Body body: RequestBody): Observable<BaseEntity<UserInfo>>

    /**
     * 密码登陆注册
     */
    @POST("user/userRegisterOrLoginByPassword")
    fun userRegisterOrLoginByPassword(@Body body: RequestBody): Observable<BaseEntity<UserInfo>>

    /**
     * 短信登陆注册
     */
    @POST("user/userRegisterOrLoginBySmsCode")
    fun userRegisterOrLoginBySmsCode(@Body body: RequestBody): Observable<BaseEntity<UserInfo>>

    /**
     * 重设密码
     */
    @PUT("user/resetPassword")
    fun userRresetPassword(@Body body: RequestBody): Observable<BaseEntity<UserInfo>>

    /**
     * 该手机是否注册
     */
    @GET("user/boolUserRegister")
    fun boolUserRegister(
        @Query("mobile") mobile: String
    ): Observable<BaseEntity<BoolUserRegisterResponse>>

    /**
     * 主题列表
     */
    @GET("topic/list")
    fun topicList(): Observable<BaseDataBean<TopicListResponse>>

    /**
     * 每日必抢
     */
    @GET("goods/everyday")
    fun goodsEveryday(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseDataBean<GoodsEveryResponse>>


    /**
     * 商品列表
     */
    @GET("goods/page")
    fun goodsPage(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("topicId") topicId: Int
    ): Observable<BaseDataBean<GoodsPageResponse>>

    /**
     * 商品详情
     */
    @GET("goods/getGoods")
    fun goodsGetGoods(
        @Query("goodsId") goodsId: Int
    ): Observable<BaseEntity<GoodsGetgoodsResponse>>

    /**
     * 金币抵扣
     */
    @GET("goods/getDiscount")
    fun goodsGetDiscount(
        @Query("goodsId") goodsId: Int
    ): Observable<BaseEntity<GoodsGetDiscountResponse>>


    /**
     * 创建订单
     */
    @POST("user/order/createOrder")
    fun createOrder(@Body body: RequestBody): Observable<BaseEntity<CreatOrderResponse>>


    /**
     * 查询交易结果
     */
    @GET("user/order/queryPayResult")
    fun queryOrder(
        @Query("orderNo") orderNo: String
    ): Observable<BaseEntity<PayStateResponse>>

    /**
     * 创建助力
     */
    @POST("assist")
    fun assist(@Body body: RequestBody): Observable<BaseEntity<CreatAssistResponse>>

    /**
     * 加入助力
     */
    @PUT("assist")
    fun putAssist(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 获取助力
     */
    @GET("assist")
    fun goodsAssist(
        @Query("groupCode") groupCode: String,
        @Query("userId") userId: Int
    ): Observable<BaseEntity<GetAssistResponse>>


    /**
     * 添加心愿购
     */
    @POST("wish")
    fun wish(@Body body: RequestBody): Observable<BaseEntity<Any>>

    /**
     * 是否需要签到
     */
    @GET("checkin/activity/isNeedCheckin")
    fun isNeedCheckin(): Observable<BaseEntity<Any>>

    /**
     * 签到
     */
    @POST("checkin/activity/checkin")
    fun checkin(@Body body: RequestBody): Observable<BaseEntity<CheckinResponse>>

    /**
     * 获取最新版本
     */
    @GET("version")
    fun checkVersion(
        @Query("version") version: String,
        @Query("osType") osType: Int
    ): Observable<BaseEntity<VersionResponse>>


    /**
     * 获取用户奖池
     */
    @GET("gashapon/user/award/pool")
    fun awardPool(): Observable<BaseEntity<AwardPoolResponse>>

    /**
     * 刷新用户奖池
     */
    @PUT("gashapon/user/award/pool")
    fun putAwardPool(): Observable<BaseEntity<AwardPoolResponse>>

    /**
     * 冲冲冲
     */
    @POST("everyday/activity/{everydayActivityId}")
    fun everydayActivity(@Path("everydayActivityId") everydayActivityId: Int): Observable<BaseEntity<Any>>

    /**
     * 摇奖
     */
    @GET("gashapon/play")
    fun gashaponPlay(): Observable<BaseDataBean<GashaponPlayResponse>>

    /**
     * 扭蛋首页
     */
    @GET("gashapon/homepage")
    fun gashaponHomepage(): Observable<BaseEntity<GashaponHomepageResponse>>

    /**
     * 获取回收内容
     */
    @GET("gashapon/user/award/get")
    fun awardGet(
        @Query("goodsId") goodsId: Int
    ): Observable<BaseEntity<AwardGetResponse>>

    /**
     * 回收奖品
     */
    @PUT("gashapon/user/award/recover")
    fun awardRecover(
        @Query("goodsId") goodsId: Int,
        @Query("orderId") orderId: Int,
        @Query("recoverType") recoverType: Int,
        @Query("number") number: Int,
        @Query("userId") userId: Int
    ): Observable<BaseEntity<Any>>


    /**
     * 心愿购商品列表
     */
    @GET("goods/all")
    fun goodsAll(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
    ): Observable<BaseDataBean<GoodsAllResponse>>

    /**
     * 获取权益中心列表
     */
    @GET("user/coupon/right/list")
    fun goodsRightList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("userId") userId: Int
    ): Observable<BaseEntity<GoodsRightListResponse>>


    /**
     * 获取用户权益券
     */
    @GET("user/coupon/user")
    fun userCoupon(): Observable<BaseEntity<UserCouponResponse>>

    /**
     * 获取权益商品详情
     */
    @GET("user/coupon/right/detail")
    fun goodsRightDetail(
        @Query("goodsId") goodsId: Int,
        @Query("userId") userId: Int
    ): Observable<BaseEntity<GoodsRightDetailResponse>>

    /**
     * 权益兑换
     */
    @POST("rights/activity/{rightsActivityId}")
    fun rightsActivity(
        @Path("rightsActivityId") rightsActivityId: Int
    ): Observable<BaseEntity<Any>>


    /**
     * 创建权益订单
     */
    @POST("user/order/createRightsOrder")
    fun createRightsOrder(@Body body: RequestBody): Observable<BaseEntity<CreatRightOrderResponse>>

}