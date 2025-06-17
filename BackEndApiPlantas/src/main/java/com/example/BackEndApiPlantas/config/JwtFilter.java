package com.example.BackEndApiPlantas.config;

import com.example.BackEndApiPlantas.service.JWTService;
import com.example.BackEndApiPlantas.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final MyUserDetailsService userDetailsService;

    public JwtFilter(JWTService jwtService, MyUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) 
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        // Si no hay header de autorización o no empieza con "Bearer ", sigue con la cadena de filtros
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extrae el token
        final String token = authHeader.substring(7);
        System.out.println("DEBUG: Token recibido: " + token);
        
        try {
            // Extrae el username del token
            final String username = jwtService.extractUserName(token);
            
            // Si hay un username y no hay autenticación ya establecida
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carga los detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                System.out.println("DEBUG: Usuario cargado: " + username);
                System.out.println("DEBUG: Roles: " + userDetails.getAuthorities());
                
                // Valida el token
                if (jwtService.validateToken(token, userDetails)) {
                    // Crea la autenticación
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                        
                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Establece la autenticación
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("DEBUG: Autenticación establecida correctamente");
                } else {
                    System.out.println("DEBUG: Token no válido");
                }
            }
        } catch(Exception e) {
            System.out.println("DEBUG: Error procesando el token: " + e.getMessage());
        }
        
        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}