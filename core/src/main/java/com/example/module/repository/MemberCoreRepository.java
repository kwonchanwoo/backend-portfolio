package com.example.module.repository;


import com.example.module.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCoreRepository extends JpaRepository<Member, Long>{
}
