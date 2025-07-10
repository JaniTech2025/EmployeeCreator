package com.example.employee.employeearchive;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;
import com.example.employee.contract.Contract;
import com.example.employee.contract.ContractRepository;
import com.example.employee.contractarchive.ContractArchive;
import com.example.employee.contractarchive.ContractArchiveRepository;
import com.example.employee.contractarchive.ContractArchiveMapper;
import com.example.employee.employeearchive.EmployeeArchive;
import com.example.employee.employeearchive.EmployeeArchiveRepository;
import com.example.employee.employeearchive.EmployeeArchiveMapper;

import java.util.List;

@Service
public class EmployeeArchivalService {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final ContractArchiveRepository contractArchiveRepository;
    private final EmployeeArchiveRepository employeeArchiveRepository;
    private final ContractArchiveMapper contractArchiveMapper;
    private final EmployeeArchiveMapper employeeArchiveMapper;

    public EmployeeArchivalService(
            EmployeeRepository employeeRepository,
            ContractRepository contractRepository,
            ContractArchiveRepository contractArchiveRepository,
            EmployeeArchiveRepository employeeArchiveRepository,
            ContractArchiveMapper contractArchiveMapper,
            EmployeeArchiveMapper employeeArchiveMapper) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.contractArchiveRepository = contractArchiveRepository;
        this.employeeArchiveRepository = employeeArchiveRepository;
        this.contractArchiveMapper = contractArchiveMapper;
        this.employeeArchiveMapper = employeeArchiveMapper;
    }

    @Transactional
    public boolean archiveEmployeeById(Long employeeId) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

            EmployeeArchive employeeArchive = employeeArchiveMapper.toArchive(employee);
            employeeArchiveRepository.save(employeeArchive);

            List<Contract> contracts = contractRepository.findByEmployeeId(employeeId);
            contracts.forEach(contract -> {
                ContractArchive contractArchive = contractArchiveMapper.toArchive(contract);
                contractArchiveRepository.save(contractArchive);
            });

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
