package com.example.employee.contract;

import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.common.exceptions.NotFoundException;
import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    public ContractService(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    public List<ContractResponseDTO> getContractsByEmployeeId(int employeeId) {
        return contractRepository.findByEmployeeId(employeeId)
                .stream()
                .map(contractMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ContractResponseDTO createContractForEmployee(int employeeId, ContractCreateDTO dto) {
        Contract contract = contractMapper.toEntity(dto);
        // associate employee id — make sure Employee entity or reference is set
        contract.setId(employeeId); // or set employee entity, depending on your model
        Contract saved = contractRepository.save(contract);
        return contractMapper.toResponseDTO(saved);
    }

    public Optional<ContractResponseDTO> updateContract(int employeeId, int contractId, ContractUpdateDTO dto) {
        Optional<Contract> contractOpt = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (contractOpt.isEmpty()) {
            return Optional.empty();
        }
        Contract contract = contractOpt.get();
        contractMapper.updateFromDTO(dto, contract);
        Contract saved = contractRepository.save(contract);
        return Optional.of(contractMapper.toResponseDTO(saved));
    }

    public boolean deleteContract(int employeeId, int contractId) {
        Optional<Contract> contractOpt = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (contractOpt.isEmpty()) {
            return false;
        }
        contractRepository.delete(contractOpt.get());
        return true;
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

    public Optional<ContractResponseDTO> findById(int id) {
        return contractRepository.findById(id)
                .map(contractMapper::toResponseDTO);
    }

    public Optional<ContractResponseDTO> findByIdAndEmployeeId(int contractId, int employeeId) {
        return contractRepository.findByIdAndEmployeeId(contractId, employeeId)
                .map(contractMapper::toResponseDTO);
    }

    @Transactional
    public Optional<ContractResponseDTO> updateByIdAndEmployeeId(int contractId, int employeeId,
            ContractUpdateDTO dto) {
        Optional<Contract> existing = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (existing.isEmpty())
            return Optional.empty();

        Contract contract = existing.get();
        contractMapper.updateFromDTO(dto, contract);
        Contract saved = contractRepository.save(contract);
        return Optional.of(contractMapper.toResponseDTO(saved));
    }

    public boolean deleteByIdAndEmployeeId(int contractId, int employeeId) {
        Optional<Contract> contractOpt = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (contractOpt.isEmpty())
            return false;

        contractRepository.delete(contractOpt.get());
        return true;
    }
}
