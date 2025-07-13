package com.example.employee.employeedetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.employee.contract.Contract;
import com.example.employee.contract.dto.ContractDTO;
import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.employeedetails.dto.CreateEmployeeDTO;
import com.example.employee.employeedetails.dto.EmployeeWithContractsDTO;
import com.example.employee.employeedetails.dto.UpdateEmployeeDTO;
import com.example.employee.employeedetails.mapper.EmployeeMapper;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public Employee create(CreateEmployeeDTO dto) {
        Employee employee = new Employee();
        // Set employee fields...
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
