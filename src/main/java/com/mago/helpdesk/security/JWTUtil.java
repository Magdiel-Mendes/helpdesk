package com.mago.helpdesk.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component

public class JWTUtil {
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}

	public boolean tokenValido(String token) {
<<<<<<< HEAD
		Claims claims = getClains(token);
=======
		Claims claims = getClaims(token);
>>>>>>> 46767caf2f067ffad7d4aa51a5e9f9d4abb50975
		if(claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
<<<<<<< HEAD
		if(username != null && expirationDate != null && now.before(now)) {
			return true;
		}
	}
		return false;
}
	private Claims getClains(String token) {
=======
			
			if(username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	private Claims getClaims(String token) {
>>>>>>> 46767caf2f067ffad7d4aa51a5e9f9d4abb50975
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String getUsername(String token) {
<<<<<<< HEAD
		Claims claims = getClains(token);
=======
		Claims claims = getClaims(token);
>>>>>>> 46767caf2f067ffad7d4aa51a5e9f9d4abb50975
		if(claims != null) {
			return claims.getSubject();
		}
		return null;
	}
}