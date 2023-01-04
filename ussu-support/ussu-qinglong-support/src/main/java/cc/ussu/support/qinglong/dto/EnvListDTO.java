package cc.ussu.support.qinglong.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 环境变量Vo
 */
@Data
public class EnvListDTO extends BaseResultDTO<List<EnvListDTO.EnvDTO>> {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DISABLE = 1;

    @Data
    @Accessors(chain = true)
    public static class EnvDTO {
        private String _id;
        private Integer id;
        private String name;
        private String value;
        private String remarks;
        // private Long created;
        // private String position;
        /**
         * 状态 0正常 1禁用
         */
        private Integer status;
        // private String timestamp;
        // private String updatedAt;
    }

}
