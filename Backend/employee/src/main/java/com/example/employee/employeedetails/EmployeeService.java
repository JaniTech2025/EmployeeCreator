package com.example.employee.employeedetails;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public Employee create(CreateEmployeeDTO data) {
        Employee newEmployee = modelMapper.map(data, Employee.class);
        Employee savedEmployee = this.employeeRepository.save(newEmployee);
        return savedEmployee;
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
        Employee employeeFromDb = foundEmployee.get();
        this.employeeRepository.delete(employeeFromDb);
        return true;
    }

    public Optional<Employee> updateById(Long id, UpdateEmployeeDTO data) {
        Optional<Employee> foundEmployee = this.findById(id);

        if (foundEmployee.isEmpty()) {
            return foundEmployee;
        }

        Employee employeeFromDB = foundEmployee.get();

        this.modelMapper.map(data, employeeFromDB);
        this.employeeRepository.save(employeeFromDB);
        return Optional.of(employeeFromDB);
    }

    public Optional<Employee> replaceById(Long id, UpdateEmployeeDTO data) {
        Optional<Employee> foundEmployee = this.findById(id);
        if (foundEmployee.isEmpty()) {
            return Optional.empty();
        }

        Employee employeeFromDB = foundEmployee.get();

        modelMapper.map(data, employeeFromDB);

        employeeRepository.save(employeeFromDB);
        return Optional.of(employeeFromDB);
    }

}