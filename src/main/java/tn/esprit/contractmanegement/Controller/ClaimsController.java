package tn.esprit.contractmanegement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.contractmanegement.Entity.Claims;
import tn.esprit.contractmanegement.Service.ClaimsService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {

    private final ClaimsService claimsService;

    public ClaimsController(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    // ✅ Create a claim
    @PostMapping
    public ResponseEntity<Claims> createClaim(@Valid @RequestBody Claims claim) {
        try {
            Claims createdClaim = claimsService.createClaim(claim);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClaim);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Get all claims
    @GetMapping
    public ResponseEntity<List<Claims>> getAllClaims() {
        List<Claims> claims = claimsService.getAllClaims();
        return claims.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(claims);
    }

    // ✅ Get claim by ID
    @GetMapping("/{id}")
    public ResponseEntity<Claims> getClaimById(@PathVariable Long id) {
        Optional<Claims> claimOptional = claimsService.getClaimById(id);
        return claimOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Update claim
    @PutMapping("/{claimId}")
    public ResponseEntity<Claims> updateClaim(@PathVariable Long claimId, @Valid @RequestBody Claims updatedClaim) {
        Optional<Claims> existingClaim = claimsService.getClaimById(claimId);
        if (existingClaim.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Claims claim = claimsService.updateClaim(claimId, updatedClaim);
        return ResponseEntity.ok(claim);
    }

    // ✅ Delete claim
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        if (claimsService.getClaimById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        claimsService.deleteClaim(id);
        return ResponseEntity.noContent().build(); // ✅ Ensures correct return type
    }
}
