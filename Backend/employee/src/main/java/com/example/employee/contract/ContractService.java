package com.example.employee.contract;

import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.common.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    public ContractService(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    public ContractResponseDTO create(ContractCreateDTO dto) {
        Contract contract = contractMapper.toEntity(dto);
        Contract saved = contractRepository.save(contract);
        return contractMapper.toResponseDTO(saved);
    }

    public List<ContractResponseDTO> findAll() {
        return contractRepository.findAll()
                .stream()
                .map(contractMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ContractResponseDTO> findById(Long id) {
        return contractRepository.findById(id)
                .map(contractMapper::toResponseDTO);
    }

    @Transactional
    public Optional<ContractResponseDTO> updateById(Long id, ContractUpdateDTO dto) {
        Optional<Contract> existing = contractRepository.findById(id);
        if (existing.isEmpty())
            return Optional.empty();

        Contract contract = existing.get();
        contractMapper.updateFromDTO(dto, contract);
        Contract saved = contractRepository.save(contract);
        return Optional.of(contractMapper.toResponseDTO(saved));
    }

    public boolean deleteById(Long id) {
        if (!contractRepository.existsById(id))
            return false;
        contractRepository.deleteById(id);
        return true;
    }
}
