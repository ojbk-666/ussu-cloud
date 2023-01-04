package cc.ussu.modules.sheep.task.jd.vo;

import cc.ussu.modules.sheep.common.SheepParam;
import cc.ussu.modules.sheep.task.jd.util.JdCkWskUtil;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JdCookieVO implements SheepParam {

    /**
     * pin 不带pt_pin=
     */
    private String pt_pin;

    /**
     * key 不带pt_key=
     */
    private String pt_key;

    public JdCookieVO() {
    }

    public JdCookieVO(String ckvalue) {
        this.pt_pin = JdCkWskUtil.getPinByCk(ckvalue);
        this.pt_key = JdCkWskUtil.getKeyByCk(ckvalue);
    }

    @Override
    public String toString() {
        return "pt_pin=" + pt_pin +";pt_key=" + pt_key + ";";
    }

}
