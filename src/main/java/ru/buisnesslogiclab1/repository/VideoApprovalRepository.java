package ru.buisnesslogiclab1.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.ApprovalStatus;
import ru.buisnesslogiclab1.entity.VideoApprovalEntity;

@Repository
public interface VideoApprovalRepository extends JpaRepository<VideoApprovalEntity, UUID> {


    @Query(nativeQuery = true,
            value = "SELECT * FROM video_approval " +
                    "WHERE approval_status = :approvalStatus")
    List<VideoApprovalEntity> findByApprovalStatus(String approvalStatus);


}
