package com.example.Warehouse.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.Warehouse.entities.accountService.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {
    private int id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private Map<String, Object> attributes;

    public UserPrincipal(int id, String email, String password,boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled=enabled;
    }

    public static UserPrincipal create(Account account) {
    	if(account!=null) {
    		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
        	if(account.getRole()==null) {
        		authorities= Collections.
                        singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        	}
        	else {
        		authorities.add(new SimpleGrantedAuthority(account.getRole().getRolename()));
        	}
            return new UserPrincipal(
            		account.getId(),
            		account.getEmail(),
            		account.getPassword(),
            		account.isEnabled(),
                    authorities
            );
    	}
    	else return null;
    }

    public static UserPrincipal create(Account account, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(account);
        if(userPrincipal!=null) {
        	userPrincipal.setAttributes(attributes);
            return userPrincipal;
        }
        else return null;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
