package com.submarket.userservice.util;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

@Slf4j
@RequiredArgsConstructor
public class TokenUtil {
    private static Environment env;

    public static String getUserIdByToken(HttpHeaders headers) {
        log.debug("Token Util Start!");
        String token = headers.get("Authorization").get(0); // Get Token in headers
        String jwt = token.replace("Bearer", ""); // delete Bearer

        log.info("JWT : " + jwt); // TokenValue
        String userId = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody().getSubject();

        log.info("getUserId End");

        return userId;
    }
}
