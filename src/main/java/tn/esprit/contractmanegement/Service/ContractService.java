package tn.esprit.contractmanegement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Contract;
import tn.esprit.contractmanegement.Entity.Property;
import tn.esprit.contractmanegement.Entity.User;
import tn.esprit.contractmanegement.Repository.ContractRepository;
import tn.esprit.contractmanegement.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService implements IContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    // ✅ Create a contract (Only if user exists)
    @Override
    public Contract createContract(Long userId, Contract contract) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            contract.setUser(user.get()); // Assign user to contract

            // Ensure property is correctly linked
            if (contract.getProperty() != null) {
                contract.getProperty().setContract(contract);
            }

            return contractRepository.save(contract);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // ✅ Get all contracts
    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    // ✅ Get a single contract
    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    // ✅ Update contract
    @Override
    public Contract updateContract(Long contractId, Contract updatedContract) {
        return contractRepository.findById(contractId).map(existingContract -> {
            existingContract.setContractNumber(updatedContract.getContractNumber());
            existingContract.setStartDate(updatedContract.getStartDate());
            existingContract.setEndDate(updatedContract.getEndDate());
            existingContract.setType(updatedContract.getType());
            existingContract.setStatus(updatedContract.getStatus());

            // Update property details if present
            if (updatedContract.getProperty() != null) {
                if (existingContract.getProperty() == null) {
                    existingContract.setProperty(updatedContract.getProperty());
                } else {
                    existingContract.getProperty().setAddress(updatedContract.getProperty().getAddress());
                    existingContract.getProperty().setPropertyType(updatedContract.getProperty().getPropertyType());
                    existingContract.getProperty().setValue(updatedContract.getProperty().getValue());
                }
            }

            return contractRepository.save(existingContract);
        }).orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    // ✅ Delete contract (Admin only)
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new RuntimeException("Contract not found");
        }
        contractRepository.deleteById(id);
    }
}
