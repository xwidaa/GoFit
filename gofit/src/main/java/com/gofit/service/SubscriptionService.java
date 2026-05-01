package com.gofit.service;

import com.gofit.model.Subscription;
import com.gofit.model.User;
import com.gofit.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // 🔹 ia subscription-ul userului (sau creează FREE dacă nu există)
    public Subscription getUserSubscription(User user) {

        return subscriptionRepository.findByUser(user)
                .orElseGet(() -> createFreeSubscription(user));
    }

    // 🔹 creează FREE (prima dată când intră userul)
    private Subscription createFreeSubscription(User user) {

        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setPlan("FREE");
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(null);

        return subscriptionRepository.save(sub);
    }

    // 🔹 upgrade (FREE → BASIC / PREMIUM)
    public Subscription upgrade(User user, String plan) {

    if (plan == null || 
        (!plan.equals("FREE") && !plan.equals("BASIC") && !plan.equals("PREMIUM"))) {
        throw new RuntimeException("Invalid plan");
    }

    Subscription sub = subscriptionRepository.findByUser(user)
            .orElseGet(() -> createFreeSubscription(user));

    sub.setPlan(plan);
    sub.setStartDate(LocalDate.now());

    if (plan.equals("FREE")) {
        sub.setEndDate(null);
    } else {
        sub.setEndDate(LocalDate.now().plusMonths(1));
    }

    return subscriptionRepository.save(sub);
}
}