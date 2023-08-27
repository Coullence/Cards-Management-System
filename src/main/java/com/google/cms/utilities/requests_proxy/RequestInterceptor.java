package com.google.cms.utilities.requests_proxy;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class RequestInterceptor implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String username = UserRequestContext.getCurrentUser();
        if (username == null || username.trim().isEmpty() || username.trim().equalsIgnoreCase("")){
            username = "Guest";
        }else {
            UserRequestContext.setCurrentUser(username);
        }
        username = (username == null || username.trim().isEmpty() || username.trim().equalsIgnoreCase("")) ? "Guest" : username;
        log.info("---------------------Request Made by: " +username+", Request Method: "+httpRequest.getMethod() + ", Requested URI: " +httpRequest.getRequestURI() + ", at: " + LocalDateTime.now());
        chain.doFilter(request, response);
    }
}
