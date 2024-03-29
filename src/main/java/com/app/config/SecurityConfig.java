package com.app.config;

import com.app.config.filter.JwtTokenValidator;
import com.app.services.users.imp.UserDetailServiceImp;
import com.app.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    /**
     * Method to define all security filter chain
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable()) // Is not important to use this in rest application
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session is not saved in memory. In this way session depend on token expiration time
                .authorizeHttpRequests(http -> {
                    // Public endpoints
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

                    // Protected endpoints
                    http.requestMatchers(HttpMethod.GET, "/method/get").hasAnyAuthority("CREATE", "READ");
                    http.requestMatchers(HttpMethod.POST, "/method/post").hasAnyAuthority("CREATE", "READ");
                    http.requestMatchers(HttpMethod.PATCH, "/method/patch").hasRole("DEV");

                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtil), BasicAuthenticationFilter.class)
                .build();
    }

    /**
     * Method to manage te security using AuthenticationManager
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Method to select the authentication provider
     *
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImp userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(this.passwordEncoder());
        provider.setUserDetailsService(userDetailService);

        return provider;
    }

    /**
     * Method to select a password encode, in this case NoOpPasswordEncoder
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
