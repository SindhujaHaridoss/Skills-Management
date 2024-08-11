package com.skillsmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillsmanagement.entity.Person;
import com.skillsmanagement.entity.PersonSkills;

@Repository
public interface PersonSkillRepository extends JpaRepository<PersonSkills, Long>{
	void deleteByPerson(Optional<Person> person);

	@Query("SELECT ps FROM PersonSkills ps WHERE ps.person.id = :personId AND ps.skill.id = :skillId")
	Optional<PersonSkills> findByPersonIdAndSkillId(@Param("personId") Long personId, @Param("skillId") Long skillId);

	void deleteByPerson_PersonId(Long personId);	
}
