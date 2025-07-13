package com.example.employee.employeedetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

import com.example.employee.contract.*;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "employee_details")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String fname;

    @Column
    private String middle_name;

    @Column
    private String last_name;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

}
