package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.mapper.MapStructMapper;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final MapStructMapper mapper;

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee savedEmp = employeeRepository.save(mapper.employeeDTOToEmployee(employeeDTO));
        return mapper.employeeToEmployeeDTO(savedEmp);
    }

    @Override
    @Transactional
    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee != null) {
            employee.setDaysAvailable(daysAvailable);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee does not exist");
        }
    }

    @Override
    public EmployeeDTO getEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(null);
        if (employee != null) {
            return mapper.employeeToEmployeeDTO(employee);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee does not exist");
        }
    }

    @Override
    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        try {
            List<Employee> availableEmployees = employeeRepository.findByDaysAvailable(employeeDTO.getDate().getDayOfWeek());
            List<Employee> result = availableEmployees.stream().filter(employee -> employee.getSkills().containsAll(employeeDTO.getSkills())).collect(Collectors.toList());
            return result.stream().map(mapper::employeeToEmployeeDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
    }
}
