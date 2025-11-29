package com.almacerca.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad de Spring
public class SecurityConfig {

    // 🔥 1. BEAN PARA EL DECODIFICADOR DE CONTRASEÑAS
    // Esto soluciona el error "Credenciales inválidas" al loguear.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. CADENA DE FILTROS (Control de accesos y rutas)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilita CSRF (necesario para APIs que no usan sesiones de navegador)
            .csrf(csrf -> csrf.disable())
            
            // Configura la aplicación como REST (sin estado/tokens)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Define las reglas de acceso por ruta
            .authorizeHttpRequests(auth -> auth
                // 🔥 Rutas públicas necesarias: Login, Registro, Listar Productos (tu catálogo)
                .requestMatchers("/api/auth/**", "/api/products").permitAll() 
                
                // Todas las demás rutas (Carrito, Admin, etc.) requieren autenticación (token)
                .anyRequest().authenticated()
            );

        return http.build();
    }
}