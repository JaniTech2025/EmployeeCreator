package com.example.employee.employeedetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.employee.contract.dto.ContractDTO;
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

    public Employee create(CreateEmployeeDTO data) {
        Employee newEmployee = employeeMapper.toEntity(data);
        return this.employeeRepository.save(newEmployee);
    }

    public List<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        return this.employeeRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            return false;
        }
        this.employeeRepository.delete(foundEmployee.get());
        return true;
    }

    public Optional<Employee> updateById(Long id, UpdateEmployeeDTO data) {
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            return foundEmployee;
        }

        Employee employeeFromDB = foundEmployee.get();
        employeeMapper.updateEntityFromDTO(data, employeeFromDB);
        this.employeeRepository.save(employeeFromDB);
        return Optional.of(employeeFromDB);
    }

    public Optional<Employee> replaceById(Long id, UpdateEmployeeDTO data) {
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
