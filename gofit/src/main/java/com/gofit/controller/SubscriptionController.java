package com.gofit.controller;

import com.gofit.model.Subscription;
import com.gofit.model.User;
import com.gofit.repository.UserRepository;
import com.gofit.service.SubscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/subscription")
@CrossOrigin
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  UserRepository userRepository) {
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
public Subscription getMySubscription(Authentication auth) {
    String email = (String) auth.getPrincipal();
    User user = userRepository.findByEmail(email).orElseThrow();

    return subscriptionService.getUserSubscription(user);
}

    @PostMapping("/upgrade")
public Subscription upgrade(Authentication auth,
                             @RequestBody UpgradeRequest request) {

    String email = (String) auth.getPrincipal();
    User user = userRepository.findByEmail(email).orElseThrow();

    return subscriptionService.upgrade(user, request.getPlan());
}


}