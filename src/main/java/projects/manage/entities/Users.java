package projects.manage.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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
}
