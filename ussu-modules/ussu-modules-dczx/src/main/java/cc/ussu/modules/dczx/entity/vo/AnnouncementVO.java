package cc.ussu.modules.dczx.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnnouncementVO implements Serializable {

    private static final long serialVersionUID = 1373008707156870879L;

    private String title;

    private String content;

}
