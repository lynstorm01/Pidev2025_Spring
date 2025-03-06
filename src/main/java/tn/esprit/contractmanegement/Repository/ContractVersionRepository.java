package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.contractmanegement.Entity.ContractVersion;

import java.util.List;

public interface ContractVersionRepository extends JpaRepository<ContractVersion, Long> {
    List<ContractVersion> findByContractIdOrderByVersionNumberDesc(Long contractId);
}
