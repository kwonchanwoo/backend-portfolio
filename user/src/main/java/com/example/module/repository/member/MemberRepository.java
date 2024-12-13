package com.example.module.repository.member;


import com.example.module.entity.Member;
import com.example.module.repository.MemberCoreRepository;
import com.example.module.repository.member.querydsl.MemberCustomRepository;

import java.util.Optional;

public interface MemberRepository extends MemberCoreRepository, MemberCustomRepository {
    Optional<Member> findByUserId(String userId);
}
