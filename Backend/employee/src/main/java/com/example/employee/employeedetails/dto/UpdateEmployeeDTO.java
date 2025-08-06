package com.example.employee.employeedetails.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.example.employee.contract.dto.ContractUpdateDTO;

public class UpdateEmployeeDTO {

    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name must not exceed 50 characters")
    private String middleName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email should be a valid format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^(\\+614|04)\\d{8}$", message = "Mobile number must start with +614 or 04 and have 10 digits total")
    private String mobileNumber;

    @Size(max = 255, message = "Residential address must not exceed 255 characters")
    private String residentialAddress;

    private String employeeStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @Pattern(regexp = "^(https?://).*$", message = "Photo URL must be a valid HTTP/HTTPS URL")
    private String photoUrl;

    @Valid
    private List<@Valid ContractUpdateDTO> contracts;

    // Getters and Setters

    public List<ContractUpdateDTO> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractUpdateDTO> contracts) {
        this.contracts = contracts;
    }

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
