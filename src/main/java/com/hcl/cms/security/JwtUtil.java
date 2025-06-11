package com.hcl.cms.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hcl.cms.entity.Admin;
import com.hcl.cms.entity.Member;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtil {

    private final String jwtSecret = "secret_key";
    private final long jwtExpirationMs = 86400000;

    public String generateToken(Member member, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("Id", member.getMemberId());
        claims.put("name", member.getFirstName() + " " + member.getLastName());
        claims.put("email", member.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(member.getMemberId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    //For Admin
    public String generateToken(Admin admin, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("Id", admin.getAdminId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(admin.getAdminId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    
    
    
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
 
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token.replace("Bearer ", "")).getBody();
    }
    
    
}