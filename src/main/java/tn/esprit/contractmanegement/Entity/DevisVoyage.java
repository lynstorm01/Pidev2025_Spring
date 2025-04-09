package tn.esprit.contractmanegement.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("VOYAGE")
public class DevisVoyage extends Devis {

    @NotNull(message = "Tranche d'âge is required.")
    @Pattern(regexp = "^(?:[1-9][0-9]?|100)(?:-(?:[1-9][0-9]?|100))?$", message = "Tranche d'âge must be between '1' and '100' or '1-100'.")

    private String trancheAge;

    @NotNull(message = "Nationalité is required.")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nationalité must contain only letters and spaces.")
    private String nationalite;

    @NotNull(message = "Destination is required.")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Destination must contain only letters and spaces.")
    private String destination;



}


