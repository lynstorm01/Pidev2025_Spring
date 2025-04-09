package tn.esprit.contractmanegement.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.contractmanegement.Entity.User;
import tn.esprit.contractmanegement.enumeration.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    User getUserByEmail(String email);

    List<User> findUserByRole(Role role);






}
