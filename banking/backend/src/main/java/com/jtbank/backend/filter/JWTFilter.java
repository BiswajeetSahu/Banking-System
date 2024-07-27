
package com.jtbank.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtbank.backend.service.IJWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final IJWTService service;
    private  final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var path = request.getRequestURI();

        var passedPaths = List.of("login", "register", "create", "swagger", "api-doc");
        for (var passedPath : passedPaths) {
            if (path.contains(passedPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token = request.getHeader("Authorization");
        try {
            if(token ==null){
                throw new  RuntimeException("token should not be empty");
        }
            String accountNumber = service.validateToken(token.substring(7));

            if(accountNumber==null|| accountNumber.isBlank()||accountNumber.isBlank())
                throw  new  RuntimeException("token not found");
            request.setAttribute("accountNumber", accountNumber);

            filterChain.doFilter(request, response);
        }catch (Exception e){
            var problemDetails= ProblemDetail.forStatus(400);
            problemDetails.setTitle("token issue");
            problemDetails.setDetail(e.getMessage());


            response.setContentType("application/json");
            response.setStatus(400);
            response.getWriter().println(objectMapper.writeValueAsString(problemDetails));
        }


    }
}
