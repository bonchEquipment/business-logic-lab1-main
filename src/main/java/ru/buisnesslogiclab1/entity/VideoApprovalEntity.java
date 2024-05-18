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
import ru.buisnesslogiclab1.dto.ApprovalStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "video_approval")
public class VideoApprovalEntity {

    @Id
    @Column(name = "video_id")
    private UUID videoId;

    @Column(name = "approval_status")
    @Enumerated(value = EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name = "comment")
    private String comment;

}
