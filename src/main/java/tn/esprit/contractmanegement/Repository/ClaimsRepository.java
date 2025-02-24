package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.contractmanegement.Entity.Claims;

public interface ClaimsRepository extends JpaRepository<Claims, Long> {
}
