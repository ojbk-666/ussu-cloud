package cn.ussu.auth.service;

public interface ThirdLoginService<T> {

    /**
     * 登录 病返回token
     */
    String login(T t);

}
