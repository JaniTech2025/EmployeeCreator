package com.example.employee.employeedetails.dto;

import jakarta.validation.constraints.*;
import java.util.List;

import java.time.LocalDate;
import com.example.employee.contract.dto.ContractUpdateDTO;

public class UpdateEmployeeDTO {

    private String fname;

    private String middle_name;

    private String last_name;

    @Email(message = "Email should be valid")
    private String email;

    private String mobile_number;

    private String residential_address;

    private String employee_status;

    private LocalDate created_at;

    private LocalDate updated_at;

    private String photoUrl;

    private List<ContractUpdateDTO> contracts;

    public List<ContractUpdateDTO> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractUpdateDTO> contracts) {
        this.contracts = contracts;
    }

    // Getters and Setters

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

    public String getphotoUrl() {
        return photoUrl;
    }

    public void setphotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
