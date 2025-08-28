package com.example.employee.employeedetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;
import jakarta.transaction.Transactional;

import com.example.employee.contract.Contract;
import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractDTO;

import com.example.employee.contract.ContractRepository;
import com.example.employee.contractarchive.ContractArchive;
import com.example.employee.contractarchive.ContractArchiveMapper;
import com.example.employee.contractarchive.ContractArchiveRepository;

import com.example.employee.employeearchive.EmployeeArchive;
import com.example.employee.employeearchive.EmployeeArchiveMapper;
import com.example.employee.employeearchive.EmployeeArchiveRepository;

import com.example.employee.employeedetails.dto.CreateEmployeeDTO;
import com.example.employee.employeedetails.dto.EmployeeWithContractsDTO;
import com.example.employee.employeedetails.dto.UpdateEmployeeDTO;
import com.example.employee.employeedetails.mapper.EmployeeMapper;
import com.example.employee.common.exceptions.NotFoundException;

import com.example.employee.common.exceptions.ServiceValidationException;
import com.example.employee.common.exceptions.ValidationErrors;

// Import logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final ContractArchiveRepository contractArchiveRepository;

    private final ContractArchiveMapper contractArchiveMapper;
    private final EmployeeArchiveRepository employeeArchiveRepository;
    private final EmployeeArchiveMapper employeeArchiveMapper;
    private final EmployeeMapper employeeMapper;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(
            EmployeeRepository employeeRepository,
            ContractRepository contractRepository,
            EmployeeArchiveRepository employeeArchiveRepository,
            ContractArchiveRepository contractArchiveRepository,
            EmployeeArchiveMapper employeeArchiveMapper,
            ContractArchiveMapper contractArchiveMapper,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.employeeArchiveRepository = employeeArchiveRepository;
        this.contractArchiveRepository = contractArchiveRepository;
        this.employeeArchiveMapper = employeeArchiveMapper;
        this.contractArchiveMapper = contractArchiveMapper;
        this.employeeMapper = employeeMapper;
    }

    // public Employee create(CreateEmployeeDTO dto) {
    // logger.debug("Creating a new employee with data: {}", dto);

    // Employee employee = new Employee();
    // employee.setFirstName(dto.getFirstName());
    // employee.setLastName(dto.getLastName());
    // employee.setEmail(dto.getEmail());
    // employee.setMobileNumber(dto.getMobileNumber());
    // employee.setResidentialAddress(dto.getResidentialAddress());
    // employee.setEmployeeStatus(dto.getEmployeeStatus());
    // employee.setCreatedAt(dto.getCreatedAt());
    // employee.setUpdatedAt(dto.getUpdatedAt());
    // employee.setPhotoUrl(dto.getPhotoUrl());

    // List<ContractCreateDTO> contractDtos = dto.getContracts();
    // if (contractDtos != null && !contractDtos.isEmpty()) {
    // List<Contract> contracts = contractDtos.stream().map(contractDto -> {
    // Contract contract = new Contract();
    // contract.setContractType(contractDto.getContractType());
    // contract.setContractTerm(contractDto.getContractTerm());
    // contract.setStartDate(contractDto.getStartDate());
    // contract.setFinishDate(contractDto.getFinishDate());
    // contract.setOngoing(contractDto.isOngoing());
    // contract.setWorkType(contractDto.getWorkType());
    // contract.setHoursPerWeek(contractDto.getHoursPerWeek());
    // return contract;
    // }).toList();

    // employee.setContracts(contracts);
    // }

    // Employee saved = employeeRepository.save(employee);
    // logger.info("Employee created with ID: {}", saved.getId());
    // return saved;
    // }

    public Employee create(CreateEmployeeDTO dto) {
        logger.debug("Creating a new employee with data: {}", dto);

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setResidentialAddress(dto.getResidentialAddress());
        employee.setEmployeeStatus(dto.getEmployeeStatus());
        employee.setCreatedAt(dto.getCreatedAt());
        employee.setUpdatedAt(dto.getUpdatedAt());
        employee.setPhotoUrl(dto.getPhotoUrl());

        Employee savedEmployee = employeeRepository.save(employee);

        List<ContractCreateDTO> contractDtos = dto.getContracts();
        if (contractDtos != null && !contractDtos.isEmpty()) {
            List<Contract> contracts = contractDtos.stream().map(contractDto -> {
                Contract contract = new Contract();
                contract.setContractType(contractDto.getContractType());
                contract.setContractTerm(contractDto.getContractTerm());
                contract.setStartDate(contractDto.getStartDate());
                contract.setFinishDate(contractDto.getFinishDate());
                // contract.setOngoing(contractDto.isOngoing());
                contract.setWorkType(contractDto.getWorkType());
                contract.setHoursPerWeek(contractDto.getHoursPerWeek());

                contract.setEmployee(savedEmployee);

                return contract;
            }).toList();

            contracts = contractRepository.saveAll(contracts);

            savedEmployee.setContracts(contracts);
        }

        logger.info("Employee created with ID: {}", savedEmployee.getId());
        return savedEmployee;
    }

    public List<Employee> findAll() {
        logger.debug("Fetching all employees");
        return this.employeeRepository.findAll();
    }

    public Optional<Employee> findById(int id) {
        logger.debug("Fetching employee with ID: {}", id);
        return this.employeeRepository.findById(id);
    }

    // Archive first and then delete
    // @Transactional
    // public void archiveAndDeleteEmployee(int employeeId) {

    // logger.info("Archiving employee with id: {}", employeeId);

    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> {
    // logger.warn("Employee with ID {} not found for archiving", employeeId);
    // return new NotFoundException("Employee not found");
    // });

    // List<Contract> contracts = contractRepository.findByEmployeeId(employeeId);
    // List<ContractArchive> archivedContracts = contracts.stream()
    // .map(contractArchiveMapper::toArchive)
    // .collect(Collectors.toList());
    // contractArchiveRepository.saveAll(archivedContracts);
    // logger.debug("Archived ongoing and inactive contracts {}, for Employee id
    // {}", contracts.size(),
    // employeeId);

    // EmployeeArchive archivedEmployee = employeeArchiveMapper.toArchive(employee);
    // employeeArchiveRepository.save(archivedEmployee);
    // logger.debug("Archived employee with id: {}", archivedEmployee.getId());

    // contractRepository.deleteAll(contracts);
    // employeeRepository.delete(employee);
    // logger.debug("Deleted employee with id: {}", employeeId);
    // }

    @Transactional
    public void archiveAndDeleteEmployee(int employeeId) {
        logger.info("Archiving employee with id: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.warn("Employee with ID {} not found for archiving", employeeId);
                    return new NotFoundException("Employee not found");
                });

        // Archive employee first - create and save archivedEmployee before contracts
        EmployeeArchive archivedEmployee = employeeArchiveMapper.toArchive(employee);
        employeeArchiveRepository.save(archivedEmployee);
        logger.debug("Archived employee with id: {}", archivedEmployee.getId());

        // Archive contracts, linking to archived employee
        List<Contract> contracts = contractRepository.findByEmployeeId(employeeId);
        List<ContractArchive> archivedContracts = contracts.stream()
                .map(contract -> contractArchiveMapper.toArchive(contract, archivedEmployee))
                .collect(Collectors.toList());
        contractArchiveRepository.saveAll(archivedContracts);
        logger.debug("Archived {} contracts for Employee id {}", contracts.size(), employeeId);

        // Delete original contracts and employee
        contractRepository.deleteAll(contracts);
        employeeRepository.delete(employee);
        logger.debug("Deleted employee with id: {}", employeeId);
    }

    public boolean deleteById(int id) {
        logger.info("Deleting employee with id: {}", id);
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            logger.warn("Employee with id: {} not found", id);
            return false;
        }
        this.employeeRepository.delete(foundEmployee.get());
        logger.info("Deleted employee with id: {}", id);
        return true;
    }

    // public Optional<Employee> updateById(int id, UpdateEmployeeDTO data) {
    // logger.info("Updating employee with id: {}", id);
    // Optional<Employee> foundEmployee = this.findById(id);
    // if (foundEmployee.isEmpty()) {
    // logger.warn("Employee with id: {} not found", id);
    // return foundEmployee;
    // }

    // Employee employeeFromDB = foundEmployee.get();
    // employeeMapper.updateEntityFromDTO(data, employeeFromDB);
    // this.employeeRepository.save(employeeFromDB);
    // logger.info("Updated details for employee with id: {}",
    // employeeFromDB.getId());
    // return Optional.of(employeeFromDB);
    // }

    @Transactional
    public Optional<Employee> updateById(int id, UpdateEmployeeDTO data) {
        logger.info("Updating employee with id: {}", id);
        Optional<Employee> foundEmployee = this.findById(id);

        if (foundEmployee.isEmpty()) {
            logger.warn("Employee with id: {} not found", id);
            return Optional.empty();
        }

        Employee employeeFromDB = foundEmployee.get();

        // Step 1: Update employee's basic fields
        employeeMapper.updateEntityFromDTO(data, employeeFromDB);

        // Step 2: Handle contracts if provided
        if (data.getContracts() != null) {
            List<Contract> updatedContracts = data.getContracts().stream().map(contractDTO -> {
                Contract contract;

                // Update existing contract if ID is present
                if (contractDTO.getId() != 0) {
                    contract = contractRepository.findById(contractDTO.getId())
                            .orElseGet(Contract::new); // fallback to new if not found
                } else {
                    contract = new Contract();
                }

                contract.setContractType(contractDTO.getContractType());
                contract.setContractTerm(contractDTO.getContractTerm());
                contract.setStartDate(contractDTO.getStartDate());
                contract.setFinishDate(contractDTO.getFinishDate());
                // contract.setOngoing(contractDTO.isOngoing());
                contract.setWorkType(contractDTO.getWorkType());
                contract.setHoursPerWeek(contractDTO.getHoursPerWeek());
                contract.setUpdatedAt(contractDTO.getUpdatedAt());
                contract.setEmployee(employeeFromDB); // IMPORTANT: link contract to employee

                return contract;
            }).toList();

            // Optional: clear old contracts if necessary
            employeeFromDB.getContracts().clear();
            employeeFromDB.getContracts().addAll(updatedContracts);
        }

        // Step 3: Save employee (cascade may save contracts depending on mapping)
        this.employeeRepository.save(employeeFromDB);

        logger.info("Updated details for employee with id: {}", employeeFromDB.getId());
        return Optional.of(employeeFromDB);
    }

    public Optional<Employee> replaceById(int id, UpdateEmployeeDTO data) {
        logger.info("Replace details for employee with id: {}", id);
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            logger.warn("During update, employee with id: {}, not found", id);
            return Optional.empty();
        }

        Employee employeeFromDB = foundEmployee.get();
        logger.info("Found record for employee with id: {}", id);
        employeeMapper.updateEntityFromDTO(data, employeeFromDB);
        this.employeeRepository.save(employeeFromDB);
        logger.debug("Saved updated details for employee with ID: {}", id);
        return Optional.of(employeeFromDB);
    }

    public List<Employee> getAllEmployeesWithContracts() {
        logger.debug("Getting details for all employees with contracts");

        List<Employee> employees = employeeRepository.findAllWithContracts();

        if (employees.isEmpty()) {
            logger.warn("No employees found with contracts");
            return List.of();
        }

        logger.debug("Found {} employees with contracts", employees.size());

        return employees;
    }

}
