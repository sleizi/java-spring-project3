package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.mapper.MapStructMapper;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    private final MapStructMapper mapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        Customer customer = mapper.customerDTOToCustomer(customerDTO);
        List<Pet> pets = new ArrayList<>();
        List<Long> petIds = customerDTO.getPetIds();
        if (petIds != null) {
            for (Long petId : petIds) {
                Pet pet = petRepository.findById(petId).orElse(null);
                if (pet != null) {
                    pets.add(pet);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet does not exist");
                }
            }
        }

        customer.setPets(pets);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerDTO result = mapper.customerToCustomerDTO(savedCustomer);
        result.setPetIds(petIds);
        return result;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> result = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO = mapper.customerToCustomerDTO(customer);
            List<Long> petIds = new ArrayList<>();

            List<Pet> pets = customer.getPets();
            for (Pet pet : pets
            ) {
                petIds.add(pet.getId());
            }
            customerDTO.setPetIds(petIds);
            result.add(customerDTO);
        }
        return result;
    }

    @Override
    public CustomerDTO getOwnerByPet(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet != null) {
            Customer customer = pet.getCustomer();
            List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            CustomerDTO result = mapper.customerToCustomerDTO(customer);
            result.setPetIds(petIds);
            return result;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet does not exist");
        }
    }
}
