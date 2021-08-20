	package com.example.Warehouse.security.oauth;

import com.example.Warehouse.entities.Account;
import com.example.Warehouse.entities.AuthProvider;
import com.example.Warehouse.entities.Permission;
import com.example.Warehouse.entities.Role;
import com.example.Warehouse.entities.UserInfor;
import com.example.Warehouse.exceptions.OAuth2AuthenticationProcessingException;
import com.example.Warehouse.repositories.PermissionRepository;
import com.example.Warehouse.repositories.RoleRepository;
import com.example.Warehouse.repositories.AccountRepository;
import com.example.Warehouse.security.UserPrincipal;
import com.example.Warehouse.security.oauth.user.OAuth2UserInfo;
import com.example.Warehouse.security.oauth.user.OAuth2UserInfoFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Account> userOptional = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
        Account user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private Account registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    	Account user = new Account();
    	Role role=roleRepository.findById(1).get();
    	Set<Permission> permissions= new HashSet<>();
    	Permission permission1=permissionRepository.findById(1).get();
    	Permission permission2=permissionRepository.findById(2).get();
    	permissions.add(permission1);
    	permissions.add(permission2);
    	UserInfor userInfor=new UserInfor();
    	userInfor.setAccount(user);
    	user.setUserinfor(userInfor);
    	user.setRole(role);
    	user.setPermissions(permissions);
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setEmail(oAuth2UserInfo.getEmail());
        return accountRepository.save(user);
    }

    private Account updateExistingUser(Account existingUser, OAuth2UserInfo oAuth2UserInfo) {
        return accountRepository.save(existingUser);
    }

}
