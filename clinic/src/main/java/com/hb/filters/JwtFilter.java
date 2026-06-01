/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.filters;

import com.hb.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author DELL
 */
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("URI: " + httpRequest.getRequestURI());
        System.out.println("Auth header: " + httpRequest.getHeader("Authorization"));
        if (httpRequest.getRequestURI().startsWith(String.format("%s/api/secure", httpRequest.getContextPath())) == true) {

            String header = httpRequest.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            } else {
                String token = header.substring(7);

                try {
                    String username = JwtUtils.validateTokenAndGetUsername(token);
                    if (username != null) {
                        System.out.println("@@@@@@@@@@valid token@@@@@@@@@@@@@@@@");
                        httpRequest.setAttribute("username", username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        try {
                            System.out.println("@@@@@@@@@@valid token - 2@@@@@@@@@@@@@@@@");
                            chain.doFilter(request, response);
                            System.out.println("@@@@@@@@@@valid token - 3@@@@@@@@@@@@@@@@");
                        } catch (Exception e) {
                            System.out.println("Lỗi xảy ra tại Filter Chain: " + e.getMessage());
                            e.printStackTrace();
                        }
                        return;
                    }
                } catch (Exception e) {
                    // Log lỗi
                }
            }

            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token không hợp lệ hoặc hết hạn");
            return;
        }

        chain.doFilter(request, response);
    }

}
