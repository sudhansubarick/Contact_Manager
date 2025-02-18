package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    // config of Authenticate provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // user detail service object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        // password endcoder object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // configuration
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/signup", "/services").permitAll();
            // which url start from user it will protect means after login all pages
            // protected you have to login/signup otherwise all are permited
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // form default login
        // customizable in login form
        httpSecurity.formLogin(formLogin -> {
            // Redirect to customize loginpage
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            //if authentication fails
            formLogin.failureHandler(authFailureHandler);
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
      
        });
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
      
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
