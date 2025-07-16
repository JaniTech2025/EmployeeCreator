package com.example.employee.contractarchive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.employee.contract.Contract;

import com.example.employee.contractarchive.ContractArchive;

import com.example.employee.employeearchive.EmployeeArchive;

import jakarta.persistence.*;

@Entity
@Table(name = "contracts_archive")
public class ContractArchive {

    public enum ContractType {
        Temporary,
        Permanent
    }

    public enum WorkType {
        FullTime,
        PartTime
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // @OneToOne
    // @JoinColumn(name = "id")
    // private Contract contract;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private ContractType contractType;

    @Column(name = "contract_term", length = 100)
    private String contractTerm;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(name = "ongoing", nullable = false)
    private boolean ongoing = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_type", nullable = false)
    private WorkType workType;

    @Column(name = "hours_per_week", precision = 4, scale = 1)
    private BigDecimal hoursPerWeek;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "employee_id", nullable = false, insertable = false, updatable = false)
    private int employeeId;

    @Column(name = "archived_at", nullable = false)
    private LocalDateTime archivedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeArchive employee;

    public EmployeeArchive getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeArchive employee) {
        this.employee = employee;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // public Contract getContract() {
    // return contract;
    // }

    // public void setContract(Contract contract) {
    // this.contract = contract;
    // }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
}
