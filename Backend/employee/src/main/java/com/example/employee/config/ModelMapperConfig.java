package com.example.employee.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.employee.employeedetails.CreateEmployeeDTO;
import com.example.employee.employeedetails.Employee;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.typeMap(String.class, String.class).setConverter(new StringTrimConverter());
        return modelMapper;
    }

    private class StringTrimConverter implements Converter<String, String> {

        @Override
        public String convert(MappingContext<String, String> context) {
            if (context.getSource() == null) {
                return null;
            }
            return context.getSource().trim();
        }

    }

    public Employee map(CreateEmployeeDTO data, Class<Employee> employeeFromDB) {
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

}