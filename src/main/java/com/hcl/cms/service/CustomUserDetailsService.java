package com.hcl.cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hcl.cms.entity.Member;
import com.hcl.cms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hcl.cms.entity.Admin;
import com.hcl.cms.repository.AdminRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// First, check Admin
		Optional<Admin> adminOpt = adminRepository.findByAdminId(username);
		if (adminOpt.isPresent()) {
			Admin admin = adminOpt.get();
			return new User(admin.getAdminId(), admin.getPassword(),
					List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
		}

		// Then, check Member
		Optional<Member> memberOpt = memberRepository.findByMemberId(username);
		if (memberOpt.isPresent()) {
			Member member = memberOpt.get();
			return new User(member.getMemberId(), member.getPassword(),
					List.of(new SimpleGrantedAuthority("ROLE_MEMBER")));
		}

		throw new UsernameNotFoundException("User not found: " + username);
	}
}