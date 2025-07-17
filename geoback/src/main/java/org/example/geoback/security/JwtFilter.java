package org.example.geoback.security;

import org.example.geoback.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String username = JwtUtil.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (!JwtUtil.isTokenExpired(token)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }
            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
