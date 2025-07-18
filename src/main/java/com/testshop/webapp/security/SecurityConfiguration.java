package com.testshop.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private static String REALM = "REAME";

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService()
    {
        User.UserBuilder users = User.builder();

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(); // le specifiche dei ruoli saranno in memory

        manager.createUser(
                users
                        .username("Enrico")
                        .password(new BCryptPasswordEncoder().encode("123Stella"))
                        .roles("USER")
                        .build());

        manager.createUser(
                users
                        .username("Admin")
                        .password(new BCryptPasswordEncoder().encode("VerySecretPwd"))
                        .roles("USER","ADMIN")
                        .build());

        return manager;
    }

    private static final String[] ADMIN_MATCHER = {"/api/prodotti/inserisci/**",
            "/api/prodotti/modifica/**", "/api/prodotti/elimina/**"};

    @Override
    protected void configure(HttpSecurity http)
            throws Exception
    {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers(USER_MATCHER).hasAnyRole("USER")
                .antMatchers(ADMIN_MATCHER).hasAnyRole("ADMIN")
                .anyRequest().authenticated() // qualsiasi richiesta sull endpoint siano autenticati quindi per accededre all user bisogna essere autenticati quindi il metodo andMatchers non serve se è presnete questo metodo
                .and()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthEntryPoint getBasicAuthEntryPoint()
    {
        return new AuthEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**"); // stiamo specificando che tutti i metodi options dovranno essere ignorati dal metodo di sicurezza
    }
}
