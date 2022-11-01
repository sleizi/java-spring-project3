package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getOwnerByPet(long petId);
}
