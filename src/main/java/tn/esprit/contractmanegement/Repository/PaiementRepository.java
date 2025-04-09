package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.contractmanegement.Entity.Paiement;

import java.util.List;

public interface PaiementRepository  extends JpaRepository<Paiement,Long> {
    List<Paiement> findByDevisId(Long devisId);
}
