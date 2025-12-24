package org.example.reminder.controllers;

import lombok.RequiredArgsConstructor;
import org.example.reminder.subscription.facade.UserSubscriptionFacade;
import org.example.reminder.subscription.facade.dto.CreateUserSubscriptionRequest;
import org.example.reminder.subscription.facade.dto.SubscriptionTemplateResponse;
import org.example.reminder.subscription.facade.dto.UserSubscriptionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subscriptions/user")
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final UserSubscriptionFacade userSubscriptionFacade;

    @PostMapping("create-subscription")
    public UserSubscriptionResponse createUserSubscription(@RequestBody CreateUserSubscriptionRequest request) {
        return userSubscriptionFacade.createUserSubscription(request);
    }

    @GetMapping("templates")
    public List<SubscriptionTemplateResponse> getAllTemplates() {
        return userSubscriptionFacade.getAllTemplates();
    }
    @PostMapping("/process-expirations")
    public void processExpirations() {
        userSubscriptionFacade.processExpirations();
    }
    
    @GetMapping("/{userId}/active")
    public List<UserSubscriptionResponse> getActiveSubscriptions(@PathVariable Long userId) {
        return userSubscriptionFacade.getActiveSubscriptions(userId);
    }
}

