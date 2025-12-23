package org.example.reminder.user.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.reminder.common.mappers.UserMapper;
import org.example.reminder.user.dto.UserResisterRequest;
import org.example.reminder.user.dto.UserResponse;
import org.example.reminder.user.model.UserEntity;
import org.example.reminder.user.service.UserService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserMapper userMapper;
    private final UserService userService;

    public void registerUser(UserResisterRequest resisterRequest) throws Exception {
        userService.existByUserName(resisterRequest.login());
        userService.registerUser(resisterRequest);
    }

    public UserResponse findUserById(Long id) {
        UserEntity userEntity = userService.findUserById(id);
        return userMapper.toUserDto(userEntity);
    }
}
