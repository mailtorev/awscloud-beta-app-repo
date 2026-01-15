package com.betatrader.trades.tfm.tfm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TokenController {
    private final JwtEncoder jwtEncoder;

    public TokenController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @GetMapping("/token")
    public Map<String,String> generateToken(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
        Instant now = Instant.now();
        long expirySeconds = 3600; //1hr

        //Use Github id / login as subject

        String subject = resolveSubject(principal);
        String name = resolveName(principal);
        String uAttr = resolveAttrName(principal);


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("tfm-trade-app")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirySeconds))
                .subject(subject)
                .claim("github_login",principal.getAttribute(uAttr))
                .claim("name", principal.getAttribute("name"))
                .claim("authorities", List.of("ROLE_USER"))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        //String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return Map.of("token", token);
    }

    private String resolveSubject(OAuth2User principal) {
        if (principal.getAttribute("sub") != null) {
            return principal.getAttribute("sub").toString(); // Google
        }
        if (principal.getAttribute("id") != null) {
            return principal.getAttribute("id").toString(); // GitHub
        }
        return principal.getName(); // fallback
    }

    private String resolveName(OAuth2User principal) {
        if (principal.getAttribute("login") != null) {
            return principal.getAttribute("login").toString();
        }
        if (principal.getAttribute("email") != null) {
            return principal.getAttribute("email").toString();
        }
        return principal.getName();
    }

    private String resolveAttrName(OAuth2User principal) {
        if(principal.getAttribute("login") != null) {
            return "login";
        }else{
            return "email";
        }
    }

}
