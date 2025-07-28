package com.example.employee.employeedetails.dto;

import jakarta.validation.constraints.*;
import java.util.List;

import java.time.LocalDate;
import com.example.employee.contract.dto.ContractUpdateDTO;

public class UpdateEmployeeDTO {

    private String firstName;

    private String middleName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String mobileNumber;

    private String residentialAddress;

    private String employeeStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String photoUrl;

    private List<ContractUpdateDTO> contracts;

    public List<ContractUpdateDTO> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractUpdateDTO> contracts) {
        this.contracts = contracts;
    }

    // Getters and Setters

    public String getFirstName() {
        return firstName;
    }

    public void setFname(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getphotoUrl() {
        return photoUrl;
    }

    public void setphotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}