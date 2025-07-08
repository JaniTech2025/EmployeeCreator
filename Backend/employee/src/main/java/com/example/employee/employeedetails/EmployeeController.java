package com.example.employee.employeedetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import com.example.employee.common.exceptions.NotFoundException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody CreateEmployeeDTO data) {
        Employee saved = this.employeeService.create(data);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> allEmployees = this.employeeService.findAll();
        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) throws NotFoundException {
        Optional<Employee> foundEmployee = this.employeeService.findById(id);
        if (foundEmployee.isPresent()) {
            return new ResponseEntity<>(foundEmployee.get(), HttpStatus.OK);
        }
        throw new NotFoundException("Employee with id " + id + " does not exist");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws NotFoundException {
        boolean deleted = this.employeeService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        throw new NotFoundException("Employee with id " + id + " does not exist");
    }

    // PUT for full update - expects full employee data in DTO
    @PutMapping("/{id}")
    public ResponseEntity<Employee> replaceEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeDTO data) throws NotFoundException {

        Optional<Employee> updated = this.employeeService.updateById(id, data);
        Employee employee = updated
                .orElseThrow(() -> new NotFoundException("Employee with id " + id + " does not exist"));

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    // PATCH for partial update - expects partial fields in DTO
    @PatchMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeDTO data) throws NotFoundException {

        Optional<Employee> updated = this.employeeService.updateById(id, data);
        Employee employee = updated
                .orElseThrow(() -> new NotFoundException("Employee with id " + id + " does not exist"));

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
