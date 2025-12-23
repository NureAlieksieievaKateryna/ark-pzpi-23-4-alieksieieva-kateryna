package org.example.reminder.subscription.service;

import lombok.RequiredArgsConstructor;
import org.example.reminder.business.facade.dto.BusinessStatisticsResponse;
import org.example.reminder.business.model.BusinessEntity;
import org.example.reminder.common.model.UserSubscriptionStatus;
import org.example.reminder.subscription.model.SubscriptionTemplateEntity;
import org.example.reminder.subscription.model.UserSubscriptionEntity;
import org.example.reminder.subscription.repository.UserSubscriptionRepository;
import org.example.reminder.user.model.UserEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public BusinessStatisticsResponse getStatistics(Long businessId) {

        Long total = userSubscriptionRepository
                .countAllByBusiness(businessId);

        Long active = userSubscriptionRepository
                .countActiveByBusiness(businessId);

        BigDecimal revenue = userSubscriptionRepository
                .sumRevenueByBusiness(businessId);

        return BusinessStatisticsResponse.builder()
                .totalSubscriptions(total)
                .activeSubscriptions(active)
                .totalRevenue(revenue)
                .build();
    }

    public UserSubscriptionEntity create(
            UserEntity user,
            BusinessEntity business,
            SubscriptionTemplateEntity template,
            LocalDateTime startDate,
            LocalDateTime endDate,
            BigDecimal finalPrice
    ) {
        UserSubscriptionEntity subscription = new UserSubscriptionEntity();

        subscription.setUser(user);
        subscription.setBusiness(business);
        subscription.setTemplate(template);

        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setFinalPrice(finalPrice);
        subscription.setStatus(UserSubscriptionStatus.ACTIVE);

        return userSubscriptionRepository.save(subscription);
    }
}
