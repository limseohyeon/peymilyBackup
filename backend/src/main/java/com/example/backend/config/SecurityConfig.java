package com.example.backend.config;

import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    @Value("CL2xeP3cZ0MDZQDmuWeHPajwAJSPwtBk0JI5t6KCdGnK6ckXxx")
    private String secretKey;
    private final JwtFilter jwtFilter; // JwtFilter를 DI를 통해 주입

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .cors().and()
            .authorizeHttpRequests() // 수정된 부분
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/users/fetchAll").authenticated()
            .requestMatchers("/profile/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/pet/add").authenticated()
            .requestMatchers(HttpMethod.POST, "/pet/{email}/uploadImage").permitAll()
            .requestMatchers(HttpMethod.POST, "/pet/{email}/downloadImage/**").permitAll()
            .requestMatchers("/schedule/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/schedule/**").authenticated()
            .requestMatchers("/shared/**").authenticated()
            .requestMatchers("/shared-images/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/shared-images/{email}/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/link/**").authenticated()
            .requestMatchers("/link/**").authenticated()
            .requestMatchers("/comment/**").authenticated()
            .requestMatchers("/community/**").authenticated()
            .requestMatchers("/communityImage/**").permitAll()
            .requestMatchers("/invitation/**").authenticated()
            .requestMatchers("/executed/**").authenticated()
            .requestMatchers("/GalleryComment/**").authenticated()
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JwtFilter 추가

        return http.build();
    }



    public void configure(WebSecurity web) throws Exception {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        web.httpFirewall(firewall);
    }
}
