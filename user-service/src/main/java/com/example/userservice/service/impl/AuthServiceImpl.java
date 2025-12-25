package com.example.userservice.service.impl;

import com.example.userservice.constant.UserRole;
import com.example.userservice.entity.User;
import com.example.userservice.exception.*;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.RegistrationRequest;
import com.example.userservice.response.UserAuthResponse;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.security.jwt.JwtProvider;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.RefreshTokenService;
import com.example.userservice.service.ServiceUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public UserProfileResponse registration(RegistrationRequest request) {
        String email = request.getEmail();
        String rawPassword = request.getPassword();
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistsException(email);
        }
        //validate email
        if(!emailValidator.isValid(email)){
            throw new InvalidEmailException(email);
        }
        //validate password
        RuleResult ruleResult = passwordValidator.validate(new PasswordData(rawPassword));
        if(!ruleResult.isValid()){
            throw new InvalidPasswordException(String.join(
                    ", ",
                    passwordValidator.getMessages(ruleResult)
            ));
        }
        User user = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName()).email(email)
                .password(passwordEncoder.encode(rawPassword))
                .roles(new HashSet<>(Arrays.asList(UserRole.CLIENT)))
                .build();

        return ServiceUtil.userToUserProfileResponse(user);
    }

    @Override
    public UserAuthResponse login(String email, String password) {
        //validate email
        if(!emailValidator.isValid(email)){
            throw new InvalidEmailException(email);
        }
        //find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new UserNotFoundException(">" + email + "< is not found in database"));

        //validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialException("Password is incorrect");
            //throw new RuntimeException("TEST_RUNTIME_EXCEPTION");
        }
        System.out.println("Hibernate thinks ID is: " + user.getUserId().toString());
        //access token
        String accessToken = jwtProvider.generateAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getRoles()
        );
        //refresh token
        String refreshToken = refreshTokenService.createRefreshToken(user.getUserId());
        return ServiceUtil.userToUserAuthResponse(user, accessToken, refreshToken);
    }

    @Override
    public UserAuthResponse refreshToken(String refreshToken) {

        try {
            UUID userId = refreshTokenService.validateAndGetUserId(refreshToken);

            refreshTokenService.revoke(refreshToken);

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() ->
                            new UserNotFoundException(
                                    "User with id " + userId + " is not found"
                            ));

            String newAccessToken =
                    jwtProvider.generateAccessToken(
                            userId,
                            user.getEmail(),
                            user.getRoles()
                    );

            String newRefreshToken =
                    refreshTokenService.createRefreshToken(userId);

            return ServiceUtil.userToUserAuthResponse(
                    user,
                    newAccessToken,
                    newRefreshToken
            );

        } catch (ReusedTokenException e) {
            refreshTokenService.revokeAll(e.getUserId());
            throw e;
        }
    }

}
