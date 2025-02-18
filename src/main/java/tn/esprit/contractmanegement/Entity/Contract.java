package tn.esprit.contractmanegement.Entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Contract {

    public enum ContractStatus {
        ACTIVE, EXPIRED, CANCELED, PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contract number is required")
    private String contractNumber;

    @NotNull(message = "Start date is required")
    @Temporal(TemporalType.DATE)
    private Date startDate;

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

    // Assuming you have a relationship with User:
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
