package tn.esprit.blogmanagement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.blogmanagement.DTO.PostDTO;
import tn.esprit.blogmanagement.DTO.UserDTO;
import tn.esprit.blogmanagement.Service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Get all posts with only userId, commentIds, and replyIds
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> userDTOs = userService.getAllUsers().stream()
                    .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                    .toList();

            if (userDTOs.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no users exist
            }

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
