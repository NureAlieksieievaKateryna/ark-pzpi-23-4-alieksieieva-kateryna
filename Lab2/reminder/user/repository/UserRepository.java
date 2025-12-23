package org.example.reminder.user.repository;

import org.example.reminder.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    Optional<UserEntity> findById(Long id);
}
