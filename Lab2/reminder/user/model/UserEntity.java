package org.example.reminder.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.reminder.common.model.UserRoles;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name ="users")
@RequiredArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRoles role;
}

