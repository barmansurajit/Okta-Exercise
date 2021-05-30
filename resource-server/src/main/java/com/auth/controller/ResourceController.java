package com.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceController {

    @GetMapping("/welcome")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public String getWelcomeMessage(Principal principal){
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        String fullName = jwtAuthenticationToken.getToken().getClaimAsString("fullName");
        return "Welcome " + fullName + "!";
    }

    @GetMapping("/email")
    @PreAuthorize("hasAuthority('SCOPE_email')")
    public String getUserEmail(Principal principal){
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        return jwtAuthenticationToken.getToken().getClaimAsString("userEmail");
    }
}
