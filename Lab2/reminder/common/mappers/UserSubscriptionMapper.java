package org.example.reminder.common.mappers;

import org.example.reminder.subscription.facade.dto.UserSubscriptionResponse;
import org.example.reminder.subscription.model.UserSubscriptionEntity;
import org.springframework.stereotype.Component;

@Component
public class UserSubscriptionMapper {

    public UserSubscriptionResponse toDto(UserSubscriptionEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserSubscriptionResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .businessId(entity.getBusiness().getId())
                .templateId(entity.getTemplate().getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .finalPrice(entity.getFinalPrice())
                .build();
    }
}
