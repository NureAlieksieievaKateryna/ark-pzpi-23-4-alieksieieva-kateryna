package org.example.reminder.subscription.repository;

import org.example.reminder.subscription.model.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, Long> {
    List<UserSubscriptionEntity> findByUserId(Long userId);

    @Query("SELECT u FROM UserSubscriptionEntity u WHERE u.business.id = :businessId AND u.status = 'ACTIVE'")
    List<UserSubscriptionEntity> findActiveByBusinessId(Long businessId);

    @Query("""
        SELECT COUNT(us)
        FROM UserSubscriptionEntity us
        WHERE us.business.id = :businessId
    """)
    Long countAllByBusiness(@Param("businessId") Long businessId);

    @Query("""
        SELECT COUNT(us)
        FROM UserSubscriptionEntity us
        WHERE us.business.id = :businessId
          AND us.status = 'ACTIVE'
    """)
    Long countActiveByBusiness(@Param("businessId") Long businessId);

    @Query("""
        SELECT COALESCE(SUM(us.finalPrice), 0)
        FROM UserSubscriptionEntity us
        WHERE us.business.id = :businessId
    """)
    BigDecimal sumRevenueByBusiness(@Param("businessId") Long businessId);
}
