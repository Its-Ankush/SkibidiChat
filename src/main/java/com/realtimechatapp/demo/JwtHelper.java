package com.realtimechatapp.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.Date;

public class JwtHelper {
// get this secret from env variable

    private static final String secret;

    static {
        try {
            secret = ConfigLoader.getConfigLoader().secret();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Algorithm algorithm = Algorithm.HMAC256(secret);
    private static final JWTVerifier verifier = JWT.require(algorithm).build();
    private static final long now = System.currentTimeMillis();
    private static DecodedJWT decodedJWT = null;

    public static String createJWT(String username) {
        String jwt = JWT.create()
                .withClaim("name", username)
                .withExpiresAt(new Date(now + 24 * 60 * 60 * 1000))
                .sign(algorithm);

        return jwt;
    }

    //     use try catch on filter
// https://github.com/auth0/java-jwt?tab=readme-ov-file#verify-a-jwt
    public static void verifyJWT(String jwt) {

        decodedJWT = verifier.verify(jwt);
    }

    public static String getUsernameFromJWT(String jwt) {

        return decodedJWT.getClaim("name").asString();
    }

}
