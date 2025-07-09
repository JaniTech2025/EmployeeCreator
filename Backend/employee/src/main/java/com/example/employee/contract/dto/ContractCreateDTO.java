package com.example.employee.contract.dto;

import com.example.employee.contract.Contract.ContractType;
import com.example.employee.contract.Contract.WorkType;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class ContractCreateDTO {

    @NotNull
    private ContractType contractType;

    @Size(max = 100)
    private String contractTerm;

    @NotNull
    private LocalDate startDate;

    private LocalDate finishDate;

    private boolean ongoing;

    @NotNull
    private WorkType workType;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal hoursPerWeek;

    @Min(1)
    private int employeeId;

    // Getters and Setters
    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public String getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public BigDecimal getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(BigDecimal hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
