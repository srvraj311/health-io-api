package com.srvraj311.healthioapi.config;

import com.google.gson.Gson;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.GlobalExceptionsHandler;
import com.srvraj311.healthioapi.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    JwtTokenUtil jwtTokenUtil;
    CustomUserDetailsService customUserDetailsService;
    GlobalExceptionsHandler globalExceptionsHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getRequestURI().contains("/api/")) {
                String authHeader = request.getHeader("Authorization");
                String token = null;
                String email = null;
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    email = jwtTokenUtil.getEmailFromToken(token);
                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                        if (jwtTokenUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } else {
                            throw new ControllerExceptions.UnauthorizedException("Token validation failed");
                        }
                    } else {
                        throw new ControllerExceptions.UnauthorizedException("Invalid token or token expired");
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (ControllerExceptions.UnauthorizedException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new Gson().toJson(globalExceptionsHandler.handleUnauthorizedException(ex).getBody()));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(globalExceptionsHandler.handleRuntimeException(ex).getBody()));
        }
    }
}
