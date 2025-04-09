package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.contractmanegement.Entity.Devis;


@Repository
public interface DevisRepository extends JpaRepository<Devis,Long> {

}
