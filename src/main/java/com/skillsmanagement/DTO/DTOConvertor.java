package com.skillsmanagement.DTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skillsmanagement.entity.Department;
import com.skillsmanagement.entity.Person;
import com.skillsmanagement.entity.PersonSkills;
import com.skillsmanagement.entity.Skill;
import com.skillsmanagement.repository.DepartmentRepository;
import com.skillsmanagement.repository.SkillRepository;

@Component
public class DTOConvertor {

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	public PersonDTO convertToPersonDTO(Person person) {
		PersonDTO personDTO = new PersonDTO();
		personDTO.setPersonId(person.getPersonId());
		personDTO.setFirstName(person.getFirstName());
		personDTO.setLastName(person.getLastName());
		personDTO.setDepartmentId(person.getDepartment().getId());
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		String dob = formatter.format(person.getDob());
		personDTO.setDob(dob);

		List<PersonSkillsDTO> personSkillsDTOs = person.getPersonSkills() == null ? 
				Collections.emptyList() : 
					person.getPersonSkills().stream()
					.map(this::convertToPersonSkillsDTO)
					.collect(Collectors.toList());

				/*
	        List<PersonSkillsDTO> personSkillsDTOs = person.getPersonSkills().stream()
	            .map(this::convertToPersonSkillsDTO)
	            .collect(Collectors.toList());*/
				personDTO.setPersonSkills(personSkillsDTOs);

				return personDTO;
	}

	public SkillDTO convertToSkillDTO(Skill skill) {
		SkillDTO skillDTO = new SkillDTO();
		skillDTO.setSkillId(skill.getSkillId());
		skillDTO.setSkillName(skill.getSkillName());

		/*List<PersonSkillsDTO> personSkillsDTOs = skill.getPersonSkills().stream()
	            .map(this::convertToPersonSkillsDTO)
	            .collect(Collectors.toList());
	        skillDTO.setPersonSkills(personSkillsDTOs);*/

		return skillDTO;
	}
	public PersonSkillsDTO convertToPersonSkillsDTO(PersonSkills personSkills) {
		PersonSkillsDTO personSkillsDTO = new PersonSkillsDTO();
		personSkillsDTO.setId(personSkills.getId());
		personSkillsDTO.setSkill(convertToSkillDTO(personSkills.getSkill()));
		personSkillsDTO.setSkillLevel(personSkills.getSkillLevel());
		personSkillsDTO.setYearsOfExperience(personSkills.getYearsOfExperience());
		return personSkillsDTO;
	}

	public Person convertToPerson(PersonDTO personDTO) throws Exception {
		try{
			Person person = new Person();

			// Mapping basic fields

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			Date dob = formatter.parse(personDTO.getDob());
			person.setFirstName(personDTO.getFirstName());
			person.setLastName(personDTO.getLastName());
			person.setDob(dob);
			person.setEmail(personDTO.getEmail());
			// check Department exits
			if (personDTO.getDepartmentId() != null) {
				Department department = departmentRepository.findById(personDTO.getDepartmentId())
						.orElseThrow(() -> new IllegalArgumentException("Department with ID " + personDTO.getDepartmentId() + " not found"));
				person.setDepartment(department);
			}
			// Mapping skills
			Set<PersonSkills> personSkills = new HashSet<>();
			for (PersonSkillsDTO personSkillDTO : personDTO.getPersonSkills()) {
				if(personSkillDTO.getSkill() != null) {
					Long skillId = personSkillDTO.getSkill().getSkillId();
					Skill skill = skillRepository.findBySkillId(skillId)
							.orElseThrow(() -> new IllegalArgumentException("Skill with ID " +personSkillDTO.getSkill().getSkillId() + " not found"));
					PersonSkills personSkill = new PersonSkills();
					personSkill.setSkill(skill);
					personSkill.setSkillLevel(personSkillDTO.getSkillLevel()); 
					personSkill.setYearsOfExperience(personSkillDTO.getYearsOfExperience());
					personSkill.setPerson(person); 
					personSkills.add(personSkill);
				}
			}
			person.setPersonSkills(personSkills);

			return person;

		}catch (ParseException e) {
			throw new Exception(e.getMessage());
		}
		catch(Exception e) {
			throw new Exception(e.getMessage());

		}
	}
}
