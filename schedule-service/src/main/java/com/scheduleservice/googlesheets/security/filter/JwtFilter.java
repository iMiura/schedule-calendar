package com.scheduleservice.googlesheets.security.filter;

import com.scheduleservice.googlesheets.security.jwt.JwtToken;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author :keisho
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(
        ServletRequest request, ServletResponse response, Object mappedValue) {

        log.error("認証できてる");

        try {
            executeLogin(request, response);
        } catch (AuthenticationException e) {
            setUnauthorizedErrorResponse(WebUtils.toHttp(response), e);
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AuthenticationException("システムエラー");
        }
        return true;
    }

    @Override
    protected boolean executeLogin(
        ServletRequest request, ServletResponse response) throws Exception {

        log.error("ログイン実行");

        JwtToken jwtToken = new JwtToken(getAuthzHeader(request));
        getSubject(request, response).login(jwtToken);
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        log.error("準備中・・・");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void setUnauthorizedErrorResponse(HttpServletResponse response, Throwable ex) {

        log.error("認証できてない");

        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        try {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("システムエラー:" + e.getMessage());
        }
    }
}
