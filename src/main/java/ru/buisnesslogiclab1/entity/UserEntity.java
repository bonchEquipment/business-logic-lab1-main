package ru.buisnesslogiclab1.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.buisnesslogiclab1.dto.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "userr")
public class UserEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "nick_name")
    private String nickName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

}
