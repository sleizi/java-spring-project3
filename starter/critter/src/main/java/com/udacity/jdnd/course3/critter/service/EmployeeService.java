package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeService {
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);

    void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId);

    EmployeeDTO getEmployee(@PathVariable long employeeId);

    List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO);
}
