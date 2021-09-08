package com.linbankbackend.configuration;

import com.linbankbackend.configuration.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomEntryPoint customEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                cors().
                and().
                csrf().disable().
                authorizeRequests().
                antMatchers("/auth/**").permitAll().
                anyRequest().
                authenticated().
                and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling();
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.exceptionHandling().authenticationEntryPoint(customEntryPoint);


//        .authenticationEntryPoint((request, response, e)
//                -> {
//            response.setContentType("application/json;charset=UTF-8");
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            HashMap respuesta = new HashMap();
//            respuesta.put("codigo", "Invalid");
//            respuesta.put("mensaje", "Invalid credentials!");
//            response.getWriter().write(respuesta.toString());
//        });




    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuditorAware<Instant> instantAuditorAware() {
        return () -> Optional.of(Instant.now());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtRequestFilter jwtFilter() {
        return new JwtRequestFilter();
    }




}
