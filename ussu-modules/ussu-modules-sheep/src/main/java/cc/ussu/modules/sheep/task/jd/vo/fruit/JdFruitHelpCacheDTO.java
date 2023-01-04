package cc.ussu.modules.sheep.task.jd.vo.fruit;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 东东农场互助的缓存dto
 */
@Data
@Accessors(chain = true)
public class JdFruitHelpCacheDTO implements Serializable {

    private static final long serialVersionUID = -4598155146789048917L;

    private String pin;

    private String shareCode;

    /**
     * 自己帮助他人助力的次数
     */
    private Integer helpOtherTimes;

    /**
     * 他人帮助自己助力的次数
     */
    private Integer otherHelpTimes;

}
