package cc.ussu.modules.sheep.task.jdbeans.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 红包响应
 */
@Data
public class RedPacketRespoonseVo {

    private Integer errcode;
    private String msg;
    private RedPacketData data;

    private static final String JING_XI = "京喜";
    private static final String JI_SU = "极速";
    private static final String JIAN_KANG = "健康";

    // 扩展方法 start
    private List<RedVo> getRed() {
        return getData().getUseRedInfo().getRedList();
    }

    public BigDecimal getRedTotal() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        getRed().stream().forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    public BigDecimal getRedExpire() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        String end = DateUtil.formatDateTime(DateUtil.endOfDay(new Date()));
        getRed().stream()
                .filter(r -> {
                    Long endTime = r.getEndTime();
                    String s = DateUtil.formatDateTime(new Date(endTime * 1000));
                    return end.equals(s);
                })
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    /**
     * 京东红包
     */
    public BigDecimal getJdRedTotal() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        getRed().stream().filter(r -> !StrUtil.equalsAny(r.getOrgLimitStr(), JING_XI, JI_SU, JIAN_KANG))
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    public BigDecimal getJdRedExpire() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        String end = DateUtil.formatDateTime(DateUtil.endOfDay(new Date()));
        getRed().stream().filter(r -> !StrUtil.equalsAny(r.getOrgLimitStr(), JING_XI, JI_SU, JIAN_KANG))
                .filter(r -> {
                    Long endTime = r.getEndTime();
                    String s = DateUtil.formatDateTime(new Date(endTime * 1000));
                    return end.equals(s);
                })
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    /**
     * 京喜红包
     */
    public BigDecimal getJxRedTotal() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        getRed().stream().filter(r -> StrUtil.equals(JING_XI, r.getOrgLimitStr()))
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    public BigDecimal getJxRedExpire() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        String end = DateUtil.formatDateTime(DateUtil.endOfDay(new Date()));
        getRed().stream().filter(r -> StrUtil.equals(JING_XI, r.getOrgLimitStr()))
                .filter(r -> {
                    Long endTime = r.getEndTime();
                    String s = DateUtil.formatDateTime(new Date(endTime * 1000));
                    return end.equals(s);
                })
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    /**
     * 极速红包
     */
    public BigDecimal getJsRedTotal() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        getRed().stream().filter(r -> StrUtil.equals(JI_SU, r.getOrgLimitStr()))
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    public BigDecimal getJsRedExpire() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        String end = DateUtil.formatDateTime(DateUtil.endOfDay(new Date()));
        getRed().stream().filter(r -> StrUtil.equals(JI_SU, r.getOrgLimitStr()))
                .filter(r -> {
                    Long endTime = r.getEndTime();
                    String s = DateUtil.formatDateTime(new Date(endTime * 1000));
                    return end.equals(s);
                })
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    /**
     * 京东健康红包
     */
    public BigDecimal getJdhRedTotal() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        getRed().stream().filter(r -> StrUtil.equals(JIAN_KANG, r.getOrgLimitStr()))
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }

    public BigDecimal getJdhRedExpire() {
        AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal("0"));
        String end = DateUtil.formatDateTime(DateUtil.endOfDay(new Date()));
        getRed().stream().filter(r -> StrUtil.equals(JIAN_KANG, r.getOrgLimitStr()))
                .filter(r -> {
                    Long endTime = r.getEndTime();
                    String s = DateUtil.formatDateTime(new Date(endTime * 1000));
                    return end.equals(s);
                })
                .forEach(r -> total.set(NumberUtil.add(total.get(), new BigDecimal(r.getBalance()))));
        return NumberUtil.roundDown(total.get(), 2);
    }
    // 扩展方法 end

    @Data
    public class RedPacketData {
        private Integer avaiCount;
        private String balance;
        private String countdownTime;
        private Integer expireType;
        private Integer expiredBalance;
        private String isDiffExpires;
        private Long serverCurrTime;
        private UseRedInfo useRedInfo;
    }

    @Data
    public class UseRedInfo {
        private Integer count;
        private List<RedVo> redList;
    }

    @Data
    public class RedVo {
        /**
         * 京喜财富岛
         */
        private String activityName;
        /**
         * 0.01
         */
        private String balance;
        /**
         *
         */
        private Long beginTime;
        private Long endTime;
        /**
         * 2022.03.28 00:00:00-2022.06.25 23:59:59之间因退款退回，支持延期3天使用，最多支持1次延期
         */
        private String delayRemark;
        private String discount;
        private String hbId;
        private Integer hbState;
        private Integer hourLabel;
        private Boolean isDelay;
        /**
         * 仅限京东购物小程序使用
         */
        private String limitStr;
        /**
         * 仅限京喜渠道使用
         */
        private String orgLimitStr;
        /**
         * 仅限京喜拼拼以外商品使用
         */
        private String skuLimitStr;
        /**
         *
         */
        private String stationLimitStr;
    }

}
