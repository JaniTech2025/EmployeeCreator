package com.example.employee.employeearchive;

import com.example.employee.employeedetails.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeArchiveMapper {

    EmployeeArchiveMapper INSTANCE = Mappers.getMapper(EmployeeArchiveMapper.class);

    @Mappings({
            @Mapping(target = "fname", source = "firstName"),
            @Mapping(target = "middle_name", source = "middleName"),
            @Mapping(target = "last_name", source = "lastName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "mobile_number", source = "mobileNumber"),
            @Mapping(target = "residential_address", source = "residentialAddress"),
            @Mapping(target = "employee_status", source = "employeeStatus"),
            @Mapping(target = "created_at", source = "createdAt"),
            @Mapping(target = "updated_at", source = "updatedAt"),
            @Mapping(target = "photoUrl", source = "photoUrl"),
            @Mapping(target = "archivedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "contractArchives", ignore = true),
            @Mapping(target = "employee", ignore = true)
    })
    EmployeeArchive toArchive(Employee employee);
}
