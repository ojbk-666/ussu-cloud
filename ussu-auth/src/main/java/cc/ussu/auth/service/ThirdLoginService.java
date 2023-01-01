package cc.ussu.auth.service;

import java.util.HashMap;
import java.util.Map;

public interface ThirdLoginService<T> {

    /**
     * 登录
     */
    <R> R login(T t) throws Exception;

    default Map getNewHashMap() {
        return new HashMap<String, Object>();
    }

}
