package com.trogiare.security;

import com.google.gson.Gson;
import com.trogiare.model.UserRole;
import com.trogiare.respone.UnauthorizedResponse;
import com.trogiare.respone.UserResp;
import com.trogiare.utils.TokenUtil;
import com.trogiare.utils.ValidateUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.*;

@Component
public class LocalTokenAuth extends OncePerRequestFilter {
    static final Logger logger = LoggerFactory.getLogger(LocalTokenAuth.class);
    @Autowired
    private LocalTokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if(request.getHeader("Authorization") == null ||request.getHeader("Authorization").isEmpty() ){
                filterChain.doFilter(request,response);
                return;
            }
            String token = TokenUtil.getTokenFrom(request);
            if (ValidateUtil.isEmpty(token)) {
                logger.info("JWT is empty");
                returnUnauthorized(request, response, "JWT is empty");
                return;
            }
            Claims claims = tokenProvider.getClaimFromToken(token);
            if (claims == null) {
                logger.info("JWT invalid");
                returnUnauthorized(request, response, "JWT invalid");
                return;
            }
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                logger.info("JWT is expired {}", expiration);
                returnUnauthorized(request, response, "JWT is expired");
                return;
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            String sub = claims.getSubject();
            logger.info("sub {}", sub);
            Gson gson = new Gson();
            TokenObject tokenObject = gson.fromJson(sub, TokenObject.class);
            for (UserRole r : tokenObject.getRoles()) {
                if (r == null) {
                    continue;
                }
                authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
            }
            UserPrincipal userDetails = new UserPrincipal();
            userDetails.setId(claims.getId());
            userDetails.setUsername(tokenObject.getUserName());
            userDetails.setFirstName(tokenObject.getFirstName());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context(Local)", ex);
        }
        filterChain.doFilter(request, response);

    }



    private void returnUnauthorized(HttpServletRequest request, HttpServletResponse response, String messageDetail) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            UnauthorizedResponse unauthorizedResponse = new UnauthorizedResponse();
            unauthorizedResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase() + ": " + messageDetail);
            unauthorizedResponse.setTimestamp(Instant.now().toString());
            unauthorizedResponse.setPath(request.getRequestURI());
            unauthorizedResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            out.print(new Gson().toJson(unauthorizedResponse));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
