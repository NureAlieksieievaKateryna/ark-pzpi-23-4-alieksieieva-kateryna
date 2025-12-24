package org.example.reminder.iot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.reminder.subscription.model.SubscriptionComponentEntity;
import org.example.reminder.subscription.model.UserSubscriptionEntity;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "iot_usage_log")
public class IoTUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private IoTDevice device;

    @ManyToOne
    private UserSubscriptionEntity subscription;

    @ManyToOne
    private SubscriptionComponentEntity component;

    private LocalDateTime usedAt;

    private Boolean success;
}
