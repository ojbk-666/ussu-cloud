/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ussu.common.core.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 快捷获取HttpServletRequest,HttpServletResponse
 */
public class HttpContext {

	/**
	 * 获取请求的ip地址，优先获取请求头的x-real-ip
	 */
	@Deprecated
	public static String getIp() {
		HttpServletRequest request = HttpContext.getRequest();
		String realIpFromNginx = request.getHeader("x-real-ip");
		if (StrUtil.isNotBlank(realIpFromNginx)) {
			return realIpFromNginx;
		}
		if (request == null) {
			return "127.0.0.1";
		} else {
			return request.getRemoteHost();
		}
	}

	/**
	 * 获取当前请求的Request对象
	 *
	 * @author fengshuonan
	 * @Date 2018/7/23 下午3:44
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return null;
		} else {
			return requestAttributes.getRequest();
		}
	}

	/**
	 * 获取当前请求的Response对象
	 *
	 * @author fengshuonan
	 * @Date 2018/7/23 下午3:44
	 */
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return null;
		} else {
			return requestAttributes.getResponse();
		}
	}

	/**
	 * 获取所有请求的值
	 *
	 * @author fengshuonan
	 * @Date 2018/7/23 下午3:44
	 */
	public static Map<String, String> getRequestParameters() {
		HashMap<String, String> values = new HashMap<>();
		HttpServletRequest request = HttpContext.getRequest();
		if (request == null) {
			return values;
		}
		Enumeration enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paramName = (String) enums.nextElement();
			String paramValue = request.getParameter(paramName);
			values.put(paramName, paramValue);
		}
		return values;
	}

	public static ServletContext getServletContext() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return null;
		}
		return request.getServletContext();
	}

	public static HttpSession getSessionServlet() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return null;
		}
		HttpSession session = request.getSession();
		return session;

	}

}
