package com.hobby.service;

import com.hobby.config.AppConfig;
import com.hobby.daos.UsersRepository;
import com.hobby.daos.VerificationTokenRepository;
import com.hobby.dtos.AuthenticationResponse;
import com.hobby.dtos.LoginRequest;
import com.hobby.dtos.RegisterRequest;
import com.hobby.enuns.RegistrationStatus;
import com.hobby.models.NotificationEmail;
import com.hobby.models.User;
import com.hobby.models.VerificationToken;
import com.hobby.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UsersRepository usersDao;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public void register(@Valid RegisterRequest registerRequest) {
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        usersDao.save(user);
        sendVerificationEmail(user.getEmail());

    }

    public void sendVerificationEmail(String email) {
        User user = findByEmail(email);
        String verificationToken = getVerificationToken(user);
        emailService.sendMail(
                NotificationEmail.builder()
                        .subject("Please Activate your Web conference application Account")
                        .body("Thank you for signing up to Web conference application, " +
                                "please click on the below url to activate your account : " +
                                appConfig.getUrl() + "api/auth/account-verification/" + verificationToken)
                        .recepient(user.getEmail())
                        .build());

    }


    private String getVerificationToken(User user) {
        return verificationTokenRepository.findByUser(user)
            .orElseGet(() -> {
                VerificationToken verificationToken = VerificationToken.createVerificationTokenForUser(user);
                verificationTokenRepository.saveAndFlush(verificationToken);
                return verificationToken;
            }).getToken();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwtToken = jwtProvider.generateToken(authenticate);
        User user = findByEmail(loginRequest.getEmail());
        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .expiresAt(Instant.now().plusSeconds(jwtProvider.getJwtTokenExpirationSecs()))      //TODO remove this field if not needed
                .username(user.getFullName())
                .email(loginRequest.getEmail())
                .build();
    }

    public User findByEmail(String email) {
        return usersDao.findByEmail(email).orElseThrow( () -> new RuntimeException("User details not available"));
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new IllegalArgumentException("Invalid Token")));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        User user = verificationToken.getUser();
        user.setRegistrationStatus(RegistrationStatus.ACTIVATED);
        usersDao.save(user);
    }


}
