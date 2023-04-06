package com.ruru.plastic.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ruru.plastic.user.enume.ResponseEnum;
import com.ruru.plastic.user.exception.CommonException;
import org.apache.commons.lang.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class TokenUtil {
    public static String createToken(Long userId, Integer appType, String deviceCode) {

        String token = null;
        try {
            Date expiresAt = DateUtils.addDays(new Date(), 7);
            token = JWT.create()
                    .withClaim("userId", userId)
                    .withClaim("appType", appType)
                    .withClaim("deviceCode", deviceCode)
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256("RURU_PLASTIC"));
        } catch (JWTCreationException | IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static DecodedJWT deToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("RURU_PLASTIC"))
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new CommonException(ResponseEnum.ERROR_TOKEN_TIMEOUT);
        } catch (JWTVerificationException | UnsupportedEncodingException exception) {
            throw new CommonException(ResponseEnum.ERROR_TOKEN);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return jwt;
    }


    public static String createAdminToken(final Long adminUserId, final Integer appType, String deviceCode) {
        String token = null;
        try {
            Date expiresAt = DateUtils.addDays(new Date(), 7);
            token = JWT.create()
                    .withClaim("adminUserId", adminUserId)
                    .withClaim("appType", appType)
                    .withClaim("deviceCode", deviceCode)
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256("RURU_PLASTIC_ADMIN"));
        } catch (JWTCreationException | IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }


    public static DecodedJWT deAdminToken(final String token) {

        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("RURU_PLASTIC_ADMIN"))
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new CommonException(ResponseEnum.ERROR_TOKEN_TIMEOUT);
        } catch (JWTVerificationException | UnsupportedEncodingException exception) {
            throw new CommonException(ResponseEnum.ERROR_TOKEN);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return jwt;
    }
}
