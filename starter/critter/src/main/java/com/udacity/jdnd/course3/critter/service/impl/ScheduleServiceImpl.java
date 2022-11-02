package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.mapper.MapStructMapper;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final EmployeeRepository employeeRepository;

    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    private final MapStructMapper mapper;

    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = mapper.scheduleDTOToSchedule(scheduleDTO);
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Employee> employees = new ArrayList<>();
        for (Long employeeId : employeeIds) {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            if (employee != null) {
                employees.add(employee);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee does not exist");
            }
        }
        List<Long> petIds = scheduleDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();
        for (Long petId : petIds) {
            Pet pet = petRepository.findById(petId).orElse(null);
            if (pet != null) {
                pets.add(pet);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet does not exist");
            }
        }
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        scheduleDTO.setId(savedSchedule.getId());
        return scheduleDTO;
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return getScheduleDTOsFromSchedules(schedules);
    }

    @Override
    public List<ScheduleDTO> getScheduleForPet(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        List<Schedule> schedules;
        if (pet != null) {
            schedules = scheduleRepository.findByPets(pet);
            return getScheduleDTOsFromSchedules(schedules);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet does not exist");
        }
    }

    @Override
    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        List<Schedule> schedules;
        if (employee != null) {
            schedules = scheduleRepository.findByEmployees(employee);
            return getScheduleDTOsFromSchedules(schedules);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee does not exist");
        }
    }

    @Override
    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        List<Schedule> schedules;
        if (customer != null) {
            schedules = scheduleRepository.findByPetsIn(customer.getPets());
            return getScheduleDTOsFromSchedules(schedules);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exist");
        }
    }

    private List<ScheduleDTO> getScheduleDTOsFromSchedules(List<Schedule> schedules) {
        List<ScheduleDTO> result = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = mapper.scheduleToScheduleDTO(schedule);
            List<Employee> employees = schedule.getEmployees();
            List<Long> employeeIds = new ArrayList<>();
            for (Employee employee : employees) {
                employeeIds.add(employee.getId());
            }
            List<Pet> pets = schedule.getPets();
            List<Long> petIds = new ArrayList<>();
            for (Pet pet : pets) {
                petIds.add(pet.getId());
            }
            scheduleDTO.setEmployeeIds(employeeIds);
            scheduleDTO.setPetIds(petIds);
            result.add(scheduleDTO);
        }
        return result;
    }
}
