package ru.buisnesslogiclab1.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.entity.VideoIdSubscriberIdPair;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {


    Optional<UserEntity> findByNickName(String nickName);


  /*  @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE userr " +
                    "SET role = 'ADMIN' " +
                    "WHERE id = :userId")
    UserEntity appointNewAdmin(UUID userId);*/

}
