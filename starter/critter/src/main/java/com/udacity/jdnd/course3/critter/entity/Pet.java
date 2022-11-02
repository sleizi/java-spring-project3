package com.udacity.jdnd.course3.critter.entity;

import com.google.common.base.Objects;
import com.udacity.jdnd.course3.critter.pet.PetType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Table
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Pet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private PetType type;
    private String name;

    @ManyToOne
    private Customer customer;

    private LocalDate birthDate;

    private String notes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id && type == pet.type && Objects.equal(name, pet.name) && Objects.equal(customer, pet.customer) && Objects.equal(birthDate, pet.birthDate) && Objects.equal(notes, pet.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, name, customer, birthDate, notes);
    }
}
