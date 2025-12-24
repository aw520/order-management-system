package com.example.userservice.security;

import org.apache.commons.validator.routines.EmailValidator;
import org.passay.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ValidatorConfig {

    @Bean
    public EmailValidator emailValidator() {
        return EmailValidator.getInstance();
    }

    @Bean
    public PasswordValidator passwordValidator() {
        PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(
                        new LengthRule(8, 30),
                        new CharacterCharacteristicsRule(3,
                                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                                new CharacterRule(EnglishCharacterData.Digit, 1),
                                new CharacterRule(EnglishCharacterData.Special, 1)
                        ),
                        new WhitespaceRule()
                )
        );
        return passwordValidator;
    }
}
