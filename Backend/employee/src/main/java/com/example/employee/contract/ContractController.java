package com.example.employee.contract;

import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.common.exceptions.NotFoundException;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ContractResponseDTO> create(@Valid @RequestBody ContractCreateDTO data) {
        ContractResponseDTO saved = contractService.create(data);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getAll() {
        List<ContractResponseDTO> contracts = contractService.findAll();
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> getById(@PathVariable Long id) throws NotFoundException {
        ContractResponseDTO contract = contractService.findById(id)
                .orElseThrow(() -> new NotFoundException("Contract with id " + id + " not found"));
        return ResponseEntity.ok(contract);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
        boolean deleted = contractService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        throw new NotFoundException("Contract with id " + id + " not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> replace(@PathVariable Long id,
            @Valid @RequestBody ContractUpdateDTO data)
            throws NotFoundException {
        ContractResponseDTO updated = contractService.updateById(id, data)
                .orElseThrow(() -> new NotFoundException("Contract with id " + id + " not found"));
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ContractUpdateDTO data)
            throws NotFoundException {
        ContractResponseDTO updated = contractService.updateById(id, data)
                .orElseThrow(() -> new NotFoundException("Contract with id " + id + " not found"));
        return ResponseEntity.ok(updated);
    }
}
