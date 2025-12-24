package org.example.reminder.subscription.facade;

import lombok.RequiredArgsConstructor;
import org.example.reminder.business.model.BusinessEntity;
import org.example.reminder.business.service.BusinessService;
import org.example.reminder.common.mappers.SubscriptionTemplateMapper;
import org.example.reminder.common.model.SubscriptionStatus;
import org.example.reminder.subscription.facade.dto.CreateSubscriptionTemplateRequest;
import org.example.reminder.subscription.facade.dto.SubscriptionTemplateResponse;
import org.example.reminder.subscription.model.SubscriptionTemplateEntity;
import org.example.reminder.subscription.service.SubscriptionTemplateService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
    public class BusinessSubscriptionFacade {
    private final SubscriptionTemplateService subscriptionTemplateService;
    private final BusinessService businessService;
    private final SubscriptionTemplateMapper templateMapper;

         /**
     * Creates a new subscription template for a specific business.
     * <p>
     * The template defines the base price, duration and description of
     * a subscription plan that can later be activated and offered to users.
     * Newly created templates are typically created in an inactive state.
     * </p>
     *
     * @param request request object containing business identifier and
     *                subscription template parameters
     * @return created subscription template as a response DTO
     * @throws IllegalArgumentException if the business does not exist
     */
    @Transactional
    public SubscriptionTemplateResponse createTemplate(CreateSubscriptionTemplateRequest request) {
        BusinessEntity business = businessService.findById(request.businessId());

        SubscriptionTemplateEntity template =
                subscriptionTemplateService.create(
                        business,
                        request.name(),
                        request.description(),
                        request.basePrice(),
                        request.durationDays()
                );

        return templateMapper.toDto(template);
    }

        
    /**
     * Retrieves all subscription templates belonging to a specific business.
     *
     * @param businessId unique identifier of the business
     * @return list of subscription templates owned by the business
     */
    public List<SubscriptionTemplateResponse> getBusinessTemplates(Long businessId) {
        return subscriptionTemplateService.findByBusinessId(businessId)
                .stream()
                .map(templateMapper::toDto)
                .toList();
    }


    @Transactional
    public void activate(Long templateId) {
        subscriptionTemplateService.changeStatus(templateId, SubscriptionStatus.ACTIVE);
    }
        
         /**
     * Deactivates a subscription template.
     * <p>
     * Deactivated templates are no longer available for new subscriptions,
     * but existing user subscriptions remain unaffected.
     * </p>
     *
     * @param templateId unique identifier of the subscription template
     */
    @Transactional
    public void deactivate(Long templateId) {
        subscriptionTemplateService.changeStatus(templateId, SubscriptionStatus.INACTIVE);
    }
}

