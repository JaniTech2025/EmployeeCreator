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
import com.example.employee.common.exceptions.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EmployeeArchivalService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeArchivalService.class);

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
    public boolean archiveEmployeeById(int employeeId) {
        logger.debug("Archiving employee by id: {}", employeeId);
        try {

            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> {
                        logger.warn("Employee with id: {} not found", employeeId);
                        return new NotFoundException("Employee not found with id: " + employeeId);
                    });

            EmployeeArchive employeeArchive = employeeArchiveMapper.toArchive(employee);
            employeeArchive.setId(employee.getId());
            employeeArchive.setEmployee(employee);
            employeeArchiveRepository.save(employeeArchive);

            List<Contract> contracts = contractRepository.findByEmployeeId(employeeId);
            for (Contract contract : contracts) {
                ContractArchive contractArchive = contractArchiveMapper.toArchive(contract);
                contractArchive.setId(contract.getId());
                contractArchive.setContract(contract);
                contractArchive.setEmployee(employeeArchive);
                contractArchiveRepository.save(contractArchive);
            }

            logger.info("Successfully archived employee {} and {} contracts", employeeId, contracts.size());
            return true;
        } catch (Exception e) {
            logger.warn("Unable to archive employee record for id: {}", employeeId, e);
            return false;
        }
    }
}
