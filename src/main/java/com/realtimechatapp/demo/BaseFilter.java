package com.realtimechatapp.demo;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*The technique for using abstraction is - create a method out of the uncommon part of the code
 * Then make them methods abstraccttttttttooo
 * Then extend that class and Override those methods - Learn this from AI
 * Very very important technique to help DRY
 * */

public abstract class BaseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
//        System.out.println("im inside the base filter");
        HttpServletRequest req = (HttpServletRequest) var1;
        HttpServletResponse resp = (HttpServletResponse) var2;

        boolean isAuthenticated = false;
        System.out.println("path ===== " + req.getServletPath());
        Cookie[] cookies = req.getCookies();


//         for unauth request ->
//        if path is /login -> go to login page -> static page
//        if path is /signup -> go to signup page -> static page
//        if path is /chat -> go to login page -> static page
        if (cookies == null || cookies.length == 0) {
            isAuthenticated = false;

        }
        /* If request is auth
         * if path is /login -> redir /chat
         * if path is /signup -> redir /chat
         * if path is /chat -> redir /chat
         * */


        String jwt = CookieFetcher.getCookieValue(cookies, "jwt");
        if (jwt == null || jwt.length() == 0 || jwt.isEmpty()) {
//            if no cookie then move to login page so that our guy can login
            System.out.println("==== null =====");
            isAuthenticated = false;


        } else {

            try {
                JwtHelper.verifyJWT(jwt);
                isAuthenticated = true;
//                resp.sendRedirect("chat");
            } catch (JWTVerificationException exception) {

//                   remove the broken/expired jwt
                Cookie jwtCookie = new Cookie("jwt", "");
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(0);
                jwtCookie.setHttpOnly(false);
                resp.addCookie(jwtCookie);
                isAuthenticated = false;


            }
        }
        if (isAuthenticated) {
            authRequest(var1, var2, var3);
        } else {
            unAuthRequest(var1, var2, var3);
        }


    }

    // as per the pat
    public abstract void unAuthRequest(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;

    public abstract void authRequest(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;
}
