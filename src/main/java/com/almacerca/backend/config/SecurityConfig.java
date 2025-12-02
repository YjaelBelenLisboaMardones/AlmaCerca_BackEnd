// [EN TU ARCHIVO SecurityConfig.java EN EL BACKEND]

package com.almacerca.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// Importación necesaria para especificar el método HTTP
import org.springframework.http.HttpMethod; 

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                // 🔥 Rutas públicas necesarias: Login, Registro, Listar Productos (catálogo)
                .requestMatchers("/api/auth/**", "/api/products").permitAll() 
                
                // 🔑 SOLUCIÓN AL ERROR 403: Permitir POST al endpoint de creación de admin.
                // Esto permite que el Frontend pueda enviar el producto sin token JWT.
                .requestMatchers(HttpMethod.POST, "/api/admin/products").permitAll() // ⬅️ CAMBIO CLAVE
                
                // Además, si el endpoint de CATEGORÍAS también es público:
                .requestMatchers("/api/products/category/**").permitAll()
                
                // Todas las demás rutas (Admin CRUD aparte del POST, Carrito, etc.) requieren autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }
}