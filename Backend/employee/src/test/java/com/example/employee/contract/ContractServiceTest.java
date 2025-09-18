package com.example.employee.contract;

import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contract.dto.ContractUpdateDTO;
import com.example.employee.contract.dto.ContractResponseDTO;
import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;
import com.example.employee.common.exceptions.ServiceValidationException;
import com.example.employee.common.exceptions.ValidationErrors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractMapper contractMapper;

    @Spy
    @InjectMocks
    private ContractService contractService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getContractsByEmployeeId_ReturnsSortedContracts() {
        Contract c1 = new Contract();
        c1.setId(1);
        c1.setStartDate(LocalDate.of(2023, 1, 1));
        Contract c2 = new Contract();
        c2.setId(2);
        c2.setStartDate(LocalDate.of(2024, 1, 1));

        ContractResponseDTO dto1 = new ContractResponseDTO();
        dto1.setId(1);
        dto1.setStartDate(LocalDate.of(2023, 1, 1));
        ContractResponseDTO dto2 = new ContractResponseDTO();
        dto2.setId(2);
        dto2.setStartDate(LocalDate.of(2024, 1, 1));

        when(contractRepository.findByEmployeeId(5)).thenReturn(List.of(c1, c2));
        when(contractMapper.toResponseDTO(c1)).thenReturn(dto1);
        when(contractMapper.toResponseDTO(c2)).thenReturn(dto2);

        List<ContractResponseDTO> result = contractService.getContractsByEmployeeId(5);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId()); // newest first
        verify(contractRepository).findByEmployeeId(5);
    }

    @Test
    void createContractForEmployee_WhenContractTypeMissing_ThrowsValidationException() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(null);

        assertThrows(ServiceValidationException.class,
                () -> contractService.createContractForEmployee(1, dto));
    }

    @Test
    void createContractForEmployee_SavesContract() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(Contract.ContractType.Permanent);

        Employee employee = new Employee();
        employee.setId(1);
        Contract contract = new Contract();
        contract.setId(10);
        contract.setEmployee(employee);
        ContractResponseDTO responseDTO = new ContractResponseDTO();
        responseDTO.setId(10);

        when(contractMapper.toEntity(dto)).thenReturn(contract);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toResponseDTO(contract)).thenReturn(responseDTO);

        ContractResponseDTO result = contractService.createContractForEmployee(1, dto);

        assertNotNull(result);
        assertEquals(10, result.getId());
        verify(contractRepository).save(contract);
    }

    @Test
    void updateContract_WhenNotFound_ReturnsEmpty() {
        ContractUpdateDTO dto = new ContractUpdateDTO();
        when(contractRepository.findByIdAndEmployeeId(5, 1)).thenReturn(Optional.empty());

        Optional<ContractResponseDTO> result = contractService.updateContract(1, 5, dto);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateContract_WhenFound_UpdatesAndSaves() {
        Contract contract = new Contract();
        contract.setId(5);
        ContractUpdateDTO dto = new ContractUpdateDTO();
        ContractResponseDTO responseDTO = new ContractResponseDTO();
        responseDTO.setId(5);

        when(contractRepository.findByIdAndEmployeeId(5, 1)).thenReturn(Optional.of(contract));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toResponseDTO(contract)).thenReturn(responseDTO);

        Optional<ContractResponseDTO> result = contractService.updateContract(1, 5, dto);

        assertTrue(result.isPresent());
        verify(contractMapper).updateFromDTO(dto, contract);
        verify(contractRepository).save(contract);
    }

    @Test
    void deleteContract_WhenNotFound_ReturnsFalse() {
        when(contractRepository.findByIdAndEmployeeId(9, 2)).thenReturn(Optional.empty());

        boolean result = contractService.deleteContract(2, 9);
        assertFalse(result);
    }

    @Test
    void deleteContract_WhenFound_DeletesAndReturnsTrue() {
        Contract contract = new Contract();
        contract.setId(9);
        when(contractRepository.findByIdAndEmployeeId(9, 2)).thenReturn(Optional.of(contract));

        boolean result = contractService.deleteContract(2, 9);

        assertTrue(result);
        verify(contractRepository).delete(contract);
    }

    @Test
    void create_WhenContractTypeMissing_ThrowsValidationException() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(null);

        assertThrows(ServiceValidationException.class, () -> contractService.create(dto));
    }

    @Test
    void create_SavesContract() {
        ContractCreateDTO dto = new ContractCreateDTO();
        dto.setContractType(Contract.ContractType.Temporary);

        Contract contract = new Contract();
        contract.setId(42);
        ContractResponseDTO responseDTO = new ContractResponseDTO();
        responseDTO.setId(42);

        when(contractMapper.toEntity(dto)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toResponseDTO(contract)).thenReturn(responseDTO);

        ContractResponseDTO result = contractService.create(dto);

        assertEquals(42, result.getId());
        verify(contractRepository).save(contract);
    }

    @Test
    void findAll_ReturnsContracts() {
        Contract contract = new Contract();
        contract.setId(1);
        ContractResponseDTO dto = new ContractResponseDTO();
        dto.setId(1);

        when(contractRepository.findAll()).thenReturn(List.of(contract));
        when(contractMapper.toResponseDTO(contract)).thenReturn(dto);

        List<ContractResponseDTO> result = contractService.findAll();

        assertEquals(1, result.size());
        verify(contractRepository).findAll();
    }

    @Test
    void findById_ReturnsOptional() {
        Contract contract = new Contract();
        contract.setId(77);
        ContractResponseDTO dto = new ContractResponseDTO();
        dto.setId(77);

        when(contractRepository.findById(77)).thenReturn(Optional.of(contract));
        when(contractMapper.toResponseDTO(contract)).thenReturn(dto);

        Optional<ContractResponseDTO> result = contractService.findById(77);

        assertTrue(result.isPresent());
        assertEquals(77, result.get().getId());
    }

    @Test
    void deleteByIdAndEmployeeId_WhenNotFound_ReturnsFalse() {
        when(contractRepository.findByIdAndEmployeeId(5, 7)).thenReturn(Optional.empty());

        boolean result = contractService.deleteByIdAndEmployeeId(5, 7);
        assertFalse(result);
    }

    @Test
    void deleteByIdAndEmployeeId_WhenFound_Deletes() {
        Contract contract = new Contract();
        contract.setId(5);
        when(contractRepository.findByIdAndEmployeeId(5, 7)).thenReturn(Optional.of(contract));

        boolean result = contractService.deleteByIdAndEmployeeId(5, 7);

        assertTrue(result);
        verify(contractRepository).delete(contract);
    }
}