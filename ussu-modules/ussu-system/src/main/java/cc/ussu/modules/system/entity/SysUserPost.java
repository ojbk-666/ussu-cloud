package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户与岗位关联表
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:05:21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_post")
public class SysUserPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 岗位ID
     */
    @TableField("post_id")
    private String postId;

}
