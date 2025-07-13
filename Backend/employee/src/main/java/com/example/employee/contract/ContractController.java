package com.example.employee.contract;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import com.example.employee.common.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

import java.util.List;
import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.contract.ContractService;

@RestController
@RequestMapping("/employees/{employeeId}/contracts")
public class ContractController {

    // Endpoints for listing/Creating/Updating/Deleting contract details
    // based on employee id

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getContracts(@PathVariable int employeeId) {
        return ResponseEntity.ok(contractService.getContractsByEmployeeId(employeeId));
    }

    @PostMapping
    public ResponseEntity<ContractResponseDTO> createContract(
            @PathVariable int employeeId,
            @Valid @RequestBody ContractCreateDTO contractDto) {
        ContractResponseDTO created = contractService.createContractForEmployee(employeeId, contractDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{contractId}")
    public ResponseEntity<ContractResponseDTO> updateContract(
            @PathVariable int employeeId,
            @PathVariable int contractId,
            @Valid @RequestBody ContractUpdateDTO contractDto) {

        ContractResponseDTO updated = contractService
                .updateContract(employeeId, contractId, contractDto)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Contract not found"));

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{contractId}")
    public ResponseEntity<Void> deleteContract(
            @PathVariable int employeeId,
            @PathVariable int contractId) {
        contractService.deleteContract(employeeId, contractId);
        return ResponseEntity.noContent().build();
    }
}
