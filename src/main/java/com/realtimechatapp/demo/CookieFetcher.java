package com.realtimechatapp.demo;

import jakarta.servlet.http.Cookie;

public class CookieFetcher {

    public static String getCookieValue(Cookie[] cookies, String name) {

        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
