package com.trogiare.security;


import com.google.gson.Gson;
import com.trogiare.model.User;
import com.trogiare.model.UserRole;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class LocalTokenProvider {

    static final Logger logger = LoggerFactory.getLogger(LocalTokenProvider.class);

    private final String JWT_SECRET = "7TgzPu003WEbRPytZcn6ZeXnPldBCYjIwgPQu5jtR8UZRLA7hzHz9Laqx44ebgyY";
    private final long JWT_EXPIRATION = 4 * 60 * 60 * 1000; // 4h

    private final SecretKey JWT_SECRET_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    public String generateToken(User user, List<UserRole> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        Gson gson = new Gson();
        TokenObject tokenObject = new TokenObject();
        tokenObject.setUserName(user.getSdt());
        tokenObject.setFirstName(user.getFirstName());
        tokenObject.setRoles(roles);
        String sub = gson.toJson(tokenObject);
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setId(user.getId())
                .signWith(JWT_SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaimFromToken(String authToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(authToken).getBody();
        } catch (Exception ex) {
            logger.error("Invalid JWT token");
        }
        return null;
    }
}