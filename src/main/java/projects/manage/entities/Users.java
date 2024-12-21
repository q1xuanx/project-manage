package projects.manage.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idUser;
    private String username;
    private String passwords;
    private String fullName;
    private String email;
    private String phoneNumber;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'member'")
    private String roles;
    private String tokenRest;
    private LocalDateTime tokenDate;
}
