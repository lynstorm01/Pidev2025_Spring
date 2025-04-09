package tn.esprit.contractmanegement.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_devis", discriminatorType = DiscriminatorType.STRING)
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nom Client is required.")
    @Size(min = 4, message = "Nom Client must be at least 5 characters long.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Nom Client must contain only letters.")
    private String nomClient;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email Client is required.")
    private String emailClient;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type d'assurance is required.")
    private TypeAssurance typeAssurance;

    @NotNull(message = "Date de début is required.")
    @PastOrPresent(message = "Date de début cannot be in the future.")
    @Temporal(TemporalType.DATE)
    private LocalDate dateDebut;

    @NotNull(message = "Date de fin is required.")
    @Future(message = "Date de fin must be in the future.")
    @Temporal(TemporalType.DATE)
    private LocalDate dateFin;

    @Positive(message = "Montant Total must be positive.")
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    private StatutDevis statut = StatutDevis.EN_ATTENTE;

    private boolean signe = false;

    @Lob
    @Column(name = "signature", columnDefinition = "LONGBLOB")
    private byte[] signature; // Stocke l'image de la signature

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Paiement> paiements;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Devis(Long id, String nomClient, TypeAssurance typeAssurance, LocalDate dateDebut, LocalDate dateFin, StatutDevis statut) {
        this.id = id;
        this.nomClient = nomClient;
        this.typeAssurance = typeAssurance;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }




}
