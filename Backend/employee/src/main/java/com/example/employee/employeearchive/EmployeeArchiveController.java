package com.example.employee.employeearchive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees/archive")
public class EmployeeArchiveController {

    private final EmployeeArchivalService employeeArchivalService;

    @Autowired
    public EmployeeArchiveController(EmployeeArchivalService employeeArchivalService) {
        this.employeeArchivalService = employeeArchivalService;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<String> archiveEmployee(@PathVariable Long employeeId) {
        boolean archived = employeeArchivalService.archiveEmployeeById(employeeId);
        if (archived) {
            return ResponseEntity.ok("Employee archived successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
