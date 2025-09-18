package com.example.employee.employeedetails;

import com.example.employee.contract.Contract;
import com.example.employee.contract.ContractRepository;
import com.example.employee.contract.dto.ContractCreateDTO;
import com.example.employee.contractarchive.ContractArchive;
import com.example.employee.contractarchive.ContractArchiveMapper;
import com.example.employee.contractarchive.ContractArchiveRepository;
import com.example.employee.employeearchive.EmployeeArchive;
import com.example.employee.employeearchive.EmployeeArchiveMapper;
import com.example.employee.employeearchive.EmployeeArchiveRepository;
import com.example.employee.employeedetails.dto.CreateEmployeeDTO;
import com.example.employee.employeedetails.dto.UpdateEmployeeDTO;
import com.example.employee.employeedetails.mapper.EmployeeMapper;
import com.example.employee.common.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractArchiveRepository contractArchiveRepository;

    @Mock
    private ContractArchiveMapper contractArchiveMapper;

    @Mock
    private EmployeeArchiveRepository employeeArchiveRepository;

    @Mock
    private EmployeeArchiveMapper employeeArchiveMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @Spy
    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_SavesEmployeeAndContracts() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");

        ContractCreateDTO contractDTO = new ContractCreateDTO();
        contractDTO.setContractType(Contract.ContractType.Permanent);
        contractDTO.setContractTerm("12 months");
        contractDTO.setStartDate(LocalDate.now());
        contractDTO.setFinishDate(LocalDate.now().plusMonths(12));
        contractDTO.setWorkType(Contract.WorkType.FullTime);
        contractDTO.setHoursPerWeek(BigDecimal.valueOf(40));
        dto.setContracts(List.of(contractDTO));

        Employee employee = new Employee();
        employee.setId(1);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(contractRepository.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.create(dto);

        assertNotNull(result);
        verify(employeeRepository).save(any(Employee.class));
        verify(contractRepository).saveAll(any());
        assertEquals(1, result.getId());
    }

    @Test
    void findAll_CallsRepository() {
        employeeService.findAll();
        verify(employeeRepository).findAll();
    }

    @Test
    void findById_CallsRepository() {
        employeeService.findById(5);
        verify(employeeRepository).findById(5);
    }

    @Test
    void archiveAndDeleteEmployee_WhenEmployeeNotFound_ThrowsNotFound() {
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeService.archiveAndDeleteEmployee(99));
    }

    @Test
    void archiveAndDeleteEmployee_ArchivesAndDeletes() {
        Employee employee = new Employee();
        employee.setId(10);

        EmployeeArchive archivedEmployee = new EmployeeArchive();
        Contract contract = new Contract();
        contract.setId(200);

        when(employeeRepository.findById(10)).thenReturn(Optional.of(employee));
        when(employeeArchiveMapper.toArchive(employee)).thenReturn(archivedEmployee);
        when(contractRepository.findByEmployeeId(10)).thenReturn(List.of(contract));
        when(contractArchiveMapper.toArchive(eq(contract), any())).thenReturn(new ContractArchive());

        employeeService.archiveAndDeleteEmployee(10);

        verify(employeeArchiveRepository).save(archivedEmployee);
        verify(contractArchiveRepository).saveAll(any());
        verify(contractRepository).deleteAll(any());
        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteById_WhenNotFound_ReturnsFalse() {
        when(employeeRepository.findById(123)).thenReturn(Optional.empty());
        boolean result = employeeService.deleteById(123);
        assertFalse(result);
    }

    @Test
    void deleteById_WhenFound_DeletesAndReturnsTrue() {
        Employee employee = new Employee();
        employee.setId(50);
        when(employeeRepository.findById(50)).thenReturn(Optional.of(employee));

        boolean result = employeeService.deleteById(50);

        assertTrue(result);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void updateById_WhenEmployeeNotFound_ReturnsEmpty() {
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO();
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.updateById(1, dto);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateById_WhenEmployeeFound_UpdatesAndSaves() {
        Employee employee = new Employee();
        employee.setId(101);

        UpdateEmployeeDTO dto = new UpdateEmployeeDTO();

        when(employeeRepository.findById(101)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.updateById(101, dto);

        assertTrue(result.isPresent());
        verify(employeeMapper).updateEntityFromDTO(dto, employee);
        verify(employeeRepository).save(employee);
    }

    @Test
    void getAllEmployeesWithContracts_CallsRepository() {
        employeeService.getAllEmployeesWithContracts();
        verify(employeeRepository).findAllWithContracts();
    }
}
