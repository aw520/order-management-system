package com.example.userservice.service.impl;

import com.example.userservice.constant.UserRole;
import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.entity.User;
import com.example.userservice.exception.AuthorizationException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.UserRepositoryCustom;
import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.ServiceUtil;
import com.example.userservice.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(UUID actUserId, UUID targetUserId, UserProfileUpdateRequest request) {
        User actUser = userRepository.findByUserId(actUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+actUserId.toString()+" not found in database"));
        User targetUser = userRepository.findByUserId(targetUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+targetUserId.toString()+" not found in database"));
        //one can only change self profile unless they are admin or root
        if(!actUserId.equals(targetUserId)){
            if(! UserRole.ableToEditUser(
                    actUser.getRoles(), targetUser.getRoles())){
                throw new AuthorizationException("You are not authorized to edit this user's profile");
            }
        }
        //update profile
        targetUser.setEmail(request.getEmail());
        targetUser.setFirstName(request.getFirstName());
        targetUser.setLastName(request.getLastName());
        targetUser.setAddress(request.getAddress());
        targetUser = userRepository.save(targetUser);

        UserProfileResponse response = ServiceUtil.userToUserProfileResponseComplete(targetUser);
        return response;
    }

    @Override
    public UserProfileResponse getUserProfile(UUID actUserId, UUID targetUserId) {
        User actUser = userRepository.findByUserId(actUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+actUserId.toString()+" not found in database"));
        User targetUser = userRepository.findByUserId(targetUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+targetUserId.toString()+" not found in database"));
        if(!actUserId.equals(targetUserId)){
            if(! UserRole.ableToFetchUser(
                actUser.getRoles(), targetUser.getRoles())){
                return ServiceUtil.userToUserProfileResponseBasic(targetUser);
            }
        }
        return ServiceUtil.userToUserProfileResponseComplete(targetUser);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserRoles(UUID actUserId, UUID targetUserId, Set<String> role) {
        User actingUser = userRepository.findByUserId(actUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+actUserId.toString()+" not found in database"));
        User targetUser = userRepository.findByUserId(targetUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+targetUserId.toString()+" not found in database"));
        //only root user allow to update others role
        if(!actingUser.getRoles().contains(UserRole.ROOT)){
            throw new AuthorizationException("You are not authorized to update this user's role");
        }
        Set<UserRole> roles = UserRole.getRolesFromNameSet(role);
        targetUser.setRoles(roles);
        userRepository.save(targetUser);
        UserProfileResponse response = ServiceUtil.userToUserProfileResponseComplete(targetUser);
        return response;
    }

    @Override
    public List<UserProfileResponse> searchUser(UserSearchCriteria criteria, UUID actUserId) {
        User actingUser = userRepository.findByUserId(actUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+actUserId.toString()+" not found in database"));
        List<User> users = userRepositoryCustom.searchUser(criteria);
        List<UserProfileResponse> response = new ArrayList<>();
        for(User user: users){
            if(UserRole.ableToFetchUser(actingUser.getRoles(), user.getRoles())){
                response.add(ServiceUtil.userToUserProfileResponseComplete(user));
            }else {
                response.add(ServiceUtil.userToUserProfileResponseBasic(user));
            }
        }
        return response;
    }
}
