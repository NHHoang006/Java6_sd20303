package org.example.java5nsd20303.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationResource {

    private final JwtEncoder jwtEncoder;


    @PostMapping("/api/authenticate")
    public JwtResponse authenticate(Authentication authentication) {
        return  new JwtResponse(createToken(authentication));
    }

    private String createToken(Authentication authentication) {
      JwtClaimsSet claims=  JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*30))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

    }

    private Object createScope(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(a-> a.getAuthority())
                .collect(Collectors.joining(" "));
    }
}


record JwtResponse(String token) {

}
