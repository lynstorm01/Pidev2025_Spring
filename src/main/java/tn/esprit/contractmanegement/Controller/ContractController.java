package tn.esprit.contractmanegement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ✅ Create a contract (Allowed for Admin & User)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/{userId}")
    public ResponseEntity<Contract> createContract(@PathVariable Long userId, @Valid @RequestBody Contract contract) {
        try {
            Contract createdContract = contractService.createContract(userId, contract);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContract);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // ✅ Get all contracts (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        if (contracts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(contracts);
    }

    // ✅ Get contract by ID (Admin & User)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Optional<Contract> contractOptional = contractService.getContractById(id);
        return contractOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Update contract (Admin & User)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")//("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long contractId, @Valid @RequestBody Contract updatedContract) {
        Optional<Contract> existingContract = contractService.getContractById(contractId);
        if (existingContract.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Contract contract = contractService.updateContract(contractId, updatedContract);
        return ResponseEntity.ok(contract);
    }

    // ✅ Delete contract (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        Optional<Contract> contractOptional = contractService.getContractById(id);
        if (contractOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
