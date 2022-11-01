package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.mapper.MapStructMapper;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    private final MapStructMapper mapper;

    @Override
    public PetDTO savePet(PetDTO petDTO) {
        Pet pet = mapper.petDTOToPet(petDTO);
        Optional<Customer> customer = customerRepository.findById(petDTO.getOwnerId());
        if (customer.isPresent()) {
            pet.setCustomer(customer.get());
            PetDTO result = mapper.petToPetDTO(petRepository.save(pet));
            result.setOwnerId(petDTO.getOwnerId());
            return result;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner does not exist");
        }
    }

    @Override
    public PetDTO getPet(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet != null) {
            PetDTO result = mapper.petToPetDTO(pet);
            result.setOwnerId(pet.getCustomer().getId());
            return result;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet does not exist");
        }
    }

    @Override
    public List<PetDTO> getPets() {
        List<PetDTO> result = new ArrayList<>();
        List<Pet> pets = petRepository.findAll();
        for (Pet pet : pets) {
            PetDTO petDTO = mapper.petToPetDTO(pet);
            petDTO.setOwnerId(pet.getCustomer().getId());
            result.add(petDTO);
        }
        return result;
    }

    @Override
    public List<PetDTO> getPetsByOwner(long ownerId) {
        Customer customer = customerRepository.findById(ownerId).orElse(null);
        if (customer != null) {
            List<PetDTO> pets = customer.getPets().stream().map(mapper::petToPetDTO).collect(Collectors.toList());
            pets.forEach(petDTO -> petDTO.setOwnerId(customer.getId()));
            return pets;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner does not exist");
        }
    }

}
