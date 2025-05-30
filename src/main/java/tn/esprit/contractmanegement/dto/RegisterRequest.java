package tn.esprit.contractmanegement.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.contractmanegement.enumeration.Role;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String password;
    private Integer phoneNumber;
    private Role role;
    @Lob
    private byte[] profileImage;


}
