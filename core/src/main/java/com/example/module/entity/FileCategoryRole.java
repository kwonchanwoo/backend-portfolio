package com.example.module.entity;

import com.example.module.dto.FileCategoryRolePK;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileCategoryRole {

    @EmbeddedId
    private FileCategoryRolePK fileCategoryRolePK;

    private boolean deleted;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_member_id", columnDefinition = "bigint null comment '작성자의 member id'", updatable = false)
    @CreatedBy
    private Member createdMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_member_id", columnDefinition = "bigint null comment '수정자의 member id'")
    @LastModifiedBy
    private Member updatedMember;
}
