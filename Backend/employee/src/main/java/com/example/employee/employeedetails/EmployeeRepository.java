package com.example.employee.employeedetails;

import com.example.employee.employeedetails.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.contracts ORDER BY e.firstName ASC, e.lastName ASC")
    List<Employee> findAllWithContracts();

}
