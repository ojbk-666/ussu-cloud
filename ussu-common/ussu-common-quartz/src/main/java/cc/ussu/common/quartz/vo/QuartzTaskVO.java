package cc.ussu.common.quartz.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QuartzTaskVO implements Serializable {

    private static final long serialVersionUID = -8214381810222102712L;

    private String name;
    private String group;
    private String jobClassName;
    private String description;
    private Date startTime;
    private Date endTime;
    private Date previousFireTime;
    private Date fireTimeAfter;

    private List<QuartzTriggerVO> triggers;

}
