package com.ordersystem.ordermanagementsystem.service.impl;

import com.ordersystem.ordermanagementsystem.entity.User;
import com.ordersystem.ordermanagementsystem.exception.*;
import com.ordersystem.ordermanagementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ordersystem.ordermanagementsystem.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerLocalUser(String firstName, String lastName, String email, String rawPassword) {
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
            throw new InvalidPasswordException(passwordValidator.getMessages(ruleResult).toString());
        }
        User user = new User().builder().firstName(firstName)
                .lastName(lastName).email(email)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User authenticateLocalUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialException("password");
        }
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
}
