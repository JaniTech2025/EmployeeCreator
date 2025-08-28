package com.example.employee.employeedetails;

import com.example.employee.employeedetails.Employee;
import com.example.employee.contract.Contract;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.contracts c ORDER BY e.firstName ASC")
    List<Employee> findAllWithContracts();

}