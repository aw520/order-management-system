package com.example.userservice.service.impl;

import com.example.userservice.constant.UserRole;
import com.example.userservice.entity.User;
import com.example.userservice.exception.InvalidEmailException;
import com.example.userservice.exception.InvalidPasswordException;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.RegistrationRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
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

}
