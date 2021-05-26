package cn.ussu.modules.system.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdLoginFormAlipayVo implements Serializable {

    private static final long serialVersionUID = -5860092173691464825L;

    private String userId;
    private String nickName;
    private String avatar;
    private String gender;
    private String province;
    private String city;

}
