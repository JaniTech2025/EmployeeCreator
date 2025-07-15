package com.example.employee.employeearchive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Import logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/employees/archive")
public class EmployeeArchiveController {

    private final EmployeeArchivalService employeeArchivalService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeArchiveController.class);

    @Autowired
    public EmployeeArchiveController(EmployeeArchivalService employeeArchivalService) {
        this.employeeArchivalService = employeeArchivalService;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<String> archiveEmployee(@PathVariable int employeeId) {
        logger.debug("Received request to archive employee with ID: {}", employeeId);

        boolean archived = employeeArchivalService.archiveEmployeeById(employeeId);
        if (archived) {
            logger.debug("Employee with id: {} archived successfully", employeeId);
            return ResponseEntity.ok("Employee archived successfully.");
        } else {
            logger.debug("Unable to archive employee with id: {}", employeeId);
            return ResponseEntity.notFound().build();
        }
    }
}
