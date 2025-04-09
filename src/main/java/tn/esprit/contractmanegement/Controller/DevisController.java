package tn.esprit.contractmanegement.Controller;


import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.contractmanegement.Entity.Devis;
import tn.esprit.contractmanegement.Entity.DevisHabitation;
import tn.esprit.contractmanegement.Entity.DevisVoyage;
import tn.esprit.contractmanegement.Service.DevisService;
import tn.esprit.contractmanegement.dto.DevisResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/devis")
public class DevisController {
    @Autowired
    private DevisService devisService;

    @PostMapping(value = "/ajouter")
    public ResponseEntity<DevisResponseDTO> createDevis(@Valid @RequestBody Devis devis) throws MessagingException {
        DevisResponseDTO createdDevis = devisService.createDevis(devis);
        return ResponseEntity.ok(createdDevis);
    }
    @PostMapping("/ajouter-voyage")
    public ResponseEntity<DevisResponseDTO> createDevisVoyage(@Valid  @RequestBody DevisVoyage devisVoyage) throws MessagingException {
        DevisResponseDTO createdDevis = devisService.createDevis(devisVoyage);
        return ResponseEntity.ok(createdDevis);
    }
    @PostMapping("/ajouter-habitation")
    public ResponseEntity<DevisResponseDTO> createDevisHabitation(@Valid  @RequestBody DevisHabitation devisHabitation) throws MessagingException {
        DevisResponseDTO createdDevis = devisService.createDevis(devisHabitation);
        return ResponseEntity.ok(createdDevis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devis> getDevisById(@PathVariable Long id) {
        Optional<Devis> devis = devisService.getDevisById(id);
        if (devis.isPresent()) {
            return ResponseEntity.ok(devis.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getAllDevis")
    public ResponseEntity<List<Devis>> getAllDevis() {
        List<Devis> devisList = devisService.getAllDevis();
        return ResponseEntity.ok(devisList);
    }

    @PutMapping("modifier/{id}")
    public ResponseEntity<DevisResponseDTO> updateDevis(@PathVariable Long id, @RequestBody DevisVoyage devis) {
        DevisResponseDTO updatedDevis = devisService.updateDevis(id, devis);
        return ResponseEntity.ok(updatedDevis);
    }
    @PutMapping("modifierHab/{id}")
    public ResponseEntity<DevisResponseDTO> updateDevisHabitation(@PathVariable Long id, @RequestBody DevisHabitation devis) {
        DevisResponseDTO updatedDevis = devisService.updateDevis(id, devis);
        return ResponseEntity.ok(updatedDevis);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DevisResponseDTO> deleteDevis(@PathVariable Long id) {
        try {
            DevisResponseDTO response = devisService.deleteDevis(id);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DevisResponseDTO(id, e.getMessage()));
        }
    }

    @PutMapping("/{id}/signer")
    public ResponseEntity<Devis> signerDevis(@PathVariable Long id, @RequestParam("signature") MultipartFile signatureFile) {
        try {
            byte[] signatureBytes = signatureFile.getBytes();
            return ResponseEntity.ok(devisService.signerDevis(id, signatureBytes));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
}




}
