package com.assurance.demo.web.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("VOYAGE")
public class DevisVoyage extends Devis {
    // Tranche d'âge
    @NotNull(message = "Tranche d'âge is required.")
    @Pattern(regexp = "^(?:[1-9][0-9]?|100)(?:-(?:[1-9][0-9]?|100))?$", message = "Tranche d'âge must be between '1' and '100' or '1-100'.")

    private String trancheAge;  // Example: "18-30", "31-50", etc.

    // Nationalité du client
    @NotNull(message = "Nationalité is required.")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nationalité must contain only letters and spaces.")
    private String nationalite;  // Nationalité du client

    // Destination du voyage
    @NotNull(message = "Destination is required.")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Destination must contain only letters and spaces.")
    private String destination;  // Destination du voyage (par exemple, "Europe", "Asie", etc.)



    // Getters et setters pour les nouveaux attributs
    public String getTrancheAge() {
        return trancheAge;
    }

    public void setTrancheAge(String trancheAge) {
        this.trancheAge = trancheAge;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


}


