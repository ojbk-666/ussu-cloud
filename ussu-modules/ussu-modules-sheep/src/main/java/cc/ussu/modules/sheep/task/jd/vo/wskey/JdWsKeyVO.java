package cc.ussu.modules.sheep.task.jd.vo.wskey;

import cc.ussu.modules.sheep.task.jd.util.JdCkWskUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JdWsKeyVO implements Serializable {

    private static final long serialVersionUID = -7769109113982752624L;

    private String pin;

    private String wskey;

    public JdWsKeyVO(String str) {
        this.pin = JdCkWskUtil.getPinByWsck(str);
        this.wskey = JdCkWskUtil.getWskeyByWsck(str);
    }

    @Override
    public String toString() {
        return "pin=" + pin + ";wskey=" + wskey + ";";
    }
}
