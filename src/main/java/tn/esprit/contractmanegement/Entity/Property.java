package tn.esprit.contractmanegement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Address is required")
    private String address;

    @Positive(message = "Area must be positive")
    private double area;

    @NotNull(message = "Property type is required")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType; // e.g., Residential, Commercial, Industrial, Agricultural

    public enum PropertyType {
        RESIDENTIAL,
        COMMERCIAL,
        INDUSTRIAL,
        AGRICULTURAL
    }

    @NotNull(message = "Value is required")
    @Min(value = 0, message = "Value must be non-negative")
    private Double value;

    @OneToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private Contract contract;
}
