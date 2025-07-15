package com.example.employee.employeearchive;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import com.example.employee.employeearchive.EmployeeArchive;
import com.example.employee.contractarchive.ContractArchive;
import com.example.employee.employeedetails.Employee;

@Entity
@Table(name = "employee_archive")
public class EmployeeArchive {

    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "first_name")
    private String fname;

    @Column
    private String middle_name;

    @Column
    private String last_name;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @Column
    private String email;

    @Column
    private String mobile_number;

    @Column
    private String residential_address;

    @Column
    private String employee_status;

    @Column
    private LocalDate created_at;

    @Column
    private LocalDate updated_at;

    @Column
    private String photoUrl;

    @OneToMany(mappedBy = "employee")
    private List<ContractArchive> contractArchives;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getResidential_address() {
        return residential_address;
    }

    public void setResidential_address(String residential_address) {
        this.residential_address = residential_address;
    }

    public String getEmployee_status() {
        return employee_status;
    }

    public void setEmployee_status(String employee_status) {
        this.employee_status = employee_status;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<ContractArchive> getContractArchives() {
        return contractArchives;
    }

    public void setContractArchives(List<ContractArchive> contractArchives) {
        this.contractArchives = contractArchives;
    }

}
