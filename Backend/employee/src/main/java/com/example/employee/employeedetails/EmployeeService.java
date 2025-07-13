package com.example.employee.employeedetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

import com.example.employee.contract.Contract;
import com.example.employee.contract.dto.ContractDTO;
import com.example.employee.contract.dto.ContractCreateDTO;
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

    public Employee create(CreateEmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setFname(dto.getFname());
        employee.setLast_name(dto.getLast_name());
        employee.setEmail(dto.getEmail());
        employee.setMobile_number(dto.getMobile_number());
        employee.setResidential_address(dto.getResidential_address());
        employee.setEmployee_status(dto.getEmployee_status());
        employee.setCreated_at(dto.getCreated_at());
        employee.setUpdated_at(dto.getUpdated_at());
        employee.setPhotoUrl(dto.getphotoUrl());

        List<ContractCreateDTO> contractDtos = dto.getContracts();
        if (contractDtos != null && !contractDtos.isEmpty()) {
            List<Contract> contracts = contractDtos.stream().map(contractDto -> {
                Contract contract = new Contract();
                contract.setContractType(contractDto.getContractType());
                contract.setContractTerm(contractDto.getContractTerm());
                contract.setStartDate(contractDto.getStartDate());
                contract.setFinishDate(contractDto.getFinishDate());
                contract.setOngoing(contractDto.isOngoing());
                contract.setWorkType(contractDto.getWorkType());
                contract.setHoursPerWeek(contractDto.getHoursPerWeek());
                contract.setCreatedAt(contractDto.getCreatedAt());
                contract.setUpdatedAt(contractDto.getUpdatedAt());
                contract.setEmployee(employee);
                return contract;
            }).toList();

            employee.setContracts(contracts);
        }

        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    public Optional<Employee> findById(int id) {
        return this.employeeRepository.findById(id);
    }

    // Archive first, delete later
    @Transactional
    public void archiveAndDeleteEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        List<Contract> contracts = contractRepository.findByEmployeeId(employeeId);
        List<ContractArchive> archivedContracts = contracts.stream()
                .map(contractArchiveMapper::toArchive)
                .collect(Collectors.toList());
        contractArchiveRepository.saveAll(archivedContracts);

        EmployeeArchive archivedEmployee = employeeArchiveMapper.toArchive(employee);
        employeeArchiveRepository.save(archivedEmployee);

        contractRepository.deleteAll(contracts);
        employeeRepository.delete(employee);
    }

    public boolean deleteById(int id) {
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            return false;
        }
        this.employeeRepository.delete(foundEmployee.get());
        return true;
    }

    public Optional<Employee> updateById(int id, UpdateEmployeeDTO data) {
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            return foundEmployee;
        }

        Employee employeeFromDB = foundEmployee.get();
        employeeMapper.updateEntityFromDTO(data, employeeFromDB);
        this.employeeRepository.save(employeeFromDB);
        return Optional.of(employeeFromDB);
    }

    public Optional<Employee> replaceById(int id, UpdateEmployeeDTO data) {
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            return Optional.empty();
        }

        Employee employeeFromDB = foundEmployee.get();
        employeeMapper.updateEntityFromDTO(data, employeeFromDB);
        this.employeeRepository.save(employeeFromDB);
        return Optional.of(employeeFromDB);
    }

    public List<EmployeeWithContractsDTO> getAllEmployeesWithContracts() {
        List<Employee> employees = employeeRepository.findAllWithContracts();
        return employees.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

}
