package ru.buisnesslogiclab1.entity;

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
@Entity(name = "subscription_type")
public class SubscriptionTypeEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "monthly_pay_rub")
    private Integer monthlyPayRub;


}
