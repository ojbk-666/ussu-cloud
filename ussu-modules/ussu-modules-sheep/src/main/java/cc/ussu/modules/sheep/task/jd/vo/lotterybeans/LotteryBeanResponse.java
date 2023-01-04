package cc.ussu.modules.sheep.task.jd.vo.lotterybeans;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class LotteryBeanResponse {

    private String code;
    private String msg;
    /**
     * 恭喜您，中奖啦!
     * 1403-风险等级未通过
     */
    private String responseMessage;

    public boolean isSuccess() {
        return "0".equals(code) && StrUtil.isNotBlank(prizeId);
    }

    /**
     * 提示语
     * 恭喜您，中奖啦!
     * 活动太火爆，请稍后重试~
     */
    private String promptMsg;

    private String prizeType;

    private String chances;

    private String subCode;

    private String transParam;

    private String prizeId;


    // private ChannelPoint channelPoint;

    private String returnMsg;

    private String winner;

    /**
     * 抽中了啥
     */
    private String prizeName;


}
