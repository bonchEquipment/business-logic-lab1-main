package ru.buisnesslogiclab1.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_subscription")
public class UserSubscriptionEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "subscription_name")
    private String subscriptionName;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
}
