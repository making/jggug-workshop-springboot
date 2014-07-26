package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@ConditionalOnProperty("bookmark.security.enabled")
@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsManagerConfigurer configurer = new InMemoryUserDetailsManagerConfigurer();
        configurer.withUser("hoge").password("hoge").roles("USER");
        configurer.withUser("admin").password("demo").roles("ADMIN");
        configurer.configure(auth);
        UserDetailsService userDetailsService = configurer.getUserDetailsService();

        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/loginForm").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/loginForm").failureUrl("/loginForm?error")
                .defaultSuccessUrl("/book/list")
                .usernameParameter("username").passwordParameter("password")
                .permitAll();
        http.logout().
                logoutUrl("/logout").permitAll();
    }
}