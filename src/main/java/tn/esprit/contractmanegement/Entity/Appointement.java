package tn.esprit.contractmanegement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idAppointment;

    @NotBlank(message = "La description ne doit pas être vide.")
    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères.")
    String description;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La date soumise est obligatoire.")
    Date dateSubmitted;

    @NotBlank(message = "Le statut est obligatoire.")
    @Pattern(regexp = "^(PENDING|CONFIRMED|CANCELED)$", message = "Le statut doit être 'PENDING', 'CONFIRMED' ou 'CANCELED'.")
    String status;

    //@ManyToOne
//@JoinColumn(name = "user_id", nullable = false)
    //private User user;
}
