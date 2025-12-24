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

    /**
     * Check all subscriptions and update their status based on expiration.
     * Optionally extend subscription if auto-renewal is enabled.
     */
    @Transactional
    public void processSubscriptionExpirations() {
        List<UserSubscriptionEntity> expired = userSubscriptionRepository.findExpiredSubscriptions();

        for (UserSubscriptionEntity sub : expired) {

            if (shouldAutoRenew(sub)) {

                BigDecimal extensionPrice = sub.getFinalPrice().multiply(BigDecimal.valueOf(0.9));
                sub.setEndDate(sub.getEndDate().plusDays(sub.getTemplate().getDurationDays()));
                sub.setFinalPrice(extensionPrice);
                userSubscriptionRepository.save(sub);
            } else {
                sub.setStatus(UserSubscriptionStatus.EXPIRED);
                userSubscriptionRepository.save(sub);
            }
        }
    }

    public List<UserSubscriptionEntity> getActiveSubscriptions(Long userId) {
        return userSubscriptionRepository.findByUserIdAndStatus(userId, UserSubscriptionStatus.ACTIVE);
    }

    private boolean shouldAutoRenew(UserSubscriptionEntity sub) {

        return true;
    }
}

