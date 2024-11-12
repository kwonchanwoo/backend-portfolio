package com.example.module.util;



import com.example.module.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//@Getter
//@MappedSuperclass // BaseEntity를 상속한 엔터티들은 아래 필드들을 컬럼으로 인식하게 된다.
@EntityListeners(AuditingEntityListener.class) // Auditing(자동으로 값 매핑) 기능 추가
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
