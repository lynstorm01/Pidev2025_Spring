package tn.esprit.contractmanegement.Service;

import tn.esprit.contractmanegement.Entity.Claims;

import java.util.List;
import java.util.Optional;

public interface IClaimsService {

    // ✅ Create a claim
    Claims createClaim(Claims claim);

    // ✅ Get all claims
    List<Claims> getAllClaims();

    // ✅ Get a claim by ID
    Optional<Claims> getClaimById(Long id);

    // ✅ Update a claim
    Claims updateClaim(Long claimId, Claims updatedClaim);

    // ✅ Delete a claim
    void deleteClaim(Long id);
}
