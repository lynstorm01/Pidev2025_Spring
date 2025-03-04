package tn.esprit.contractmanegement.Service;

import tn.esprit.contractmanegement.Entity.Contract;

import java.util.List;
import java.util.Optional;

public interface IContractService {

    // ✅ Create a contract (Only if user exists)
    Contract createContract(Contract contract);

    List<Contract> getAllContracts();
    Optional<Contract> getContractById(Long id);
    Contract updateContract(Long contractId, Contract updatedContract);
    void deleteContract(Long id);
    public Contract signContract(Long contractId, byte[] signature);
}
