package cn.ussu.common.core.constants;

import cn.hutool.core.text.StrPool;

public interface StrConstants {

    int SUCCESS_CODE = 20000;
    int ERROR_CODE = 50000;
    String code = "code";
    String msg = "msg";
    String message = "message";
    String data = "data";
    String ok = "ok";
    String error = "";
    String name = "name";
    String value = "value";
    String param = "param";
    String all = "all";
    String Layout = "Layout";
    String ParentView = "ParentView";
    String defaultSuccessCode = "";

    String COMMA = StrPool.COMMA;

    String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    String id = "id";
    String pid = "pid";
    String parent_id = "parent_id";
    String parentId = "parentId";
    String sort = "sort";
    String create_time = "create_time";
    String createTime = "createTime";
    String create_by = "create_by";
    String createBy = "createBy";
    String update_time = "update_time";
    String updateTime = "updateTime";
    String update_by = "update_by";
    String updateBy = "updateBy";
    String del_flag = "del_flag";
    String delFlag = "delFlag";
    String version = "version";

    String SLASH_all = StrPool.SLASH + all;
    String SLASH_remote = StrPool.SLASH + "remote";

}
