package com.hcl.cms.repository;

import java.util.List;
import java.util.Optional;

import com.hcl.cms.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.cms.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    List<Member> findByStatus(MemberStatus status);
    Optional<Member> findByEmail(String email);
}