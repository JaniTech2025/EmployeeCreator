package com.example.employee.employeearchive;

import com.example.employee.employeedetails.Employee;
import com.example.employee.contractarchive.ContractArchiveMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ContractArchiveMapper.class })
public interface EmployeeArchiveMapper {

    @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())")
    EmployeeArchive toArchive(Employee employee);
}
