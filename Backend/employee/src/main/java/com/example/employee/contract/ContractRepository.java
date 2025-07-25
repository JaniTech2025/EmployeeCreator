package com.example.employee.contract;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findByEmployeeId(int employeeId);

    Optional<Contract> findByIdAndEmployeeId(int contractId, int employeeId);
}
