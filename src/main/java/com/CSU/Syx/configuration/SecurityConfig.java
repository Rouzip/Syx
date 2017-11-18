package com.CSU.Syx.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Rouzip
 * @date 2017.11.9
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 静态资源全部允许，首页允许，websocket允许通过
                    .antMatchers("/**", "/static/**", "/index","/ws").permitAll()
                .anyRequest().authenticated().and()
                .csrf().disable();
    }
}
