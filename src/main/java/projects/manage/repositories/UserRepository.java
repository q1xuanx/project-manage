package projects.manage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.manage.entities.Users;

public interface UserRepository extends JpaRepository<Users,Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Users findByUsernameAndPasswords(String username, String password);
}
