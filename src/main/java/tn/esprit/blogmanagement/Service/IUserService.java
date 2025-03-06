package tn.esprit.blogmanagement.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.esprit.blogmanagement.Entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User registerUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void deleteUser(Long id);
    Optional<User> getUsername(String username);
    List<User> findByUsernameIn(List<String> usernames);
}
