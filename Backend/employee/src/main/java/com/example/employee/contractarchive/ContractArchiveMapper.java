package com.example.employee.contractarchive;

import java.util.List;

import com.example.employee.contract.Contract;
import com.example.employee.employeearchive.EmployeeArchive;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContractArchiveMapper {

    ContractArchiveMapper INSTANCE = Mappers.getMapper(ContractArchiveMapper.class);

    @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "employeeArchive", target = "employee")
    @Mapping(target = "id", ignore = true) // Let DB generate ID
    @Mapping(target = "contract", ignore = true) // 'contract' not meaningful, so ignore
    ContractArchive toArchive(Contract contract, EmployeeArchive employeeArchive);

    @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contract", ignore = true)
    ContractArchive toArchive(Contract contract);

    List<ContractArchive> toArchiveList(List<Contract> contracts);
}
