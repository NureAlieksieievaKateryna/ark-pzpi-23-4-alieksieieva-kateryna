package org.example.reminder.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.reminder.business.facade.dto.BusinessCreateRequest;
import org.example.reminder.common.model.TokenResponse;
import org.example.reminder.common.model.UserRoles;
import org.example.reminder.user.repository.UserRepository;
import org.example.reminder.security.config.jwt.JwtService;
import org.example.reminder.security.config.jwt.dto.TokenCreateRequest;
import org.example.reminder.user.dto.UserResisterRequest;
import org.example.reminder.user.model.UserEntity;
import org.example.reminder.user.dto.UserSignInRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public void registerUser(UserResisterRequest resisterRequest) throws Exception {
        if(userRepository.existsByLogin(resisterRequest.login()) && userRepository.existsByEmail(resisterRequest.email())){
            throw new Exception("User already exists with such login or email");
        }
        UserEntity saveduser = new UserEntity();
        saveduser.setLogin(resisterRequest.login());
        saveduser.setPassword(passwordEncoder.encode(resisterRequest.password()));
        saveduser.setEmail(resisterRequest.email());
        saveduser.setRole(UserRoles.CLIENT);
        userRepository.save(saveduser);
        log.info("User was created successfully");
    }

    public UserEntity registerBusiness(BusinessCreateRequest businessCreateRequest) throws Exception {
        if(userRepository.existsByLogin(businessCreateRequest.login()) && userRepository.existsByEmail(businessCreateRequest.email())){
            throw new Exception("Business already exists with such login or email");
        }
        UserEntity saveduser = new UserEntity();
        saveduser.setLogin(businessCreateRequest.login());
        saveduser.setPassword(passwordEncoder.encode(businessCreateRequest.password()));
        saveduser.setEmail(businessCreateRequest.email());
        saveduser.setRole(UserRoles.BUSINESS);
       return userRepository.save(saveduser);
    }

    public void existByUserName(String login){
         if(userRepository.existsByLogin(login)){
            throw new RuntimeException("User with such login exist %s".formatted(login));
        };
    }

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }

    public TokenResponse login(UserSignInRequest userSignInRequest) throws Exception {
        if(!userRepository.existsByLogin(userSignInRequest.login())){
            throw new Exception("User doesnt exist with such login");
        }
      UserEntity user = userRepository.findByLogin(userSignInRequest.login());
        return TokenResponse.builder().token(jwtService.generateToken(TokenCreateRequest.builder().login(user.getLogin())
                .userRoles(user.getRole()).build())).build();
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with such %s id doent exist".formatted(id)));
    }
}
