package tn.esprit.contractmanegement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Contract;
import tn.esprit.contractmanegement.Entity.Property;
import tn.esprit.contractmanegement.Entity.User;
import tn.esprit.contractmanegement.Repository.ContractRepository;
import tn.esprit.contractmanegement.Repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
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

    // ✅ Create a contract (Linking it to the static user with id=1)
    @Override
    public Contract createContract(Contract contract) {
        // Check if the contract number already exists
        if (contractRepository.existsByContractNumber(contract.getContractNumber())) {
            throw new RuntimeException("Contract number already exists. Please use a unique number.");
        }
        // Ensure property is correctly linked

        if (contract.getProperty() != null) {
            contract.getProperty().setContract(contract);
        }
        // Set static user (id=1)
        Optional<User> staticUser = userRepository.findById(1L);
        if (staticUser.isPresent()) {
            contract.setUser(staticUser.get());
        } else {
            throw new RuntimeException("Static user with id 1 not found");
        }
        return contractRepository.save(contract);
    }

    // ✅ Get all contracts
    @Override
    public List<Contract> getAllContracts() {
        List<Contract> contracts = contractRepository.findByStatusNot(Contract.ContractStatus.ARCHIVED);
        return contracts.isEmpty() ? new ArrayList<>() : contracts;
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

            // Update property details safely
            if (updatedContract.getProperty() != null) {
                if (existingContract.getProperty() == null) {
                    existingContract.setProperty(new Property());
                }
                existingContract.getProperty().setAddress(updatedContract.getProperty().getAddress());
                existingContract.getProperty().setPropertyType(updatedContract.getProperty().getPropertyType());
                existingContract.getProperty().setValue(updatedContract.getProperty().getValue());
            }
            return contractRepository.save(existingContract);
        }).orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    // ✅ Delete contract (Admin only)
    @Override
    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new RuntimeException("Contract not found");
        }
        contractRepository.deleteById(id);
    }


    ///HASH-SING

//    public boolean verifySignature(Long contractId, byte[] signatureToVerify) {
//        Contract contract = contractRepository.findById(contractId)
//                .orElseThrow(() -> new RuntimeException("Contract not found"));
//
//        if (!contract.isSigned()) {
//            return false; // Contract is not signed
//        }
//
//        String newHash = computeHash(signatureToVerify);
//        return newHash.equals(contract.getSignatureHash());
//    }

    private String computeHash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error computing hash", e);
        }
    }

    //////



    // ✅ Sign contract method
    public Contract signContract(Long contractId, byte[] signature) {
        return contractRepository.findById(contractId).map(contract -> {
            if (contract.isSigned()) {
                throw new RuntimeException("Contract is already signed.");
            }

            // Compute hash of signature
            String signatureHash = computeHash(signature);
            contract.setSignature(signature);
            contract.setSignatureHash(signatureHash);
            contract.setSigned(true);
            contract.setSignatureVerificationStatus("PENDING");
            return contractRepository.save(contract);
        }).orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public List<Contract> getContractsByUserId(Long userId) {
        return contractRepository.findByUser_Id(userId);
    }


    public Contract approveEsignature(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        // Set the status to VERIFIED upon approval
        contract.setSignatureVerificationStatus("VERIFIED");
        return contractRepository.save(contract);
    }

}

