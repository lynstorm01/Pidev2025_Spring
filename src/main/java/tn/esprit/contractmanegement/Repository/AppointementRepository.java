package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.contractmanegement.Entity.Appointement;

public interface AppointementRepository extends JpaRepository<Appointement, Long> {
}
