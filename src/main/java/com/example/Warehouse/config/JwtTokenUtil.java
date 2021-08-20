package com.example.Warehouse.config;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.Warehouse.entities.Account;
import com.example.Warehouse.entities.Permission;
import com.example.Warehouse.security.UserPrincipal;
import com.example.Warehouse.services.AccountService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
	@Autowired
	private AccountService accountservice;

	@Value("${JWT_SECRET}")
	private String secret;
	


    public JwtTokenUtil() {
    	
    }
	public String getEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
	}
	
//	public String getUserIdFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey("926D96C90030DD58429D2751AC1BDBBC")
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }


	public UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final UserDetails userDetails) {

		final JwtParser jwtParser = Jwts.parser().setSigningKey(secret.getBytes());

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		final Claims claims = claimsJws.getBody();
		
		final Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}
	
	
	

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final Account account = this.accountservice.getByUserName(authentication.getName());
		
		Map<Integer, String> permissions= new HashMap<Integer, String>();

		for (Permission permission : account.getPermissions()) {
				permissions.put(permission.getId(), permission.getActname());
		}
			

		final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 864000000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authorities)
				.claim("id", (userPrincipal.getId()))
				.claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
