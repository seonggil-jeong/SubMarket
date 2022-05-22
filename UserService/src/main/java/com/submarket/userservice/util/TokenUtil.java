package com.submarket.userservice.util;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
public class TokenUtil {

    public static String getUserIdByToken(HttpHeaders headers, String secret) {
        log.debug("Token Util Start!");
        String token = headers.get("Authorization").get(0); // Get Token in headers
        String jwt = token.replace("Bearer", ""); // delete Bearer

        log.info("JWT : " + jwt); // TokenValue
        String userId = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody().getSubject();

        log.info("getUserId End");

        return userId;
    }
}
