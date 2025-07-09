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

    @Mapping(target = "employee.id", source = "employeeId")
    void updateFromDTO(ContractUpdateDTO dto, @MappingTarget Contract contract);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeFullName", expression = "java(contract.getEmployee().getFname() + \" \" + contract.getEmployee().getLast_name())")
    ContractResponseDTO toResponseDTO(Contract contract);
}
