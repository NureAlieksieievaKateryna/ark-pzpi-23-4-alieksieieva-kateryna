package org.example.reminder.controllers;

import lombok.RequiredArgsConstructor;
import org.example.reminder.subscription.facade.BusinessSubscriptionFacade;
import org.example.reminder.subscription.facade.dto.CreateSubscriptionTemplateRequest;
import org.example.reminder.subscription.facade.dto.SubscriptionTemplateResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions/business")
@RequiredArgsConstructor
public class BusinessSubscriptionController {

    private final BusinessSubscriptionFacade businessSubscriptionFacade;

    @PostMapping("template/create")
    //@PreAuthorize("hasRole('BUSINESS')")
    public SubscriptionTemplateResponse createTemplate(@RequestBody CreateSubscriptionTemplateRequest request) {
        return businessSubscriptionFacade.createTemplate(request);
    }

    @GetMapping("/{businessId}")
    public List<SubscriptionTemplateResponse> getBusinessTemplates(@PathVariable Long businessId) {
        return  businessSubscriptionFacade.getBusinessTemplates(businessId);
    }

    @PatchMapping("/{templateId}/activate")
    public void activate(@PathVariable Long templateId) {
         businessSubscriptionFacade.activate(templateId);
    }

    @PatchMapping("/{templateId}/deactivate")
    public void deactivate(@PathVariable Long templateId) {
          businessSubscriptionFacade.deactivate(templateId);
    }
}

