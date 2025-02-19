package tn.esprit.contractmanegement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.contractmanegement.Entity.Contract;
import tn.esprit.contractmanegement.Service.ContractService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // ✅ Create a contract
    @PostMapping
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract contract) {
        try {
            Contract createdContract = contractService.createContract(contract);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContract);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Get all contracts
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return contracts.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(contracts);
    }

    // ✅ Get contract by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Optional<Contract> contractOptional = contractService.getContractById(id);
        return contractOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Update contract
    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long contractId, @Valid @RequestBody Contract updatedContract) {
        Optional<Contract> existingContract = contractService.getContractById(contractId);
        if (existingContract.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Contract contract = contractService.updateContract(contractId, updatedContract);
        return ResponseEntity.ok(contract);
    }

    // ✅ Delete contract (FIXED ERROR)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        if (contractService.getContractById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build(); // ✅ Ensures correct return type
    }
}
