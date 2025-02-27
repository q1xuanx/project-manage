package projects.manage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projects.manage.entities.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users findByTokenRest(String tokenRest);
}
