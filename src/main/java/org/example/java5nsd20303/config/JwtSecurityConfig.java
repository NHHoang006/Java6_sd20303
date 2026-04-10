package org.example.java5nsd20303.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.example.java5nsd20303.exception.CustomErrorDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration

@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class JwtSecurityConfig {

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
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(403);

                        CustomErrorDetails errorDetails = new CustomErrorDetails(
                                LocalDateTime.now(),
                                "Access Denied: You do not have permission to access this resource",
                                "uri=" + request.getRequestURI()
                        );

                        response.getWriter().write(new ObjectMapper().writeValueAsString(errorDetails));
                    });
                })
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2->{
                    oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()));
                });
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
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        return keyPairGenerator.generateKeyPair();
    }

    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {

        return new RSAKey
                .Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {

        //JWKSet jwkSet = new JWKSet(rsaKey);

        return ((jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaKey)));
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {

        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
