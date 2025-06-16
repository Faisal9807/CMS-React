package com.hcl.cms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordResetToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // The actual reset token string

    @OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "member_id") // Link to the Member entity
    private Member member;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Constructor for easy creation
    public PasswordResetToken(String token, Member member, LocalDateTime expiryDate) {
        this.token = token;
        this.member = member;
        this.expiryDate = expiryDate;
    }
    
    
	public PasswordResetToken() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
}
