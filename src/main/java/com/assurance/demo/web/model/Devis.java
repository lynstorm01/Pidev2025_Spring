package com.assurance.demo.web.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_devis", discriminatorType = DiscriminatorType.STRING)
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identifiant unique

    @NotBlank(message = "Nom Client is required.")
    @Size(min = 4, message = "Nom Client must be at least 5 characters long.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Nom Client must contain only letters.")
    private String nomClient;  // Nom du client

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email Client is required.")
    private String emailClient; // Client's email address

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type d'assurance is required.")
    private TypeAssurance typeAssurance;  // Type d'assurance (auto, santé, habitation...)

    @NotNull(message = "Date de début is required.")
    @PastOrPresent(message = "Date de début cannot be in the future.")
    @Temporal(TemporalType.DATE)
    private LocalDate dateDebut;  // Date de début de la couverture

    @NotNull(message = "Date de fin is required.")
    @Future(message = "Date de fin must be in the future.")
    @Temporal(TemporalType.DATE)
    private LocalDate dateFin;  // Date de fin de la couverture

    @Positive(message = "Montant Total must be positive.")
    private BigDecimal montantTotal;  // Total amount

    @Enumerated(EnumType.STRING)
    private StatutDevis statut = StatutDevis.EN_ATTENTE;

    // signature devis

    // signature status
    private boolean signe = false;

    @Lob

    private byte[] signature; // Stocke l'image de la signature


    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Paiement> paiements;
    // Many-to-One relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }



    public boolean isSigne() {
        return signe;
    }

    public void setSigne(boolean signe) {
        this.signe = signe;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }



    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Devis() {
    }

    public TypeAssurance getTypeAssurance() {
        return typeAssurance;
    }

    public Devis(Long id, String nomClient, TypeAssurance typeAssurance, LocalDate dateDebut, LocalDate dateFin, StatutDevis statut) {
        this.id = id;
        this.nomClient = nomClient;
        this.typeAssurance = typeAssurance;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public void setTypeAssurance(TypeAssurance typeAssurance) {
        this.typeAssurance = typeAssurance;
    }


    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutDevis getStatut() {
        return statut;
    }

    public void setStatut(StatutDevis statut) {
        this.statut = statut;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
}
