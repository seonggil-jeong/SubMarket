package com.submarket.userservice.util;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenUtil {
    private final Environment env;


    public String getUserIdByToken(HttpHeaders headers) {
        log.debug("Token Util Start!");
        String token = headers.get("Authorization").get(0); // Get Token in headers
        String jwt = token.replace("Bearer ", ""); // delete Bearer

        log.info("JWT : " + jwt); // TokenValue
        String userId = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody().getSubject();

        log.info("getUserId End");

        return userId;
    }
}
