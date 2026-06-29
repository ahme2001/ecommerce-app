package com.ecommerce.sb_ecom.config;

import com.ecommerce.sb_ecom.utils.AuthEntryPointJwt;
import com.ecommerce.sb_ecom.utils.AuthTokenFilter;
import com.ecommerce.sb_ecom.utils.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter  authTokenFilterBean() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider DAP = new DaoAuthenticationProvider(userDetailsService);
        DAP.setPasswordEncoder(passwordEncoder());
        return DAP;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(ses ->
                        ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api/webhook").permitAll()
//                        .requestMatchers("/api/public/**").permitAll()
//                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated());

        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);

        http.headers(heders -> heders.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));
        return http.build();
    }

    // Defining endponts here , makes them not going in spring security
    // and it will be completed bypassed from spring security
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web -> web.ignoring().requestMatchers(
//                "/v2/api-docs", "/configuration/ui",
//                "/swagger-resources/**", "/configuration/security",
//                "/swagger-ui.html", "/webjars/**"
//        ));
//    }
}
