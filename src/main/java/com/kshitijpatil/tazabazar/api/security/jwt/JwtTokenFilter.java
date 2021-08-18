package com.kshitijpatil.tazabazar.api.security.jwt;

import com.kshitijpatil.tazabazar.api.security.repository.UserRepository;
import com.kshitijpatil.tazabazar.api.security.service.JwtValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("in_memory_user_repository")
    UserRepository userRepository;
    @Autowired
    private JwtValidateService jwtValidateService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get JWT token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtValidateService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on Spring Security Context
        var userDetails = userRepository
                .findByUsername(jwtValidateService.getUsername(token))
                .orElse(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                ofNullable(userDetails).map(UserDetails::getAuthorities).orElse(List.of())
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
