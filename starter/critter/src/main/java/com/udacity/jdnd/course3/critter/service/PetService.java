package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.PetDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PetService {
    PetDTO savePet(PetDTO petDTO);

    PetDTO getPet(long petId);

    List<PetDTO> getPets();

    List<PetDTO> getPetsByOwner(long ownerId);
}
