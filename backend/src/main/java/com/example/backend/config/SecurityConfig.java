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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/users/fetchAll").permitAll()//authenticated()
                .antMatchers("/users/**").permitAll()//authenticated()
                .antMatchers("/pet/**").permitAll()//authenticated()
                .antMatchers(HttpMethod.POST, "/pet/add").permitAll()//authenticated()
                .antMatchers(HttpMethod.POST, "/pet/{email}/uploadImage").permitAll()//authenticated()
                .antMatchers("/schedule/**").permitAll()//authenticated()
                .antMatchers(HttpMethod.DELETE, "/schedule/**").permitAll()
                .antMatchers("/shared/**").permitAll()//authenticated()
                .antMatchers("/shared-images/**").permitAll()//authenticated()
                .antMatchers(HttpMethod.POST, "/shared-images/{email}/**").permitAll()//authenticated()
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        web.httpFirewall(firewall);
    }
}
