package com.kongwc.tools.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhanghx on 2017/11/21.
 */
@Component
public class SmartLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public SmartLoginUrlAuthenticationEntryPoint() {
        super("/login");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"))) {
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write("invalidSession");
            printWriter.flush();
            printWriter.close();
        } else
            super.commence(request, response, authException);
    }
}
