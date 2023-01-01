package cc.ussu.common.core.util;

import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * 路径匹配工具
 */
public class PathMatcherUtil {

    private static final AntPathMatcher antPathMatcher;

    static {
        antPathMatcher = new AntPathMatcher();
    }

    public static boolean match(Collection<String> urls, String pattern) {
        return urls.stream().anyMatch(item -> antPathMatcher.match(item, pattern));
    }

    public static boolean match(String pattern, String url) {
        return antPathMatcher.match(pattern, url);
    }

}
