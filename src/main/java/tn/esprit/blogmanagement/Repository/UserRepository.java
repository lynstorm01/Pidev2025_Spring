package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.blogmanagement.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByUsernameIn(List<String> usernames);

    Optional<Object> findByEmail(String email);
}
