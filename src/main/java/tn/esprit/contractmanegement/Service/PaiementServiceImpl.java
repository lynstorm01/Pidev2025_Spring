package tn.esprit.contractmanegement.Service;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Devis;
import tn.esprit.contractmanegement.Entity.Paiement;
import tn.esprit.contractmanegement.Repository.DevisRepository;
import tn.esprit.contractmanegement.Repository.PaiementRepository;
import tn.esprit.contractmanegement.dto.PaiementResponseDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementServiceImpl implements PaiementService{

        @Autowired
        private PaiementRepository paiementRepository;
        @Autowired
        private DevisRepository devisRepository;

        public Paiement addPaiement(Long devisId, Paiement paiement) {
            Optional<Devis> devisOptional = devisRepository.findById(devisId);

            Devis devis = devisOptional.get();
            paiement.setDevis(devis);
                return paiementRepository.save(paiement);
        }

        // Delete Paiement by ID

        public PaiementResponseDTO deletePaiement(Long id) {
            Optional<Paiement> PaiementToDeleteOptional = paiementRepository.findById(id);
            if (PaiementToDeleteOptional.isPresent()) {
                Paiement paiementToDelete = PaiementToDeleteOptional.get();
                paiementRepository.delete(paiementToDelete);
                return new PaiementResponseDTO(id, "Paiement with ID " + id + " has been deleted successfully.");
            } else {
                return new PaiementResponseDTO(id, "Paiement with ID " + id + " not found.");
            }
        }

    // Update Paiement by ID

    public PaiementResponseDTO updatePaiement(Long id,Paiement paiement) {
        Optional<Paiement> paiementToUpdateOptional = paiementRepository.findById(id);
        if (paiementToUpdateOptional.isPresent()) {
            Paiement paiementToUpdate = paiementToUpdateOptional.get();
            BeanUtils.copyProperties(paiement, paiementToUpdate, "id","devis");

            paiementRepository.save(paiementToUpdate);
            return new PaiementResponseDTO(paiementToUpdate.getId(), "paiement with ID " + paiementToUpdate.getId() + " updated successfully.");
        } else {
            return new PaiementResponseDTO(id, "paiement with ID " + id + " not found.");
        }
    }

    // Get Paiement by ID
    public Optional<Paiement> getPaiementById(Long id) {
        return  paiementRepository.findById(id);
    }

    public List<Paiement> getAllPaiements() {
        List<Paiement> paiementList = paiementRepository.findAll();
        return paiementList;
    }


    /**
     * Payer en une seule fois
     */
    public Paiement payerEnUneFois(Long devisId, String method) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new RuntimeException("Devis introuvable"));

        Paiement paiement = new Paiement();
        paiement.setDevis(devis);
        paiement.setMontant(devis.getMontantTotal());
        paiement.setDatePaiement(new Date());
        paiement.setMethod(method);
        paiement.setStatut("Payé");

        return paiementRepository.save(paiement);
    }
    /**
     * Générer un paiement échelonné
     */
    public List<Paiement> genererPaiementEchelonne(Long devisId, int nombreEcheances, String method) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new RuntimeException("Devis introuvable"));

        BigDecimal montantEcheance = devis.getMontantTotal().divide(BigDecimal.valueOf(nombreEcheances), RoundingMode.HALF_UP);
        List<Paiement> paiements = new ArrayList<>();

        for (int i = 0; i < nombreEcheances; i++) {
            LocalDate dateEcheance = devis.getDateDebut().plusMonths(i); // ✅ Utilisation correcte de plusMonths()

            Paiement paiement = new Paiement();
            paiement.setDevis(devis);
            paiement.setMontant(montantEcheance);
            paiement.setDatePaiement(Date.from(dateEcheance.atStartOfDay(ZoneId.systemDefault()).toInstant())); // ✅ Conversion correcte
            paiement.setMethod(method);

            // Marquer le premier paiement comme "Payé" et les autres comme "En attente"
            if (i == 0) {
                paiement.setStatut("Payé");
            } else {
                paiement.setStatut("En attente");
            }

            paiements.add(paiement);
        }

        return paiementRepository.saveAll(paiements);
    }



    public List<Paiement> getPaiementsByDevis(Long devisId) {
        return paiementRepository.findByDevisId(devisId);
    }



}










