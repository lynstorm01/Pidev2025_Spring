package com.assurance.demo.web.repository;


import com.assurance.demo.web.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository  extends JpaRepository<Paiement,Long> {
    List<Paiement> findByDevisId(Long devisId);
}
