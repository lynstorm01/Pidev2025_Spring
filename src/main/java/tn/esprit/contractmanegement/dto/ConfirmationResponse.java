package tn.esprit.contractmanegement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationResponse {
    private String messageResponse;
    private boolean confirmedEmail;
}
