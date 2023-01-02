package cc.ussu.modules.dczx.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class DczxLoginResultVo implements Serializable {

    private String jsessionid;
    private String loginName;
    private String password;

    @JsonIgnore
    public String getRequestCookie() {
        return "JSESSIONID=" + jsessionid;
    }

    private String sign;
    private String accesstoken;
    private String userId;
    private String time;

    @JsonIgnore
    public Map<String, String> getHeaderMap() {
        Map<String, String> m = new HashMap<>();
        m.put("accesstoken", accesstoken);
        m.put("sign", sign);
        m.put("userid", userId);
        m.put("time", time);
        return m;
    }

    private String archieveId;
    private String tpId;
    private String isCourseModule;
    private String subjectId;
    private String studentStatus;
    /**
     * 学习中心名称
     */
    private String fdzName;
    private String fdzJGcode;
    private String fdzId;
    private String studyKindId;
    /**
     * 证件号码
     */
    private String cardId;
    /**
     * 姓名
     */
    private String studentName;
    /**
     * 头像
     */
    private String stuPic;
    private String stufileStatus;

}
