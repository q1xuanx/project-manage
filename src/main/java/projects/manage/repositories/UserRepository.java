package projects.manage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.manage.entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    User findByUsernameAndPassword(String username, String password);
}
