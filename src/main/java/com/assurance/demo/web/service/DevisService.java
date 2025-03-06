package com.assurance.demo.web.service;

import com.assurance.demo.web.dto.DevisResponseDTO;
import com.assurance.demo.web.model.Devis;
import com.assurance.demo.web.model.DevisVoyage;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;

public interface DevisService {

    // Créer un devis
    DevisResponseDTO  createDevis(Devis devis) throws MessagingException;

    // Lire un devis par son identifiant
    Optional<Devis>  getDevisById(Long id);

    // Lire tous les devis
    List<Devis> getAllDevis();

    // Mettre à jour un devis
    DevisResponseDTO updateDevis(Long id, Devis devis);

    // Supprimer un devis
    DevisResponseDTO deleteDevis(Long id);

    //ajouter signature
     Devis signerDevis(Long devisId, byte[] signature);

    public void calculerPrimeDevisVoyage(DevisVoyage devisVoyage);


}
