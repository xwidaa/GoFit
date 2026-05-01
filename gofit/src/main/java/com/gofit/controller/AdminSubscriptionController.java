package com.gofit.controller;

import com.gofit.model.Subscription;
import com.gofit.model.User;
import com.gofit.repository.UserRepository;
import com.gofit.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/subscription")
@CrossOrigin
public class AdminSubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    public AdminSubscriptionController(SubscriptionService subscriptionService,
                                       UserRepository userRepository) {
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }

    // 🔹 UPDATE subscription for a specific user
    @PutMapping("/{userId}")
    public Subscription updateSubscription(
            @PathVariable Long userId,
            @RequestBody UpgradeRequest request
    ) {

        // 1. găsim userul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. facem upgrade/downgrade
        return subscriptionService.upgrade(user, request.getPlan());
    }

    // 🔹 (OPTIONAL) vezi subscription-ul unui user (pt debug/admin)
    @GetMapping("/{userId}")
    public Subscription getUserSubscription(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionService.getUserSubscription(user);
    }
}