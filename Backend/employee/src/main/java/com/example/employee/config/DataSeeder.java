package com.example.employee.config;

import org.springframework.web.client.RestTemplate;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final EmployeeArchiveRepository employeeArchiveRepository;
    private final ContractArchiveRepository contractArchiveRepository;
    private final EmployeeArchiveMapper employeeArchiveMapper;
    private final ContractArchiveMapper contractArchiveMapper;
    private final EmployeeRepository employeeRepository;
    private final RestTemplate restTemplate;

    private final Faker faker = new Faker(Locale.forLanguageTag("en-AU"));
    private final Random random = new Random();

    public DataSeeder(
            EmployeeRepository employeeRepository,
            ContractRepository contractRepository,
            EmployeeArchiveRepository employeeArchiveRepository,
            ContractArchiveRepository contractArchiveRepository,
            EmployeeArchiveMapper employeeArchiveMapper,
            ContractArchiveMapper contractArchiveMapper,
            RestTemplate restTemplate) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.employeeArchiveRepository = employeeArchiveRepository;
        this.contractArchiveRepository = contractArchiveRepository;
        this.employeeArchiveMapper = employeeArchiveMapper;
        this.contractArchiveMapper = contractArchiveMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clean up existing data
        contractRepository.deleteAll();
        employeeRepository.deleteAll();
        // employeeArchiveRepository.deleteAll();
        // contractArchiveRepository.deleteAll();

        List<String> photoUrls = fetchRandomPhotoUrls(10);

        for (int i = 0; i < 10; i++) {
            Employee emp = new Employee();
            emp.setFirstName(faker.name().firstName());
            emp.setMiddleName(random.nextBoolean() ? faker.name().firstName() : null);
            emp.setLastName(faker.name().lastName());
            emp.setEmail(faker.internet().emailAddress());
            emp.setMobileNumber(faker.phoneNumber().cellPhone());
            emp.setPhotoUrl(photoUrls.get(i));
            emp.setResidentialAddress(faker.address().fullAddress());
            emp.setEmployeeStatus("Active");
            emp.setCreatedAt(LocalDate.now().minusMonths(3));
            emp.setUpdatedAt(LocalDate.now());
            employeeRepository.save(emp);

            Contract contract = new Contract();
            contract.setEmployee(emp);
            contract.setContractType(faker.options().option(Contract.ContractType.values()));
            contract.setContractTerm(faker.options().option("3 months", "6 months", "1 year"));

            LocalDate startDate = LocalDate.now().minusDays(random.nextInt(60));
            contract.setStartDate(startDate);
            // boolean ongoing = random.nextBoolean();
            // contract.setOngoing(ongoing);
            contract.setFinishDate(
                    random.nextBoolean() ? null : LocalDate.now().plusMonths(3 + random.nextInt(6)));
            contract.setWorkType(faker.options().option(Contract.WorkType.values()));
            double hours = 10.0 + (40.0 - 10.0) * random.nextDouble();
            contract.setHoursPerWeek(BigDecimal.valueOf(Math.round(hours * 10) / 10.0));
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            contractRepository.save(contract);

            // Additional historical contract (from last year)
            Contract historicalContract = new Contract();
            historicalContract.setEmployee(emp);
            historicalContract.setContractType(faker.options().option(Contract.ContractType.values()));
            historicalContract.setContractTerm(faker.options().option("3 months", "6 months", "1 year"));

            LocalDate lastYearStart = LocalDate.now().minusYears(1).minusDays(random.nextInt(90)); // e.g., June–Sept
                                                                                                   // last year
            historicalContract.setStartDate(lastYearStart);

            // Ensure finish date is always after start and within last year
            LocalDate lastYearFinish = lastYearStart.plusMonths(3 + random.nextInt(6));
            if (lastYearFinish.isAfter(LocalDate.now().minusMonths(1))) {
                lastYearFinish = LocalDate.now().minusMonths(1); // Ensure it's historical
            }
            historicalContract.setFinishDate(lastYearStart.plusMonths(3 + random.nextInt(6)));

            // Ongoing is false because it's historical
            historicalContract.setWorkType(faker.options().option(Contract.WorkType.values()));
            historicalContract.setHoursPerWeek(BigDecimal.valueOf(Math.round(hours * 10) / 10.0));
            historicalContract.setCreatedAt(LocalDateTime.now().minusYears(1));
            historicalContract.setUpdatedAt(LocalDateTime.now().minusYears(1));
            contractRepository.save(historicalContract);

            // Prepare archive tables

            // EmployeeArchive archiveEmp = employeeArchiveMapper.toArchive(emp);
            // employeeArchiveRepository.save(archiveEmp);

            // ContractArchive archiveContract = contractArchiveMapper.toArchive(contract);
            // archiveContract.setEmployee(archiveEmp);
            // contractArchiveRepository.save(archiveContract);
        }

        System.out.println("Seeded 10 employees and their contracts");
    }

    private List<String> fetchRandomPhotoUrls(int count) {
        String url = "https://randomuser.me/api/?results=" + count;
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");

        List<String> photoUrls = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            String photoUrl = results.getJSONObject(i)
                    .getJSONObject("picture")
                    .getString("large");
            photoUrls.add(photoUrl);
        }
        return photoUrls;
    }
}
