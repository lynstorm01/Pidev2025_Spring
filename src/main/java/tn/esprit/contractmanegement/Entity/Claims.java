package tn.esprit.contractmanegement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Claims {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idClaim;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de réclamation est obligatoire.")
    ReclamationType reclamationType;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La date de réclamation est obligatoire.")
    Date reclamationDate;

    @NotBlank(message = "La description ne doit pas être vide.")
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères.")
    String description;

   // @ManyToOne
  //  @JoinColumn(name = "user_id", nullable = false)
   // private User user;
}
