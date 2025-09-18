package com.example.employee.contract;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.employee.common.exceptions.ServiceValidationException;
import com.example.employee.common.exceptions.ValidationErrors;
import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ContractEndToEndTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractMapper contractMapper;

    @InjectMocks
    private ContractService contractService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Can create employee without a contract (can be added later)
    @Test
    public void createContractForEmployee_ContractTypeNull_StillCreatesContract() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(Contract.ContractType.Permanent);

        Employee mockEmployee = new Employee();
        Contract mockContract = new Contract();
        Contract savedContract = new Contract();
        ContractResponseDTO responseDTO = new ContractResponseDTO();

        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
        when(contractMapper.toEntity(dto)).thenReturn(mockContract);
        when(contractRepository.save(mockContract)).thenReturn(savedContract);
        when(contractMapper.toResponseDTO(savedContract)).thenReturn(responseDTO);

        ContractResponseDTO result = contractService.createContractForEmployee(1, dto);

        assertEquals(responseDTO, result);
        verify(contractRepository).save(mockContract);
    }

    // createContractForEmployee - employee not found
    @Test
    public void createContractForEmployee_EmployeeNotFound_ThrowsIllegalArgumentException() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(Contract.ContractType.Permanent);
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contractService.createContractForEmployee(1, dto));
    }

    // Successfully create contract for employees
    @Test
    public void createContractForEmployee_Success_ReturnsResponseDTO() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(Contract.ContractType.Permanent);
        ;

        Employee mockEmployee = new Employee();
        Contract mockContract = new Contract();
        Contract savedContract = new Contract();
        ContractResponseDTO responseDTO = new ContractResponseDTO();

        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
        when(contractMapper.toEntity(dto)).thenReturn(mockContract);
        when(contractRepository.save(mockContract)).thenReturn(savedContract);
        when(contractMapper.toResponseDTO(savedContract)).thenReturn(responseDTO);

        ContractResponseDTO result = contractService.createContractForEmployee(1, dto);

        assertEquals(responseDTO, result);
        verify(contractRepository).save(mockContract);
    }

    // updateContract - contract not found
    @Test
    public void updateContract_NotFound_ReturnsEmptyOptional() {
        when(contractRepository.findByIdAndEmployeeId(10, 5)).thenReturn(Optional.empty());

        Optional<ContractResponseDTO> result = contractService.updateContract(5, 10, new ContractUpdateDTO());

        assertTrue(result.isEmpty());
    }

    // updateContract - change Temporary to Permanent
    @Test
    public void updateContract_ChangeTemporaryToPermanent_Success() {
        Contract existingContract = new Contract();
        existingContract.setContractType(Contract.ContractType.Temporary);

        ContractUpdateDTO updateDTO = new ContractUpdateDTO();
        updateDTO.setContractType(Contract.ContractType.Permanent);

        Contract updatedContract = new Contract();
        updatedContract.setContractType(Contract.ContractType.Permanent);

        ContractResponseDTO responseDTO = new ContractResponseDTO();
        responseDTO.setContractType(Contract.ContractType.Permanent);

        when(contractRepository.findByIdAndEmployeeId(10, 5)).thenReturn(Optional.of(existingContract));
        doAnswer(invocation -> {
            ContractUpdateDTO dto = invocation.getArgument(0);
            Contract target = invocation.getArgument(1);
            target.setContractType(dto.getContractType());
            return null;
        }).when(contractMapper).updateFromDTO(updateDTO, existingContract);

        when(contractRepository.save(existingContract)).thenReturn(updatedContract);
        when(contractMapper.toResponseDTO(updatedContract)).thenReturn(responseDTO);

        Optional<ContractResponseDTO> result = contractService.updateContract(5, 10, updateDTO);

        assertTrue(result.isPresent());
        assertEquals(Contract.ContractType.Permanent, result.get().getContractType());
        verify(contractRepository).save(existingContract);
    }

    // deleteContract - not found
    @Test
    public void deleteContract_NotFound_ReturnsFalse() {
        when(contractRepository.findByIdAndEmployeeId(10, 5)).thenReturn(Optional.empty());

        boolean result = contractService.deleteContract(5, 10);

        assertFalse(result);
    }

    // deleteContract - found and deleted
    @Test
    public void deleteContract_Found_ReturnsTrue() {
        Contract contract = new Contract();
        when(contractRepository.findByIdAndEmployeeId(10, 5)).thenReturn(Optional.of(contract));

        boolean result = contractService.deleteContract(5, 10);

        assertTrue(result);
        verify(contractRepository).delete(contract);
    }

    // fetch all contracts - returns mapped contracts
    @Test
    public void findAll_ReturnsMappedContracts() {
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        ContractResponseDTO dto1 = new ContractResponseDTO();
        ContractResponseDTO dto2 = new ContractResponseDTO();

        when(contractRepository.findAll()).thenReturn(Arrays.asList(contract1, contract2));
        when(contractMapper.toResponseDTO(contract1)).thenReturn(dto1);
        when(contractMapper.toResponseDTO(contract2)).thenReturn(dto2);

        List<ContractResponseDTO> results = contractService.findAll();

        assertEquals(2, results.size());
        assertTrue(results.contains(dto1));
        assertTrue(results.contains(dto2));
    }
}
