package com.example.module.repository.member;


import com.example.module.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository{
    Optional<Member> findByUserId(String userId);

    List<Member> findByIdInAndDeletedFalse(Collection<Long> ids);

    Optional<Member> findByIdAndDeletedFalse(Long id);
}
