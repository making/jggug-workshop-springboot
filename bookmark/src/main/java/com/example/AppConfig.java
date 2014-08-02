package com.example;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class AppConfig {
    @Autowired
    DataSourceProperties properties;
    DataSource dataSource;

    @ConfigurationProperties(prefix = DataSourceAutoConfiguration.CONFIGURATION_PREFIX)
    @Bean(destroyMethod = "close")
    DataSource realDataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create(this.properties.getClassLoader())
                .url(this.properties.getUrl())
                .username(this.properties.getUsername())
                .password(this.properties.getPassword());
        this.dataSource = factory.build();
        return this.dataSource;
    }

    @Bean
    DataSource dataSource() {
        return new Log4jdbcProxyDataSource(this.dataSource);
    }

    // ここから
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        return filter;
    }
    // ここまで

    @Bean
    Filter corsFilter() {
        return new Filter() {
            public void doFilter(ServletRequest req, ServletResponse res,
                    FilterChain chain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) res;
                String method = request.getMethod();
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods",
                        "POST,GET,OPTIONS,DELETE");
                response.setHeader("Access-Control-Max-Age",
                        Long.toString(60 * 60));
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader(
                        "Access-Control-Allow-Headers",
                        "Origin,Accept,X-Requested-With,"
                                + "Content-Type,Access-Control-Request-Method,"
                                + "Access-Control-Request-Headers,Authorization");
                if ("OPTIONS".equals(method)) {
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    chain.doFilter(req, res);
                }
            }

            public void init(FilterConfig filterConfig) {
            }

            public void destroy() {
            }
        };
    }
}
