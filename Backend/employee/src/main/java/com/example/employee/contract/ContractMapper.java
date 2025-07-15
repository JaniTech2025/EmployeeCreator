package com.example.employee.contract;

import com.example.employee.contract.Contract;
import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mapping(target = "employee.id", source = "employeeId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Contract toEntity(ContractCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    Contract toContract(ContractCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Contract toContract(ContractUpdateDTO dto);

    @Mapping(target = "employee.id", source = "employeeId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDTO(ContractUpdateDTO dto, @MappingTarget Contract contract);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeFullName", expression = "java(contract.getEmployee().getFirstName() + \" \" + contract.getEmployee().getLastName())")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ContractResponseDTO toResponseDTO(Contract contract);

}
