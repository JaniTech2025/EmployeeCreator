package com.example.employee.employeedetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employee.employeedetails.dto.CreateEmployeeDTO;
import com.example.employee.employeedetails.dto.UpdateEmployeeDTO;

import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import com.example.employee.common.exceptions.NotFoundException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody CreateEmployeeDTO data) {
        logger.debug("Received request to create new Employee");
        Employee saved = this.employeeService.create(data);
        logger.debug("Created new Employee: {}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> employees = employeeService.getAllEmployeesWithContracts();
        logger.debug("Fetched {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable int id) throws NotFoundException {
        logger.debug("Fetching employee with ID: {}", id);
        Optional<Employee> foundEmployee = this.employeeService.findById(id);
        if (foundEmployee.isPresent()) {
            logger.info("Found employee with ID: {}", id);
            return new ResponseEntity<>(foundEmployee.get(), HttpStatus.OK);
        }
        logger.warn("Employee with ID: {} not found", id);
        throw new NotFoundException("Employee with id " + id + " does not exist");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteById(@PathVariable int id) throws
    // NotFoundException {
    // boolean deleted = this.employeeService.deleteById(id);
    // if (deleted) {
    // return ResponseEntity.noContent().build();
    // }
    // throw new NotFoundException("Employee with id " + id + " does not exist");
    // }

    // Archive first, delete later
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> archiveAndDeleteEmployee(@PathVariable int id) {
        logger.debug("Archiving and deleting employee with ID: {}", id);
        employeeService.archiveAndDeleteEmployee(id);
        logger.info("Archived and deleted employee with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // PUT for full update - expects full employee data in DTO
    @PutMapping("/{id}")
    public ResponseEntity<Employee> replaceEmployee(
            @PathVariable int id,
            @Valid @RequestBody UpdateEmployeeDTO data) throws NotFoundException {

        logger.debug("Replacing employee with ID: {}", id);

        Optional<Employee> updated = this.employeeService.updateById(id, data);
        Employee employee = updated
                .orElseThrow(() -> {
                    logger.warn("Replace failed. Employee with ID: {} not found", id);
                    return new NotFoundException("Employee with id " + id + " does not exist");
                });

        logger.info("Replaced employee with ID: {}", id);

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    // PATCH for partial update - expects partial fields in DTO
    @PatchMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody UpdateEmployeeDTO data) throws NotFoundException {

        logger.debug("Updating employee with ID: {}", id);

        Optional<Employee> updated = this.employeeService.updateById(id, data);
        Employee employee = updated
                .orElseThrow(() -> {
                    logger.warn("Employee with id: {} not found", id);
                    return new NotFoundException("Employee with id " + id + " does not exist");
                });

        logger.info("Updated employee with ID: {}", id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
