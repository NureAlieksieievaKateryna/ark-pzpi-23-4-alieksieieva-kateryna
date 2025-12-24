package org.example.reminder.subscription.service;

import lombok.RequiredArgsConstructor;
import org.example.reminder.business.model.BusinessEntity;
import org.example.reminder.common.model.SubscriptionStatus;
import org.example.reminder.subscription.model.SubscriptionTemplateEntity;
import org.example.reminder.subscription.repository.SubscriptionTemplateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionTemplateService {
    private final SubscriptionTemplateRepository subscriptionTemplateRepository;

    
    /**
     * Finds a subscription template by its unique identifier.
     * <p>
     * Throws a RuntimeException if the template does not exist.
     * This ensures that further processing only occurs with valid templates.
     * </p>
     *
     * @param templateId the unique ID of the subscription template
     * @return the subscription template entity
     */
    public SubscriptionTemplateEntity findById(Long templateId){
        return subscriptionTemplateRepository.findById(templateId).orElseThrow(()->new RuntimeException("Subscription template doesnt exist %s".formatted(templateId)));
    }

    public List<SubscriptionTemplateEntity> findAll(){
        return subscriptionTemplateRepository.findAll();
    }


/**
     * Creates a new subscription template for a specific business.
     * <p>
     * The new template is automatically set to ACTIVE status and saved in the database.
     * </p>
     *
     * @param business     the business entity that owns the template
     * @param name         the name of the subscription template
     * @param description  a description of the subscription template
     * @param basePrice    the base price of the subscription
     * @param durationDays the duration of the subscription in days
     * @return the created subscription template entity
     */
    public SubscriptionTemplateEntity create(
            BusinessEntity business,
            String name,
            String description,
            BigDecimal basePrice,
            Integer durationDays
    ) {
        SubscriptionTemplateEntity template =
                SubscriptionTemplateEntity.builder()
                        .business(business)
                        .name(name)
                        .description(description)
                        .basePrice(basePrice)
                        .durationDays(durationDays)
                        .status(SubscriptionStatus.ACTIVE)
                        .build();

        return subscriptionTemplateRepository.save(template);
    }

        /**
     * Retrieves all subscription templates from the system.
     * <p>
     * This method returns a list of all templates, regardless of business or status.
     * </p>
     *
     * @return a list of all subscription templates
     */
    public List<SubscriptionTemplateEntity> findByBusinessId(Long businessId) {
        return subscriptionTemplateRepository.findByBusinessId(businessId);
    }

        /**
     * Retrieves the active subscription template by its ID.
     * <p>
     * Only templates with ACTIVE status can be returned. Throws an exception if no active template is found.
     * This ensures that subscriptions are only created based on valid and active templates.
     * </p>
     *
     * @param id the ID of the subscription template
     * @return the active subscription template entity
     */
    public SubscriptionTemplateEntity getActiveTemplate(Long id) {
        return subscriptionTemplateRepository.findByIdAndStatus(id, SubscriptionStatus.ACTIVE)
                .orElseThrow(() ->
                        new IllegalStateException("Active template not found"));
    }

    public void changeStatus(Long templateId, SubscriptionStatus status) {
        SubscriptionTemplateEntity template =
                subscriptionTemplateRepository.findById(templateId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Template not found"));

        template.setStatus(status);
    }
    
    /**
 * Calculates the final price of a subscription template
 * including all component price modifiers.
 *
 * @param template subscription template entity
 * @return final calculated price
 */
public BigDecimal calculateFinalPrice(SubscriptionTemplateEntity template) {

    BigDecimal result = template.getBasePrice();

    for (SubscriptionComponentEntity component : template.getComponents()) {
        if (component.getPriceModifier() != null) {
            result = result.add(component.getPriceModifier());
        }
    }

    return result;
}

}


