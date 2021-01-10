package com.febs.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.febs.common.domain.FebsConstant;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FebsUtil;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FebsAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper mapper = new ObjectMapper();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        if (FebsUtil.isAjaxRequest(request)) {
            response.setContentType(FebsConstant.JSON_UTF8);
            response.getWriter().write(this.mapper.writeValueAsString(ResponseBo.error("没有该权限！")));
        } else {
            redirectStrategy.sendRedirect(request, response, FebsConstant.FEBS_ACCESS_DENY_URL);
        }
    }
}
