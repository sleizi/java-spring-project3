package com.udacity.jdnd.course3.critter.entity;

import com.google.common.base.Objects;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Table
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Employee.class)
    @ToString.Exclude
    private List<Employee> employees;

    @ManyToMany(targetEntity = Pet.class)
    @ToString.Exclude
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection
    private Set<EmployeeSkill> activities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id == schedule.id && Objects.equal(employees, schedule.employees) && Objects.equal(pets, schedule.pets) && Objects.equal(date, schedule.date) && Objects.equal(activities, schedule.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, employees, pets, date, activities);
    }
}
