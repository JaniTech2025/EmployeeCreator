package com.example.employee.contract;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.employee.employeedetails.Employee;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

import java.time.Period;

@Entity
@Table(name = "contracts")
public class Contract {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    // @JsonProperty("contract_type")
    private ContractType contractType;

    // @Column(name = "contract_term", length = 100)
    // @JsonProperty("contract_term")
    private String contractTerm;

    @Transient
    private boolean ongoing;

    @Transient
    // @JsonProperty("contract_term")
    public String getContractTerm() {
        if (startDate == null) {
            return "";
        }
        if (finishDate == null) {
            return "Ongoing";
        }

        Period period = Period.between(startDate, finishDate);
        int years = period.getYears();
        int months = period.getMonths();

        if (years > 0) {
            return years + (years == 1 ? " year" : " years") +
                    (months > 0 ? " " + months + (months == 1 ? " month" : " months") : "");
        } else if (months > 0) {
            return months + (months == 1 ? " month" : " months");
        } else {
            return "Less than a month";
        }
    }

    @Column(name = "start_date")
    // @JsonProperty("start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    // @JsonProperty("finish_date")
    private LocalDate finishDate;

    // @Column(name = "ongoing", nullable = false)
    // @JsonProperty("ongoing")
    // private boolean ongoing = false;

    // @Transient
    // @JsonGetter("ongoing")
    // public boolean isOngoing() {
    // LocalDate today = LocalDate.now();
    // return (finishDate == null || !today.isAfter(finishDate)) &&
    // !today.isBefore(startDate);
    // }

    @Enumerated(EnumType.STRING)
    @Column(name = "work_type", nullable = false)
    // @JsonProperty("work_type")
    private WorkType workType;

    @Column(name = "hours_per_week", precision = 4, scale = 1)
    // @JsonProperty("hours_per_week")
    private BigDecimal hoursPerWeek;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    // @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    // @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    // Getters and Setters
    // (You can leave these as-is; Jackson uses fields with @JsonProperty for
    // naming)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    // public String getContractTerm() {
    // return contractTerm;
    // }

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

    // public boolean isOngoing() {
    // return ongoing;
    // }

    // public void setOngoing(boolean ongoing) {
    // this.ongoing = ongoing;
    // }

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isOngoing() {
        LocalDate today = LocalDate.now();

        // Current date is on or after start date & on or before finishdate
        if (today.isBefore(startDate) ||
                ((finishDate != null) && today.isAfter(finishDate)))
            return false;

        return true;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }
}
