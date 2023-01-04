package cc.ussu.modules.sheep.task.jd.vo.fruit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 助力码VO
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JdFruitInviteCodeVO implements Serializable {

    private static final long serialVersionUID = 8084605082406596911L;

    private String pin;

    private String inviteCode;
}
