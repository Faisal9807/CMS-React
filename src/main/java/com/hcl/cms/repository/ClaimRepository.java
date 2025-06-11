package com.hcl.cms.repository;

import java.util.List;

import com.hcl.cms.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.cms.entity.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByMember_MemberId(String memberId);
    List<Claim> findByStatus(ClaimStatus status);
}