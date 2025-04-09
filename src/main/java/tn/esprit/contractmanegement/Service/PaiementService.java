package tn.esprit.contractmanegement.Service;


import tn.esprit.contractmanegement.Entity.Paiement;
import tn.esprit.contractmanegement.dto.PaiementResponseDTO;

import java.util.List;
import java.util.Optional;

public interface PaiementService {

    public Paiement addPaiement(Long id, Paiement p  );

    public PaiementResponseDTO deletePaiement(Long paiementId);

    public PaiementResponseDTO updatePaiement(Long paiementId, Paiement p);

    public Optional<Paiement> getPaiementById(Long paiementId);
    public List<Paiement> getAllPaiements();

    public Paiement payerEnUneFois(Long devisId, String method);
    public List<Paiement> genererPaiementEchelonne(Long devisId, int nombreEcheances, String method);

    public List<Paiement> getPaiementsByDevis(Long devisId);


}
