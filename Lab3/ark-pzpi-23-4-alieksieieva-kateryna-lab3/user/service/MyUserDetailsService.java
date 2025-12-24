package org.example.reminder.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.reminder.common.model.UserPrincipal;
import org.example.reminder.user.repository.UserRepository;
import org.example.reminder.user.model.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("is here---------> %s".formatted(username));
        UserEntity user = repo.findByLogin(username);

        if (user == null) {
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        log.info("User is exist");
        return new UserPrincipal(user);
    }

}



