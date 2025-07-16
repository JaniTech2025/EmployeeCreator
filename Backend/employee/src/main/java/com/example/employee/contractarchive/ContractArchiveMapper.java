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

    @Mapping(target = "id", ignore = true)
    // @Mapping(target = "contract", source = "contract")
    @Mapping(target = "contractType", source = "contract.contractType")
    @Mapping(target = "contractTerm", source = "contract.contractTerm")
    @Mapping(target = "startDate", source = "contract.startDate")
    @Mapping(target = "finishDate", source = "contract.finishDate")
    @Mapping(target = "ongoing", source = "contract.ongoing")
    @Mapping(target = "workType", source = "contract.workType")
    @Mapping(target = "hoursPerWeek", source = "contract.hoursPerWeek")
    @Mapping(target = "createdAt", source = "contract.createdAt")
    @Mapping(target = "updatedAt", source = "contract.updatedAt")
    @Mapping(target = "employee", source = "employeeArchive")
    @Mapping(target = "employeeId", expression = "java(employeeArchive.getId())")
    @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())")
    ContractArchive toArchive(Contract contract, EmployeeArchive employeeArchive);

    @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "id", ignore = true)
    // @Mapping(target = "contract", ignore = true)
    ContractArchive toArchive(Contract contract);

    List<ContractArchive> toArchiveList(List<Contract> contracts);

}
