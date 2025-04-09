package tn.esprit.contractmanegement.Controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.contractmanegement.Entity.Paiement;
import tn.esprit.contractmanegement.Service.PaiementService;
import tn.esprit.contractmanegement.dto.PaiementResponseDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

        @Autowired
        private PaiementService paiementService;
        @PostMapping("/{id}")
        public ResponseEntity<Paiement> addPaiement(@PathVariable Long id,
                                                    @RequestBody @Valid Paiement paiement)
        {
            paiementService.addPaiement(id, paiement);
            return ResponseEntity.status(HttpStatus.CREATED).body(paiement);
        }
    // GET a Paiement by ID
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiement(@PathVariable Long id) {
        Optional<Paiement> paiement = paiementService.getPaiementById(id);
        // Verify Devis exists
        if (paiement.isPresent()) {
            return ResponseEntity.ok(paiement.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//retrive all paiements
    @GetMapping
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        List<Paiement> paiements = paiementService.getAllPaiements();
        return ResponseEntity.ok(paiements);  // Return HTTP 200 with the list of Paiements
    }

    // UPDATE a Paiement by ID
    @PutMapping("/{paiementId}")
    public ResponseEntity<PaiementResponseDTO> updatePaiement(@PathVariable Long paiementId,
                                                              @RequestBody @Valid Paiement paiement) {
        PaiementResponseDTO updatedPaiement = paiementService.updatePaiement(paiementId, paiement);
        return ResponseEntity.ok(updatedPaiement);  // Return HTTP 200 with the updated Paiement
    }

    // DELETE a Paiement by ID
    @DeleteMapping("/{paiementId}")
    public ResponseEntity<PaiementResponseDTO> deletePaiement(@PathVariable Long paiementId) {

        try {
            // Appel du service pour supprimer le devis
            PaiementResponseDTO response =       paiementService.deletePaiement(paiementId);

            // Retourner le DTO de réponse avec un code HTTP 200
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            // Retourner une réponse 404 si le devis n'est pas trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaiementResponseDTO(paiementId, e.getMessage()));
        }
    }

    /**
     * Payer en une seule fois
     */
    @PostMapping("/payer-en-une-fois/{devisId}")
    public ResponseEntity<Paiement> payerEnUneFois(@PathVariable Long devisId, @RequestParam String method) {
        Paiement paiement = paiementService.payerEnUneFois(devisId, method);
        return ResponseEntity.ok(paiement);
    }

    /**
     * Générer un paiement échelonné
     */
    @PostMapping("/payer-en-plusieurs-fois/{devisId}")
    public ResponseEntity<List<Paiement>> payerEnPlusieursFois(@PathVariable Long devisId,
                                                               @RequestParam int nombreEcheances,
                                                               @RequestParam String method) {
        List<Paiement> paiements = paiementService.genererPaiementEchelonne(devisId, nombreEcheances, method);
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/devis/{devisId}")
    public ResponseEntity<List<Paiement>> getPaiementsByDevis(@PathVariable Long devisId) {
        List<Paiement> paiements = paiementService.getPaiementsByDevis(devisId);
        return ResponseEntity.ok(paiements);
    }

}











