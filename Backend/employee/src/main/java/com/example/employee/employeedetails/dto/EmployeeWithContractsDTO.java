package com.example.employee.employeedetails.dto;

import com.example.employee.contract.dto.ContractDTO;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

public class EmployeeWithContractsDTO {

    @Min(value = 1, message = "ID must be positive")
    private int id;

    @NotBlank(message = "First name must not be blank")
    @Size(max = 50, message = "First name can be at most 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name can be at most 50 characters")
    private String middleName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 50, message = "Last name can be at most 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email can be at most 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "Mobile number must be 8 to 15 digits")
    private String mobileNumber;

    @Size(max = 255, message = "Residential address can be at most 255 characters")
    private String residentialAddress;

    @NotBlank(message = "Employee status must not be blank")
    private String employeeStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @Pattern(regexp = "^(https?://).*$", message = "Photo URL must be a valid HTTP/HTTPS URL")
    private String photoUrl;

    @Valid
    private List<ContractDTO> contracts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
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

    public List<ContractDTO> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractDTO> contracts) {
        this.contracts = contracts;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
