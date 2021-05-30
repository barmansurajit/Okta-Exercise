package com.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class ClientController {

    private final WebClient webClient;
    @Value("#{ @environment['resourceServer.url'] }")
    private String resourceServerUrl;

    public ClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user.getUserInfo());
        Map<String,String> userBasicProfile = new HashMap<String,String>();
        userBasicProfile.put("First Name",user.getGivenName());
        userBasicProfile.put("Middle Initial",user.getMiddleName());
        userBasicProfile.put("Last Name",user.getFamilyName());
        userBasicProfile.put("Nick Name",user.getNickName());
        String welcomeMessage = this.webClient.get()
                .uri(this.resourceServerUrl + "/welcome").retrieve()
                .bodyToMono(String.class).block();
        mav.addObject("welcomeMessage",welcomeMessage);

        try {
            String email = this.webClient.get()
                    .uri(this.resourceServerUrl + "/email").retrieve()
                    .bodyToMono(String.class).block();

            if (email != null) {
                userBasicProfile.put("Email", email);
            }
        } catch (Exception e) {
            mav.addObject("emailError", true);
        }

        mav.addObject("profile", userBasicProfile);
        mav.setViewName("home");
        log.info("******* model and view ****** {}", mav);
        return mav;
    }
}
