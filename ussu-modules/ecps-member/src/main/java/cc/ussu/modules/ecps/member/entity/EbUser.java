package cc.ussu.modules.ecps.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liming
 * @since 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbUser对象", description="")
public class EbUser extends Model<EbUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private String ebUserId;

    @ApiModelProperty(value = "登录名")
    private String username;

    @ApiModelProperty(value = "1男2女")
    private Integer gender;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "正式姓名")
    private String realName;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "省")
    private String resiProv;

    @ApiModelProperty(value = "市")
    private String resiCity;

    @ApiModelProperty(value = "区")
    private String resiDist;

    @ApiModelProperty(value = "默认收货地址")
    private String addr;

    private Integer zipCode;

    @ApiModelProperty(value = "登录时间")
    private Date loginTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "邮箱")
    private String email;

    private Integer groupId;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "qq")
    private String qqNum;

    private String cellphone;

    private String introInfo;

    private Integer loginCount;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "手机客户端")
    private Integer isMobileClient;

}
