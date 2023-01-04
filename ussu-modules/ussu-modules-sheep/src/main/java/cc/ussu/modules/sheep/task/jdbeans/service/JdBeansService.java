package cc.ussu.modules.sheep.task.jdbeans.service;

import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jdbeans.vo.*;
import cn.hutool.json.JSONObject;

import java.util.List;

public interface JdBeansService {

    JSONObject getStatisticData(String ck);

    /**
     * 总资产
     */
    TotalBeanResponseVo.TotalBeanResponseDataVo totalBean(String cookie) throws JdCookieExpiredException, RequestErrorException, ResponseResultException;

    /**
     * 总资产2
     */
    TotalBean2ResponseVo totalBean2(String cookie) throws RequestErrorException;

    /**
     * 钱包信息接口
     */
    MyWalletInfoResponse myWalletInfo(String cookie);

    /**
     * 今日收入京豆
     */
    @Deprecated
    TodayBeanVO todayBean(String cookie);

    /**
     * 今日收入京豆
     */
    TodayBeanVO todayBean2(String cookie);

    /**
     * 今日喜豆收入
     */
    TodayJxBeanVo todayJxBean(String cookie);

    /**
     * 查询京豆详情
     */
    @Deprecated
    List<JdDetailResponseVo.DetailList> getJingBeanDetail(String ck, Integer page) throws RequestErrorException, JdCookieExpiredException, ResponseResultException;

    /**
     * 查询京豆详情
     */
    List<JdDetailResponseVo.DetailList> getJingBeanDetail2(String ck, Integer page) throws RequestErrorException, JdCookieExpiredException, ResponseResultException;

    /**
     * 查询喜豆详情
     */
    List<TodayJxBeanDetailResponseVo.Detail> getXiBeanDetail(String ck);

    /**
     * 查询过期京豆
     */
    List<JdExpireResponseVo.Expirejingdou> getExpireJingBean(String ck);

    /**
     * 过期京豆兑换喜豆
     */
    @Deprecated
    Object expireJdExchangeJxBeans(String ck, Integer expireJdNum);

    /**
     * 获取红包金额
     */
    RedPacketRespoonseVo getRedPacket(String ck);

    /**
     * 汪汪乐园
     */
    void getJoyPark(String cookie);

    /**
     * 京东赚赚
     */
    JdzzResponseVo.JdzzDataResponseVo getJdZZ(String cookie);

    /**
     * 京东秒杀
     */
    JdMsResponseVo.JdMsResultAssignmentVo getMs(String cookie);

    /**
     * 东东农场
     */
    JdFruitInitForFarmResponseVo jdFruit(String cookie);

    /**
     * 极速金币
     */
    JdSpeedCoinResponseVo.JdSpeedCoinDataVo jdSpeedCoin(String cookie);

    /**
     * 东东萌宠
     */
    JdPetInitPetTownResponseVo.JdPetInitPetTownResult jdPet(String ck);

    /**
     * 京喜牧场
     */
    @Deprecated
    void getJxmc(String ck);

    /**
     * 京喜工厂
     */
    @Deprecated
    void getJxFactory(String ck);

    /**
     * 京东工厂
     */
    @Deprecated
    void getDdFactory(String ck);

    /**
     * 领现金
     */
    JdCashResponseVo.JdCashDataVo jdCash(String ck);

    /**
     * 喜豆查询
     */
    JxBeanQuerybeanamountResponseVo.JxBeanQuerybeanamountDataVo jxBean(String ck);

}
