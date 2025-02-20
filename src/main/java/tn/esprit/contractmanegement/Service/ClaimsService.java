package tn.esprit.contractmanegement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Claims;
import tn.esprit.contractmanegement.Repository.ClaimsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimsService implements IClaimsService {

    private final ClaimsRepository claimsRepository;

    @Autowired
    public ClaimsService(ClaimsRepository claimsRepository) {
        this.claimsRepository = claimsRepository;
    }

    // ✅ Create a claim
    @Override
    public Claims createClaim(Claims claim) {
        return claimsRepository.save(claim);
    }

    // ✅ Get all claims
    @Override
    public List<Claims> getAllClaims() {
        List<Claims> claims = claimsRepository.findAll();
        return claims.isEmpty() ? new ArrayList<>() : claims;
    }

    // ✅ Get a claim by ID
    @Override
    public Optional<Claims> getClaimById(Long id) {
        return claimsRepository.findById(id);
    }

    // ✅ Update claim
    @Override
    public Claims updateClaim(Long claimId, Claims updatedClaim) {
        return claimsRepository.findById(claimId).map(existingClaim -> {
            existingClaim.setReclamationType(updatedClaim.getReclamationType());
            existingClaim.setReclamationDate(updatedClaim.getReclamationDate());
            existingClaim.setDescription(updatedClaim.getDescription());
            // Ensure user exists or update as needed
           // existingClaim.setUser(updatedClaim.getUser());
            return claimsRepository.save(existingClaim);
        }).orElseThrow(() -> new RuntimeException("Claim not found"));
    }

    // ✅ Delete claim
    @Override
    public void deleteClaim(Long id) {
        if (!claimsRepository.existsById(id)) {
            throw new RuntimeException("Claim not found");
        }
        claimsRepository.deleteById(id);
    }
}
