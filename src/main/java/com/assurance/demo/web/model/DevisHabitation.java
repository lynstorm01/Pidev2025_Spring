package com.assurance.demo.web.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("HABITATION")
public class DevisHabitation extends Devis{

    @NotBlank(message = "Adresse is required.")
    @Size(min = 3, message = "Adresse must be at least 3 characters long.")
    @Pattern(regexp = "^(?=.*[A-Za-zÀ-ÿ])(?=.*[0-9])[A-Za-zÀ-ÿ0-9,\\s-]+$", message = "Adresse must contain both letters and numbers.")
    private String adresse;
    @NotNull(message = "surface is required.")
    @Positive(message = "Surface must be a positive number.")
    @Min(value = 10, message = "Surface must be at least 10.")
    private int surface;
    @NotNull(message = "value is required.")
    @Positive(message = "Valeur must be a positive number.")
    @Min(value = 1000, message = "Valeur must be at least 1000.")
    private int valeur;





    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }





    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
}
