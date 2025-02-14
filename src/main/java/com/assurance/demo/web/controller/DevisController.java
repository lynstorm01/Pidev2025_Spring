package com.assurance.demo.web.controller;


import com.assurance.demo.web.dto.DevisResponseDTO;
import com.assurance.demo.web.model.Devis;
import com.assurance.demo.web.model.DevisHabitation;
import com.assurance.demo.web.model.DevisVoyage;
import com.assurance.demo.web.service.DevisService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/devis")
public class DevisController {
    @Autowired
    private DevisService devisService;


    //Create Devis
    @PostMapping(value = "/ajouter")
    public ResponseEntity<DevisResponseDTO> createDevis( @Valid @RequestBody Devis devis) throws MessagingException {
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



    // Get Devis By Id
    @GetMapping("/{id}")
    public ResponseEntity<Devis> getDevisById(@PathVariable Long id) {
        Optional<Devis> devis = devisService.getDevisById(id);
        // Verify Devis exists
        if (devis.isPresent()) {
            return ResponseEntity.ok(devis.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Retrive All Devis
    @GetMapping("getAllDevis")
    public ResponseEntity<List<Devis>> getAllDevis() {
        List<Devis> devisList = devisService.getAllDevis();
        return ResponseEntity.ok(devisList);
    }

    // Update Devis
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

    // Supprimer un devis
    @DeleteMapping("/{id}")
    public ResponseEntity<DevisResponseDTO> deleteDevis(@PathVariable Long id) {
        try {
            // Appel du service pour supprimer le devis
            DevisResponseDTO response = devisService.deleteDevis(id);

            // Retourner le DTO de réponse avec un code HTTP 200
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            // Retourner une réponse 404 si le devis n'est pas trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DevisResponseDTO(id, e.getMessage()));
        }
    }

//ajouter signature
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
