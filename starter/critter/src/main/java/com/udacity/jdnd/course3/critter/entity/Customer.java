package com.udacity.jdnd.course3.critter.entity;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String phoneNumber;

    private String notes;

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private List<Pet> pets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Objects.equal(name, customer.name) && Objects.equal(phoneNumber, customer.phoneNumber) && Objects.equal(notes, customer.notes) && Objects.equal(pets, customer.pets);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, phoneNumber, notes, pets);
    }
}
