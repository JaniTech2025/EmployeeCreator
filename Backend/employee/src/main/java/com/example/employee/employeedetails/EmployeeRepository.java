package com.example.employee.employeedetails;

import com.example.employee.employeedetails.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.contracts")
    List<Employee> findAllWithContracts();

}
