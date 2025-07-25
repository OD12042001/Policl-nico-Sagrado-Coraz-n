package com.webapplication.PoliclinicoSagradoCorazon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    /*
     * @Bean
     * public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
     * Exception {
     * http
     * .authorizeHttpRequests(auth -> auth
     * .anyRequest().permitAll()
     * )
     * .csrf(csrf -> csrf.disable()) // para evitar problemas con formularios
     * .formLogin(form -> form.disable()); // desactiva el login
     * 
     * return http.build();
     * }
     */

    @Bean
public SecurityFilterChain filtro(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login","/horario","/validarDni", "/procesarLogin", "/image/**", "/css/**", "/js/**", "/webjars/**", "/index","/registro-cita","/guardarHorarioSeleccionado").permitAll()
            .requestMatchers(HttpMethod.POST, "/registrarPaciente").permitAll()
            .requestMatchers("/portalPaciente").hasAuthority("PACIENTE")
            .requestMatchers("/portalRecepcionista","/formulario-dni").hasAuthority("RECEPCIONISTA")
            .requestMatchers("/portalAdministrador").hasAuthority("ADMINISTRADOR")
            .anyRequest().authenticated()
            //.anyRequest().permitAll()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/procesarLogin")
            .successHandler(customSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true) // <- Esto invalida la sesión
            .deleteCookies("JSESSIONID") // <- Esto elimina la cookie de sesión
            .permitAll()
        )
        .csrf(csrf -> csrf.disable());

    return http.build();
}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
