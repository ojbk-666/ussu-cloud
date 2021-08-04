package cn.ussu.modules.ecps.portal.controller;

import cn.ussu.common.core.base.BaseController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

public class BasePortalController extends BaseController {

    public void setAttr(String k, Object v) {
        request.setAttribute(k, v);
    }

    @ModelAttribute
    public void setModelAttribute() {
        String contextPath = servletContext.getContextPath();
        setAttr("ctx", "/portal" + contextPath);
        setAttr("IMAGE_SERVER", "https://img.seasmall.top/");
    }

    public void injectAllParamToRequestScope() {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            setAttr(key, request.getParameter(key));
        }
    }

    protected ModelAndView toLogin(String backUrl) {
        RedirectView redirectView = new RedirectView("login?backUrl=" + backUrl);
        redirectView.setPropagateQueryParams(true);
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

}
