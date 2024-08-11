package com.skillsmanagement.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillsmanagement.entity.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	Page<Person> findAll(Pageable pageable);
	Optional<Person> findByFirstNameAndLastNameAndDob(String firstName, String lastName, Date dob);
	Optional<Person> findByPersonId(Long personId);
}

