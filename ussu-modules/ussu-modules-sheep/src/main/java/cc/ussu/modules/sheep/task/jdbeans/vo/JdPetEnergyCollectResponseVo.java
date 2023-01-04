package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

import java.util.List;

/**
 * 萌宠请求响应1
 */
@Data
public class JdPetEnergyCollectResponseVo {

    private String code;
    private String resultCode;
    private String message;
    private JdPetEnergyCollectResult result;

    @Data
    public class JdPetEnergyCollectResult {
        private String medalPercent;
        private Integer needCollectEnergy;
        private Integer totalEnergy;
        private Integer needCollectMedalNum;
        private Boolean showHongBaoExchangePop;
        private Integer medalNum;
        private Boolean showMedalExchangePop;
        private List<PetPlaceInfo> petPlaceInfoList;
    }

    @Data
    public class PetPlaceInfo {
        private Integer place;
        private Boolean updateSign;
        private Integer energy;
    }

}
