package com.hcl.cms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.cms.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long> {
	
	Optional<Admin> findByAdminId(String adminId);
	

}
