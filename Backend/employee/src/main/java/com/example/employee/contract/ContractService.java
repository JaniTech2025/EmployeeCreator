package com.example.employee.contract;

import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.common.exceptions.NotFoundException;
import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.example.employee.common.exceptions.ValidationErrors;
import com.example.employee.common.exceptions.ServiceValidationException;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

//Add logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final ContractMapper contractMapper;

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    public ContractService(ContractRepository contractRepository,
            ContractMapper contractMapper,
            EmployeeRepository employeeRepository) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.employeeRepository = employeeRepository;
    }

    public List<ContractResponseDTO> getContractsByEmployeeId(int employeeId) {
        logger.debug("Getting contracts for employee id: {}", employeeId);
        List<ContractResponseDTO> contracts = contractRepository.findByEmployeeId(employeeId)
                .stream()
                .map(contractMapper::toResponseDTO)
                .collect(Collectors.toList());
        logger.debug("Found {} contracts for employee id: {}", contracts.size(), employeeId);
        return contracts;
    }

    @Transactional
    public ContractResponseDTO createContractForEmployee(int employeeId, ContractCreateDTO dto) {
        logger.debug("Creating contract for employee ID: {}, DTO: {}", employeeId, dto);

        if (dto.getContractType() == null) {
            logger.warn("Contract type is missing: {}", dto);
            ValidationErrors errors = new ValidationErrors();
            errors.add("contractType", "Contract type is required");
            throw new ServiceValidationException(errors);
        }

        Contract contract = contractMapper.toEntity(dto);
        // contract.setEmployeeId(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        contract.setEmployee(employee);

        Contract saved = contractRepository.save(contract);
        logger.debug("Created contract ID: {} for employee ID: {}", saved.getId(), employeeId);
        return contractMapper.toResponseDTO(saved);
    }

    @Transactional
    public Optional<ContractResponseDTO> updateContract(int employeeId, int contractId, ContractUpdateDTO dto) {
        logger.debug("Updating contract ID: {} for employee ID: {}", contractId, employeeId);

        Optional<Contract> contractOpt = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (contractOpt.isEmpty()) {
            return Optional.empty();
        }
        Contract contract = contractOpt.get();
        contractMapper.updateFromDTO(dto, contract);
        Contract saved = contractRepository.save(contract);
        logger.debug("Saved updated contract ID: {} for employee ID: {}", saved.getId(), employeeId);
        return Optional.of(contractMapper.toResponseDTO(saved));
    }

    @Transactional
    public boolean deleteContract(int employeeId, int contractId) {

        logger.debug("Deleting contract id: {} for employee with id: {}", employeeId, contractId);

        Optional<Contract> contractOpt = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (contractOpt.isEmpty()) {
            logger.warn("Contract with id: {} not found for employee id: {}", contractId, employeeId);
            return false;
        }
        contractRepository.delete(contractOpt.get());
        logger.debug("Deleted contract id: {} for employee with id: {}", employeeId, contractId);

        return true;
    }

    @Transactional
    public ContractResponseDTO create(ContractCreateDTO dto) {
        logger.debug("Creating contract with details: {}", dto);

        if (dto.getContractType() == null) {
            logger.warn("Contract type is missing");
            ValidationErrors errors = new ValidationErrors();
            errors.add("contractType", "Contract type is required");
            throw new ServiceValidationException(errors);
        }

        Contract contract = contractMapper.toEntity(dto);
        Contract saved = contractRepository.save(contract);
        logger.debug("Created contract with details: {}", dto);
        return contractMapper.toResponseDTO(saved);
    }

    public List<ContractResponseDTO> findAll() {
        logger.debug("Fetching all contracts");
        List<ContractResponseDTO> allContracts = contractRepository.findAll()
                .stream()
                .map(contractMapper::toResponseDTO)
                .collect(Collectors.toList());
        logger.debug("Total number of contracts fetched: {}", allContracts.size());
        return allContracts;
    }

    public Optional<ContractResponseDTO> findById(int id) {
        logger.debug("Finding contract by id: {}", id);
        return contractRepository.findById(id)
                .map(contractMapper::toResponseDTO);
    }

    public Optional<ContractResponseDTO> findByIdAndEmployeeId(int contractId, int employeeId) {
        logger.debug("Finding contract by ID: {} and employee ID: {}", contractId, employeeId);
        return contractRepository.findByIdAndEmployeeId(contractId, employeeId)
                .map(contractMapper::toResponseDTO);
    }

    @Transactional
    // Transactional annotation ensures atomicity i.e. All or None
    // Rolls back on failure
    public Optional<ContractResponseDTO> updateByIdAndEmployeeId(int contractId, int employeeId,
            ContractUpdateDTO dto) {

        logger.debug("Updating for contract ID: {}, employee ID: {}", contractId, employeeId);
        Optional<Contract> existing = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (existing.isEmpty()) {
            logger.warn("Could not find contract id corresponding to employee id");
            return Optional.empty();
        }

        Contract contract = existing.get();
        contractMapper.updateFromDTO(dto, contract);
        Contract saved = contractRepository.save(contract);
        logger.debug("Updated for contract ID: {}, employee ID: {}", contractId, employeeId);
        return Optional.of(contractMapper.toResponseDTO(saved));
    }

    @Transactional
    public boolean deleteByIdAndEmployeeId(int contractId, int employeeId) {
        logger.debug("Deleting contract by ID: {}, employee ID: {}", contractId, employeeId);
        Optional<Contract> contractOpt = contractRepository.findByIdAndEmployeeId(contractId, employeeId);
        if (contractOpt.isEmpty()) {
            logger.warn("Contract id: {} not found for deletion for employee id: {}", contractId, employeeId);
            return false;
        }

        contractRepository.delete(contractOpt.get());
        logger.debug("Deleted contract by ID: {}, employee ID: {}", contractId, employeeId);
        return true;
    }
}
