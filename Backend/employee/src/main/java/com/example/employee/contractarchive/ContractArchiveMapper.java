package com.example.employee.contractarchive;

import java.util.List;

import com.example.employee.contract.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface ContractArchiveMapper {

    ContractArchiveMapper INSTANCE = Mappers.getMapper(ContractArchiveMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())")
    ContractArchive toArchive(Contract contract);

    List<ContractArchive> toArchiveList(List<Contract> contracts);
}
