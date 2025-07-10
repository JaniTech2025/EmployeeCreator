package com.example.employee.config;

import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;
import com.example.employee.contract.Contract;
import com.example.employee.contract.ContractRepository;
import com.example.employee.contractarchive.ContractArchive;
import com.example.employee.contractarchive.ContractArchiveMapper;
import com.example.employee.contractarchive.ContractArchiveRepository;
import com.example.employee.employeearchive.EmployeeArchive;
import com.example.employee.employeearchive.EmployeeArchiveMapper;
import com.example.employee.employeearchive.EmployeeArchiveRepository;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final EmployeeArchiveRepository employeeArchiveRepository;
    private final ContractArchiveRepository contractArchiveRepository;
    private final EmployeeArchiveMapper employeeArchiveMapper;
    private final ContractArchiveMapper contractArchiveMapper;

    private final Faker faker = new Faker(Locale.forLanguageTag("en-AU"));
    private final Random random = new Random();

    public DataSeeder(
            EmployeeRepository employeeRepository,
            ContractRepository contractRepository,
            EmployeeArchiveRepository employeeArchiveRepository,
            ContractArchiveRepository contractArchiveRepository,
            EmployeeArchiveMapper employeeArchiveMapper,
            ContractArchiveMapper contractArchiveMapper) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.employeeArchiveRepository = employeeArchiveRepository;
        this.contractArchiveRepository = contractArchiveRepository;
        this.employeeArchiveMapper = employeeArchiveMapper;
        this.contractArchiveMapper = contractArchiveMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clean up existing data
        contractRepository.deleteAll();
        employeeRepository.deleteAll();
        employeeArchiveRepository.deleteAll();
        contractArchiveRepository.deleteAll();

        for (int i = 0; i < 10; i++) {
            Employee emp = new Employee();
            emp.setFname(faker.name().firstName());
            emp.setMiddle_name(random.nextBoolean() ? faker.name().firstName() : null);
            emp.setLast_name(faker.name().lastName());
            emp.setEmail(faker.internet().emailAddress());
            emp.setMobile_number(faker.phoneNumber().cellPhone());
            emp.setPhotoUrl(faker.internet().avatar());
            emp.setResidential_address(faker.address().fullAddress());
            emp.setEmployee_status("Active");
            emp.setCreated_at(LocalDate.now().minusMonths(3));
            emp.setUpdated_at(LocalDate.now());
            employeeRepository.save(emp);

            Contract contract = new Contract();
            contract.setEmployee(emp);
            contract.setContractType(faker.options().option(Contract.ContractType.values()));
            contract.setContractTerm(faker.options().option("3 months", "6 months", "1 year"));

            LocalDate startDate = LocalDate.now().minusDays(random.nextInt(60));
            contract.setStartDate(startDate);
            boolean ongoing = random.nextBoolean();
            contract.setOngoing(ongoing);
            contract.setFinishDate(ongoing ? null : LocalDate.now().plusMonths(3 + random.nextInt(6)));
            contract.setWorkType(faker.options().option(Contract.WorkType.values()));
            double hours = 10.0 + (40.0 - 10.0) * random.nextDouble();
            contract.setHoursPerWeek(BigDecimal.valueOf(Math.round(hours * 10) / 10.0));
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            contractRepository.save(contract);

            // ARCHIVE section
            EmployeeArchive archiveEmp = employeeArchiveMapper.toArchive(emp);
            employeeArchiveRepository.save(archiveEmp);

            ContractArchive archiveContract = contractArchiveMapper.toArchive(contract);
            contractArchiveRepository.save(archiveContract);
        }

        System.out.println("Seeded 10 employees, contracts, and their archive records.");
    }
}
