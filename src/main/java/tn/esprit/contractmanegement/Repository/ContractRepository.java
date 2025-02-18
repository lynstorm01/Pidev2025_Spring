package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.contractmanegement.Entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}