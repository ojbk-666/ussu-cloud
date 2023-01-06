package cc.ussu.common.quartz.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class QuartzTriggerVO implements Serializable {

    private static final long serialVersionUID = -4567852509850559323L;

    private String name;
    private String group;
    private Date startTime;
    private Date endTime;

}
