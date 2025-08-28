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
import com.example.employee.employeedetails.dto.EmployeeWithContractsDTO;
import com.example.employee.employeedetails.dto.UpdateEmployeeDTO;
import com.example.employee.employeedetails.mapper.EmployeeMapper;
import com.example.employee.common.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private EmployeeArchiveRepository employeeArchiveRepository;
    @Mock
    private ContractArchiveRepository contractArchiveRepository;
    @Mock
    private EmployeeArchiveMapper employeeArchiveMapper;
    @Mock
    private ContractArchiveMapper contractArchiveMapper;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_EmployeeWithoutContracts_Success() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO();
        dto.setFirstName("John");

        Employee saved = new Employee();
        saved.setId(1);
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        Employee result = employeeService.create(dto);

        assertEquals(1, result.getId());
        verify(employeeRepository).save(any(Employee.class));
        verifyNoInteractions(contractRepository);
    }

    @Test
    void create_EmployeeWithContracts_Success() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO();
        ContractCreateDTO contractDTO = new ContractCreateDTO();
        dto.setContracts(List.of(contractDTO));

        Employee saved = new Employee();
        saved.setId(1);
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);
        when(contractRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.create(dto);

        assertEquals(1, result.getId());
        assertFalse(result.getContracts().isEmpty());
        verify(contractRepository).saveAll(anyList());
    }

    @Test
    void findAll_ReturnsList() {
        when(employeeRepository.findAll()).thenReturn(List.of(new Employee()));
        List<Employee> employees = employeeService.findAll();
        assertEquals(1, employees.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void findById_Found_ReturnsOptional() {
        Employee emp = new Employee();
        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));
        Optional<Employee> result = employeeService.findById(1);
        assertTrue(result.isPresent());
    }

    @Test
    void findById_NotFound_ReturnsEmpty() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
        assertTrue(employeeService.findById(1).isEmpty());
    }

    @Test
    void archiveAndDeleteEmployee_Found_Success() {
        Employee emp = new Employee();
        emp.setId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));

        EmployeeArchive empArchive = new EmployeeArchive();
        when(employeeArchiveMapper.toArchive(emp)).thenReturn(empArchive);

        List<Contract> contracts = List.of(new Contract());
        when(contractRepository.findByEmployeeId(1)).thenReturn(contracts);

        ContractArchive contractArchive = new ContractArchive();
        when(contractArchiveMapper.toArchive(any(Contract.class), eq(empArchive)))
                .thenReturn(contractArchive);

        employeeService.archiveAndDeleteEmployee(1);

        verify(employeeArchiveRepository).save(empArchive);
        verify(contractArchiveRepository).saveAll(anyList());
        verify(contractRepository).deleteAll(contracts);
        verify(employeeRepository).delete(emp);
    }

    @Test
    void archiveAndDeleteEmployee_NotFound_Throws() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> employeeService.archiveAndDeleteEmployee(1));
    }

    @Test
    void deleteById_Found_ReturnsTrue() {
        Employee emp = new Employee();
        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));
        boolean result = employeeService.deleteById(1);
        assertTrue(result);
        verify(employeeRepository).delete(emp);
    }

    @Test
    void deleteById_NotFound_ReturnsFalse() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
        assertFalse(employeeService.deleteById(1));
    }

    @Test
    void updateById_Found_UpdatesAndReturns() {
        Employee emp = new Employee();
        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));

        UpdateEmployeeDTO dto = new UpdateEmployeeDTO();
        when(employeeRepository.save(emp)).thenReturn(emp);

        Optional<Employee> result = employeeService.updateById(1, dto);

        assertTrue(result.isPresent());
        verify(employeeMapper).updateEntityFromDTO(dto, emp);
        verify(employeeRepository).save(emp);
    }

    @Test
    void updateById_NotFound_ReturnsEmpty() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
        assertTrue(employeeService.updateById(1, new UpdateEmployeeDTO()).isEmpty());
    }

    @Test
    void replaceById_Found_Success() {
        Employee emp = new Employee();
        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO();

        Optional<Employee> result = employeeService.replaceById(1, dto);

        assertTrue(result.isPresent());
        verify(employeeMapper).updateEntityFromDTO(dto, emp);
        verify(employeeRepository).save(emp);
    }

    @Test
    void replaceById_NotFound_ReturnsEmpty() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
        assertTrue(employeeService.replaceById(1, new UpdateEmployeeDTO()).isEmpty());
    }

    @Test
    void getAllEmployeesWithContracts_Empty_ReturnsEmptyList() {
        when(employeeRepository.findAllWithContracts()).thenReturn(List.of());
        assertTrue(employeeService.getAllEmployeesWithContracts().isEmpty());
    }

    @Test
    void getAllEmployeesWithContracts_Found_ReturnsEmployeeList() {
        Employee emp = new Employee();
        when(employeeRepository.findAllWithContracts()).thenReturn(List.of(emp));

        List<Employee> result = employeeService.getAllEmployeesWithContracts();

        assertEquals(1, result.size());
        assertSame(emp, result.get(0));
    }

}