package tn.esprit.contractmanegement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.contractmanegement.Entity.Contract;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByEndDateBetween(LocalDate start, LocalDate end);

    List<Contract> findByEndDate(Date endDate);
    List<Contract> findByUser_Id(Long userId);
    boolean existsByContractNumber(String contractNumber);
    List<Contract> findByStatusNot(Contract.ContractStatus status);
}