package com.example.employee.employeearchive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeArchiveRepository extends JpaRepository<EmployeeArchive, Integer> {
    // You can add custom query methods here if needed
}
