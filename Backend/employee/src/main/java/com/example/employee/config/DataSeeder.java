package com.example.employee.config;

import com.example.employee.employeedetails.Employee;
import com.example.employee.employeedetails.EmployeeRepository;
import com.example.employee.contract.Contract;
import com.example.employee.contract.ContractRepository;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final Faker faker = new Faker(Locale.forLanguageTag("en-AU"));
    private final Random random = new Random();

    public DataSeeder(EmployeeRepository employeeRepository, ContractRepository contractRepository) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        contractRepository.deleteAll();
        employeeRepository.deleteAll();

        for (int i = 0; i < 10; i++) {
            Employee emp = new Employee();
            emp.setFname(faker.name().firstName());
            emp.setMiddle_name(random.nextBoolean() ? faker.name().firstName() : null);
            emp.setLast_name(faker.name().lastName());
            emp.setEmail(faker.internet().emailAddress());
            emp.setMobile_number(faker.phoneNumber().cellPhone());
            emp.setPhotoUrl(faker.internet().avatar());
            emp.setResidential_address(faker.address().fullAddress());
            emp.setEmployee_status(random.nextBoolean() ? "Active" : "Inactive");
            employeeRepository.save(emp);

            Contract contract = new Contract();
            contract.setEmployee(emp);

            // Randomly pick contract type enum
            Contract.ContractType contractType = faker.options().option(Contract.ContractType.values());
            contract.setContractType(contractType);

            // Random contract term
            contract.setContractTerm(faker.options().option("3 months", "6 months", "1 year"));

            // Random start date within past year
            LocalDate startDate = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            contract.setStartDate(null);

            // Random finish date after start date, or null if ongoing
            boolean ongoing = random.nextBoolean();
            contract.setOngoing(ongoing);
            LocalDate finishLocalDate = startDate.plusMonths(faker.number().numberBetween(1, 12));
            contract.setFinishDate(finishLocalDate);
            // Random work type
            Contract.WorkType workType = faker.options().option(Contract.WorkType.values());
            contract.setWorkType(workType);

            // Random hours per week between 10.0 and 40.0
            double hours = 10.0 + (40.0 - 10.0) * random.nextDouble();
            contract.setHoursPerWeek(BigDecimal.valueOf(Math.round(hours * 10) / 10.0));

            contractRepository.save(contract);
        }

        System.out.println("Seeded 10 random employees with contracts.");
    }
}
