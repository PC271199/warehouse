package com.example.Warehouse.security.oauth;

import com.example.Warehouse.config.JwtTokenUtil;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.AuthProvider;
import com.example.Warehouse.entities.accountService.Permission;
import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.accountService.UserInfor;
import com.example.Warehouse.entities.accountService.VerificationToken;
import com.example.Warehouse.exceptions.accountService.OAuth2AuthenticationProcessingException;
import com.example.Warehouse.pojo.Mail;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.accountService.PermissionRepository;
import com.example.Warehouse.repositories.accountService.RoleRepository;
import com.example.Warehouse.repositories.accountService.VerificationRepository;
import com.example.Warehouse.security.UserPrincipal;
import com.example.Warehouse.security.oauth.user.OAuth2UserInfo;
import com.example.Warehouse.security.oauth.user.OAuth2UserInfoFactory;
import com.example.Warehouse.services.MailService;
import com.example.Warehouse.services.MailServiceImpl;
import com.example.Warehouse.util.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil tokenProvider;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	MailServiceImpl mailServiceImpl;
	@Autowired
	private VerificationRepository verifyRepo;
	private static final String MAIL_FROM = "phuoccong99@gmail.com";

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

		try {
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			// Throwing an instance of AuthenticationException will trigger the
			// OAuth2AuthenticationFailureHandler
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
				oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		Optional<Account> userOptional = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
		Account user = null;
		if (userOptional.isPresent()) {
			user = userOptional.get();
			if (!user.getProvider()
					.equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
				throw new OAuth2AuthenticationProcessingException(
						"Looks like you're signed up with " + user.getProvider() + " account. Please use your "
								+ user.getProvider() + " account to login.");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerByGoogle(oAuth2UserRequest, oAuth2UserInfo);
		}

		return UserPrincipal.create(user, oAuth2User.getAttributes());
	}

	private Account registerByGoogle(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		Account account = new Account();
		Role role = roleRepository.findById(1).get();
		Set<Permission> permissions = new HashSet<>();
		Permission permission1 = permissionRepository.findById(1).get();
		Permission permission2 = permissionRepository.findById(2).get();
		permissions.add(permission1);
		permissions.add(permission2);
		UserInfor userInfor = new UserInfor();
		userInfor.setAccount(account);
		String result = PasswordUtil.generatePswd(9, 10, 2, 2, 2);
		String codeId = PasswordUtil.generatePswd(40, 50, 10, 30, 0);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		account.setCodeId(codeId);
		account.setPassword(encoder.encode(result));
		account.setUserinfor(userInfor);
		account.setRole(role);
		account.setPermissions(permissions);
		account.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
		account.setEmail(oAuth2UserInfo.getEmail());
		account.setEnabled(true);
		String verifyToken = UUID.randomUUID().toString();
		VerificationToken verificationToken=createVerificationToken(verifyToken);
		verificationToken.setAccount(account);
		account.setVerificationToken(verificationToken);
		Account accountResult = accountRepository.save(account);
		
		Mail mail = new Mail();

		mail.setMailFrom(MAIL_FROM);
		mail.setMailTo(oAuth2UserInfo.getEmail());
		mail.setMailContent(
				"Your initial password: "+result+"\n"+
				"Click the following link to change your password" + "\n"
						+ "http://localhost:3000/changePassword/" + codeId);

		try {
			MailService mailService = (MailService) mailServiceImpl;
			mailService.sendEmailPassword(mail);
		} catch (Exception e) {
			throw e;
		}
		return accountResult;
	}
	private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
	public VerificationToken createVerificationToken(String token) {
		VerificationToken myToken = new VerificationToken(token);
		myToken.setExpiryDate(calculateExpiryDate(5));
		return myToken;
	}
	private Account updateExistingUser(Account existingUser, OAuth2UserInfo oAuth2UserInfo) {
		return accountRepository.save(existingUser);
	}

}
