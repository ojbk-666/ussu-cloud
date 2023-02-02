package cc.ussu.auth.service;

public interface ThirdLoginService<T> {

    /**
     * 登录
     */
    <R> R login(T t) throws Exception;

    /**
     * 解析三方账号信息
     */
    <R> R exchangeThirdAccountInfo(T t) throws Exception;

}
