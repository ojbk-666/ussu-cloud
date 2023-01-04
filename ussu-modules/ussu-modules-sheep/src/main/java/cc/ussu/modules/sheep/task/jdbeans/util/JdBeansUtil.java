package cc.ussu.modules.sheep.task.jdbeans.util;

import cc.ussu.modules.sheep.task.jdbeans.vo.*;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

public final class JdBeansUtil {

    public static final String ydy = "≈";
    public static final String CHINESE_YUAN = "元";
    public static final String RIGHT_BRACKETS = ")";

    /**
     * 获取会员等级
     */
    public static final String getMemberLevel(TotalBeanResponseVo.TotalBeanResponseDataVo totalBeanResponseDataVo) {
        String memberLevelStr = "";
        String levelName = totalBeanResponseDataVo.getUserInfo().getBaseInfo().getLevelName();
        if (StrUtil.isNotBlank(levelName)) {
            if (levelName.length() > 2) {
                levelName = StrUtil.sub(levelName, 0, 2);
            }
            if (StrUtil.equals(levelName, "注册")) {
                memberLevelStr = "😊普通";
            } else if (StrUtil.equals(levelName, "钻石")) {
                memberLevelStr = "💎钻石";
            } else if (StrUtil.equals(levelName, "金牌")) {
                memberLevelStr = "🥇金牌";
            } else if (StrUtil.equals(levelName, "银牌")) {
                memberLevelStr = "🥈银牌";
            } else if (StrUtil.equals(levelName, "铜牌")) {
                memberLevelStr = "🥉铜牌";
            } else {
            }
            if ("1".equals(totalBeanResponseDataVo.getUserInfo().getIsPlusVip())) {
                memberLevelStr += "Plus";
            } else {
                memberLevelStr += "会员";
            }
        }
        return memberLevelStr;
    }

    /**
     * 总京豆
     */
    public static final String getMsg(TotalBeanResponseVo.TotalBeanResponseDataVo obj) {
        String dqjd = obj.getAssetInfo().getBeanNum();
        return dqjd + "京豆(" + ydy + div(dqjd, "100") + CHINESE_YUAN + RIGHT_BRACKETS;
    }

    /**
     * 总喜豆
     */
    public static final String getMsg(JxBeanQuerybeanamountResponseVo.JxBeanQuerybeanamountDataVo obj) {
        String dqxd = obj.getXibean() + StrUtil.EMPTY;
        return dqxd + "喜豆(" + ydy + div(dqxd, "100") + CHINESE_YUAN + RIGHT_BRACKETS;
    }

    /**
     * 极速金币
     */
    public static final String getMsg(JdSpeedCoinResponseVo.JdSpeedCoinDataVo obj) {
        String jsjb = obj.getGoldBalance() + "";
        return jsjb + "币(" + ydy + div(jsjb, "10000") + CHINESE_YUAN + RIGHT_BRACKETS;
    }

    /**
     * 京东赚赚
     */
    public static final String getMsg(JdzzResponseVo.JdzzDataResponseVo obj) {
        String jdzz = obj.getTotalNum();
        return jdzz + "币(" + ydy + div(jdzz, "10000") + CHINESE_YUAN + RIGHT_BRACKETS;
    }

    /**
     * 京东秒杀
     */
    public static final String getMsg(JdMsResponseVo.JdMsResultAssignmentVo obj) {
        String jdms = obj.getAssignmentPoints() + "";
        return jdms + "币(" + ydy + div(jdms, "1000") + CHINESE_YUAN + RIGHT_BRACKETS;
    }

    /**
     * 领现金
     */
    public static final String getMsg(JdCashResponseVo.JdCashDataVo obj) {
        String lxj = obj.getResult().getTotalMoney();
        return "领现金：" + lxj + CHINESE_YUAN;
    }

    /**
     * 东东农场
     */
    public static final String getMsg(JdFruitInitForFarmResponseVo jdFruit) {
        // 东东农场
        String fruitMsg = null;
        String fruitName = jdFruit.getFarmUserPro().getName();
        Integer treeEnergy = jdFruit.getFarmUserPro().getTreeEnergy();
        Integer treeTotalEnergy = jdFruit.getFarmUserPro().getTreeTotalEnergy();
        Integer treeState = jdFruit.getTreeState();
        if (StrUtil.isNotBlank(fruitName)) {
            // 农场状态正常
            if (treeEnergy != null && treeEnergy != 0) {
                if (2 == treeState || 3 == treeState) {
                    // 可以兑换了
                    fruitMsg = fruitName + " 可以兑换了!";
                } else {
                    fruitMsg = fruitName + " (" + div(treeEnergy * 100 + "", treeTotalEnergy + "") + "%)";
                }
            } else {
                if (0 == treeState) {
                    // 水果领取后未重新种植
                    fruitMsg = "水果领取后未重新种植";
                } else if (1 == treeState) {
                    // 种植中
                    fruitMsg = fruitName + " 种植中...";
                } else {
                    // 状态异常
                    fruitMsg = "农场状态异常";
                }
            }
        } else {
            // 状态异常
            fruitMsg = "农场状态异常";
        }
        return fruitMsg;
    }

    /**
     * 东东萌宠
     */
    public static final String getMsg(JdPetInitPetTownResponseVo.JdPetInitPetTownResult petTownResult) {
        String petMsg = null;
        JdPetInitPetTownResponseVo.GoodsInfo goodsInfo = petTownResult.getGoodsInfo();
        String goodsName = goodsInfo.getGoodsName();
        if (goodsInfo == null) {
            petMsg = "没有选择要兑换的商品";
        } else {
            if (0 == petTownResult.getUserStatus()) {
                petMsg = "萌宠活动未开启";
            } else if (5 == petTownResult.getUserStatus()) {
                petMsg = goodsName + " 可以兑换了!";
            } else if (6 == petTownResult.getUserStatus()) {
                petMsg = "未选择物品";
            } else {
                petMsg = goodsName + "(" + petTownResult.getMedalPercent() + "%, " + petTownResult.getMedalNum() + "/" + goodsInfo.getExchangeMedalNum() + "块)";
            }
        }
        return petMsg;
    }

    /**
     * 红包总额
     */
    public static final String getMsg(RedPacketRespoonseVo redPacket) {
        // 红包总额
        String zhb = redPacket.getData().getBalance() + "(总过期" + redPacket.getData().getExpiredBalance() + RIGHT_BRACKETS + CHINESE_YUAN;
        return zhb;
    }

    /**
     * 京东红包
     */
    public static final String getMsgRedPacketJd(RedPacketRespoonseVo redPacket) {
        String jdhb = redPacket.getJdRedTotal().toString() + "(总过期" + redPacket.getJdRedExpire() + RIGHT_BRACKETS + CHINESE_YUAN;
        return jdhb;
    }

    /**
     * 京喜红包
     */
    public static final String getMsgRedPacketJx(RedPacketRespoonseVo redPacket) {
        String jxhb = redPacket.getJxRedTotal().toString() + "(总过期" + redPacket.getJxRedExpire() + RIGHT_BRACKETS + CHINESE_YUAN;
        return jxhb;
    }

    /**
     * 极速红包
     */
    public static final String getMsgRedPacketJs(RedPacketRespoonseVo redPacket) {
        String jshb = redPacket.getJsRedTotal().toString() + "(总过期" + redPacket.getJsRedExpire() + RIGHT_BRACKETS + CHINESE_YUAN;
        return jshb;
    }

    /**
     * 健康红包
     */
    public static final String getMsgRedPacketJk(RedPacketRespoonseVo redPacket) {
        String jkhb = redPacket.getJdhRedTotal().toString() + "(总过期" + redPacket.getJdhRedExpire() + RIGHT_BRACKETS + CHINESE_YUAN;
        return jkhb;
    }

    /**
     * 今日京豆收入
     */
    public static final String getMsg(TodayBeanVO todayBeanVo) {
        return "收" + todayBeanVo.getIncome().toString() + "豆，支" + todayBeanVo.getOut().toString() + "豆";
    }

    /**
     * 今日喜豆收入
     */
    public static final String getMsg(TodayJxBeanVo todayJxBeanVo) {
        return "收" + todayJxBeanVo.getIncome().toString() + "豆，支" + todayJxBeanVo.getOut().toString() + "豆";
    }

    /**
     * 精确除法 截取两位小数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return
     */
    private static String div(String dividend, String divisor) {
        return div(dividend, divisor, 2);
    }

    private static String div(String dividend, String divisor, int scale) {
        return NumberUtil.roundDown(NumberUtil.div(dividend, divisor), scale).toString();
    }

}
