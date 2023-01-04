package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通知内容
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_notice_content")
public class SysNoticeContent implements Serializable {

    private static final long serialVersionUID = -7522260669578426948L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String noticeContent;

}
