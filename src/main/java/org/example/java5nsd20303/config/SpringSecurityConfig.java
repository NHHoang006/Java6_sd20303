package org.example.java5nsd20303.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.java5nsd20303.exception.CustomErrorDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

//@Configuration

@RequiredArgsConstructor
//@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SpringSecurityConfig {

    //Tiêm
    private final UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                .authorizeHttpRequests(authuorize ->{
                //Cách 1
//                    // Chỉ admin mới có quyền truy cập
//                    authuorize.requestMatchers(HttpMethod.POST,"/api/**").hasRole("ADMIN");
//                    authuorize.requestMatchers(HttpMethod.PUT,"/api/**").hasRole("ADMIN");
//                    authuorize.requestMatchers(HttpMethod.DELETE,"/api/**").hasRole("ADMIN");
//
//                    //Mọi người đều có quyền truy cập
//                    authuorize.requestMatchers(HttpMethod.GET,"/api/**").hasAnyRole("ADMIN","USER","MANAGER");

                    authuorize.anyRequest().authenticated();
        })

                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//       UserDetails userDetails= User.builder().username("user")
//                .password(passwordEncoder()
//                        .encode("123456")).roles("USER").build();
//
//
//       UserDetails adminDetails= User.builder().username("admin")
//                .password(passwordEncoder()
//                        .encode("123456")).roles("ADMIN").build();
//
//
//       return new InMemoryUserDetailsManager(userDetails, adminDetails);
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
