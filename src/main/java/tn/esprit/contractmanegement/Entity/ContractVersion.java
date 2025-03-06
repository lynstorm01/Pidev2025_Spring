package tn.esprit.contractmanegement.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A reference to the original Contract
    private Long contractId;

    // Store the entire contract snapshot (or relevant fields)
    private String contractNumber;
    private String type;
    private String status;
    private String propertyAddress;
    private double propertyValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Metadata for versioning
    private Integer versionNumber;           // e.g., 1, 2, 3 ...
    private LocalDateTime updatedAt;         // Timestamp of update
    private String updatedBy;                // Who performed the update (e.g. admin username)

    // Optionally store additional fields or JSON representation
    // e.g. private String contractJson; if you want to store entire JSON
}
