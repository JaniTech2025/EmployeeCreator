package com.example.employee.contract.dto;

import com.example.employee.contract.Contract.ContractType;
import com.example.employee.contract.Contract.WorkType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContractUpdateDTO {

    private Integer id;

    @JsonProperty("contract_type")
    private ContractType contractType;

    @JsonProperty("contract_term")
    private String contractTerm;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("finish_date")
    private LocalDate finishDate;

    private boolean ongoing;

    @JsonProperty("work_type")
    private WorkType workType;

    @JsonProperty("hours_per_week")
    private BigDecimal hoursPerWeek;

    private int employeeId;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // ✅ Getter & Setter for id (needed in service layer)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
