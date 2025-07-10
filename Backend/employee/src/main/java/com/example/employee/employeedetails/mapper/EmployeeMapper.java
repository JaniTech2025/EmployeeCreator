package com.example.employee.employeedetails.mapper;

import com.example.employee.contract.Contract;
import com.example.employee.contract.dto.ContractDTO;
import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.dto.CreateEmployeeDTO;
import com.example.employee.employeedetails.dto.EmployeeWithContractsDTO;
import com.example.employee.employeedetails.dto.UpdateEmployeeDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Map CreateEmployeeDTO to Employee entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photoUrl", source = "photoUrl")
    Employee toEntity(CreateEmployeeDTO dto);

    @Mapping(target = "photoUrl", source = "photoUrl")
    void updateEntityFromDTO(UpdateEmployeeDTO dto, @MappingTarget Employee employee);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "photoUrl", source = "photoUrl")
    @Mapping(target = "contracts", expression = "java(mapContracts(employee.getContracts()))")
    EmployeeWithContractsDTO toDTO(Employee employee);

    default List<ContractDTO> mapContracts(List<Contract> contracts) {
        if (contracts == null || contracts.isEmpty()) {
            return List.of();
        }

        LocalDate today = LocalDate.now();

        return contracts.stream()
                .filter(contract -> {
                    LocalDate start = contract.getStartDate();
                    LocalDate end = contract.isOngoing() ? LocalDate.MAX : contract.getFinishDate();
                    return (start == null || !today.isBefore(start)) &&
                            (end == null || !today.isAfter(end));
                })
                .findFirst() // there will only be one active contract
                .map(contract -> {
                    ContractDTO dto = new ContractDTO();
                    dto.setId(contract.getId());
                    dto.setContractType(contract.getContractType());
                    dto.setContractTerm(contract.getContractTerm());
                    dto.setStartDate(contract.getStartDate());
                    dto.setFinishDate(contract.getFinishDate());
                    dto.setOngoing(contract.isOngoing());
                    dto.setWorkType(contract.getWorkType());
                    dto.setHoursPerWeek(contract.getHoursPerWeek());
                    dto.setCreatedAt(contract.getCreatedAt());
                    dto.setUpdatedAt(contract.getUpdatedAt());
                    dto.setEmployeeId(contract.getEmployee().getId());
                    return List.of(dto);
                })
                .orElse(List.of()); // no active contract
    }

}
