package org.example.reminder.common.mappers;

import org.example.reminder.subscription.facade.dto.SubscriptionTemplateResponse;
import org.example.reminder.subscription.model.SubscriptionTemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionTemplateMapper {

    public SubscriptionTemplateResponse toDto(
            SubscriptionTemplateEntity entity
    ) {
        if (entity == null) {
            return null;
        }

        return SubscriptionTemplateResponse.builder()
                .id(entity.getId())
                .businessId(entity.getBusiness().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .basePrice(entity.getBasePrice())
                .durationDays(entity.getDurationDays())
                .status(entity.getStatus())
                .build();
    }
}
