package tn.esprit.contractmanegement.Entity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
public class Contract {

    public enum ContractStatus {
        ACTIVE, EXPIRED, CANCELED, PENDING,ARCHIVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contract number is required")
    private String contractNumber;

    @NotNull(message = "Start date is required")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent
    private Date startDate;

    @FutureOrPresent
    @NotNull(message = "End date is required")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Valid
    @NotNull(message = "Property is required")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;

    @Lob
    @Column(name = "signature", columnDefinition = "LONGBLOB")
    private byte[] signature;
    private boolean signed;
    private String signatureHash;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

}
