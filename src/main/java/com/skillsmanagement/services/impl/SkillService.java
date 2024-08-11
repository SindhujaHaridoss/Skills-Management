package com.skillsmanagement.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsmanagement.DTO.DTOConvertor;
import com.skillsmanagement.DTO.PersonDTO;
import com.skillsmanagement.DTO.PersonSkillsDTO;
import com.skillsmanagement.DTO.UpdatePersonDTO;
import com.skillsmanagement.entity.Department;
import com.skillsmanagement.entity.Person;
import com.skillsmanagement.entity.PersonSkills;
import com.skillsmanagement.entity.Skill;
import com.skillsmanagement.helper.ResponseStatus;
import com.skillsmanagement.repository.DepartmentRepository;
import com.skillsmanagement.repository.PersonRepository;
import com.skillsmanagement.repository.PersonSkillRepository;
import com.skillsmanagement.repository.SkillRepository;

@Service
public class SkillService {
	private static final Logger log = LoggerFactory
			.getLogger(com.skillsmanagement.services.impl.SkillService.class);

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private PersonSkillRepository personSkillRepository;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DTOConvertor dtoConverter;

	@Transactional(readOnly=true)
	@Cacheable(value = "skills", key = "'page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize")
	public Page<Skill> getAllSkills(Pageable pageable) {
		return skillRepository.findAll(pageable);
	}

	public Page<Map<String, Object>> getOrganizationSkills(Pageable pageable) {
		Page<Skill> skillPage = skillRepository.findAll(pageable);
		List<Skill> skills = skillPage.getContent();
		List<PersonSkills> personSkills = personSkillRepository.findAll();
		log.info("getOrganizationSkills");
		// Group EmployeeSkills by Skill ID
		Map<Long, List<PersonSkills>> personSkillsBySkill = personSkills.stream()
				.collect(Collectors.groupingBy(ps -> ps.getSkill().getSkillId()));


		log.info("personSkillsBySkill"+personSkillsBySkill.size());
		// Map skills to their respective data
		List<Map<String, Object>> skillsData = skills.stream().map(skill -> {
			List<PersonSkills> skillPersonSkills = personSkillsBySkill.getOrDefault(skill.getSkillId(), Collections.emptyList());


			log.info("skillPersonSkills"+skillPersonSkills.size());


			// Map employees with their proficiency levels
			List<Map<String, Object>> employeesWithProficiency = skillPersonSkills.stream()
					.map(ps -> {
						Map<String, Object> employeeMap = new HashMap<>();
						employeeMap.put("personId", ps.getPerson().getPersonId());
						employeeMap.put("personName", ps.getPerson().getFirstName());
						employeeMap.put("department", ps.getPerson().getDepartment().getName());
						employeeMap.put("SkillLevel", ps.getSkillLevel().name());
						employeeMap.put("yearsOfExperience", ps.getYearsOfExperience());
						return employeeMap;
					})
					.collect(Collectors.toList());
			// Group employees by department
			Map<Long, List<PersonSkills>> personSkillsByDepartment = skillPersonSkills.stream()
					.collect(Collectors.groupingBy(ps -> ps.getPerson().getDepartment().getId()));

			// Map departments to their respective data
			List<Map<String, Object>> departments = personSkillsByDepartment.entrySet().stream().map(entry -> {
				Long departmentId = entry.getKey();
				List<PersonSkills> departmentSkills = entry.getValue();

				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("departmentId", departmentId);
				departmentMap.put("departmentName", departmentSkills.get(0).getPerson().getDepartment().getName());
				departmentMap.put("employeesWithSkill", departmentSkills.size());
				return departmentMap;
			}).collect(Collectors.toList());

			Map<String, Object> skillMap = new HashMap<>();
			skillMap.put("skillId", skill.getSkillId());
			skillMap.put("skillName", skill.getSkillName());
			skillMap.put("employeesWithProficiency", employeesWithProficiency);
			skillMap.put("departments", departments);
			return skillMap;
		}).collect(Collectors.toList());
		// Return the final aggregated data structure
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("organizationId", "TestORG123"); 
		resultMap.put("organizationName", "TechCorp"); 
		resultMap.put("skills", skillsData);
		return new PageImpl<>(skillsData, pageable, skillPage.getTotalElements());
	}


	@Transactional(readOnly=true)
	@Cacheable(value = "skills", key = "#skillName")
	public ResponseEntity<ResponseStatus<Skill>> getSkillBySkillName(String skillName) {
		try {

			Optional<Skill> skill = skillRepository.findBySkillNameIgnoreCase(skillName);
			if(skill.isPresent()) {
				ResponseStatus<Skill> status= new ResponseStatus<>("200", "SUCCESS","Skill Details retrieved",skill.get());
				return new ResponseEntity<>(status, HttpStatus.OK);
			} else {
				ResponseStatus<Skill> status = new ResponseStatus<Skill>("404", "ERROR","Skill not available",null);
				return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
			}
		} catch(Exception e) {
			ResponseStatus<Skill> status = new ResponseStatus<Skill>("500", "ERROR","ERROR occured while fectching skill information",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

	@Transactional(readOnly=true)
	@Cacheable(value = "skills", key = "#id")
	public ResponseEntity<ResponseStatus<Skill>> getSkillById(Long id) {
		try {
			Optional<Skill> skill = skillRepository.findBySkillId(id);
			if(skill.isPresent()) {
				ResponseStatus<Skill> status= new ResponseStatus<>("200", "SUCCESS","Skill Details retrieved",skill.get());
				return new ResponseEntity<>(status, HttpStatus.OK);
			} else {
				ResponseStatus<Skill> status = new ResponseStatus<Skill>("404", "ERROR","Skill not found",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
		} catch(Exception e) {
			ResponseStatus<Skill> status = new ResponseStatus<Skill>("500", "ERROR","ERROR occured while fectching skill information",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@CacheEvict(value = "skills", key = "#id")
	public ResponseEntity<ResponseStatus<Skill>> deleteSkill(Long id) {
		try{
			Optional<Skill> skill = skillRepository.findBySkillId(id);
			if(skill.isPresent()) {
				skillRepository.deleteById(id);
				ResponseStatus<Skill> status= new ResponseStatus<Skill>("200", "SUCCESS","Skill Deleted",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
			else
			{
				ResponseStatus<Skill> status = new ResponseStatus<Skill>("204", "ERROR","Skill not found",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}

		}catch (Exception e) {
			ResponseStatus<Skill> status = new ResponseStatus<Skill>("500", "ERROR","ERROR occured while fectching skill information",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@Transactional
	@CachePut(value = "persons", key = "#result.body.data?.personId ?: 'defaultKey'", unless = "#result.body.data == null || #result.body.data.personId == null")
	public ResponseEntity<ResponseStatus<Person>> updateSkillToPerson(UpdatePersonDTO personDTO) {
		try{
			Person person = new Person();

			Optional<Person> personOpt = personRepository.findByPersonId(personDTO.getPersonId());
			if (personOpt.isPresent()) {
				log.info("person id"+personOpt.get());
				person = updateSkill(personDTO, personOpt.get());
				personRepository.save(person);
				ResponseStatus<Person> status= new ResponseStatus<>("200", "SUCCESS","skill updated to person record successfully",personOpt.get());
				return new ResponseEntity<>(status, HttpStatus.OK);
			}else {
				log.info("Search with name and date");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

				Date dob = formatter.parse(personDTO.getDob());
				Optional<Person> personwithName = personRepository.findByFirstNameAndLastNameAndDob(personDTO.getFirstName(), personDTO.getLastName(),dob);
				if(personwithName.isPresent()){
					person = updateSkill(personDTO, personwithName.get());
					personRepository.save(person);
					ResponseStatus<Person> status= new ResponseStatus<>("200", "SUCCESS","skill updated to person record successfully",person);
					return new ResponseEntity<>(status, HttpStatus.OK);
				} else {
					ResponseStatus<Person> status= new ResponseStatus<>("204", "ERROR","Person not found to add skill",null);
					return new ResponseEntity<>(status, HttpStatus.OK);
				}
			}
		}catch(IllegalArgumentException ie) {
			ResponseStatus<Person> status= new ResponseStatus<>("500", "ERROR",ie.getMessage(),null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			log.error("Exception block"+e.getMessage());
			e.printStackTrace();
			ResponseStatus<Person> status= new ResponseStatus<>("500", "ERROR","Error occured while adding skill to person record",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	public Person updateSkill(UpdatePersonDTO personDTO, Person person) {
		log.info("updateSkill");
		Set<PersonSkills> newSkills = new HashSet<>();
		for (PersonSkillsDTO personSkillDTO : personDTO.getPersonSkills()) {
			Skill skill = skillRepository.findById(personSkillDTO.getSkill().getSkillId())
					.orElseThrow(() -> new IllegalArgumentException("Skill with ID " + personSkillDTO.getSkill().getSkillId() + " not found"));

			PersonSkills personSkill = new PersonSkills();
			personSkill.setSkill(skill);
			personSkill.setSkillLevel(personSkillDTO.getSkillLevel());
			personSkill.setYearsOfExperience(personSkillDTO.getYearsOfExperience());
			personSkill.setPerson(person); // Link to person
			newSkills.add(personSkill);
		}

		// Update skills
		Set<PersonSkills> existingSkills = person.getPersonSkills();
		Set<PersonSkills> skillsToAdd = new HashSet<>(newSkills);
		skillsToAdd.removeAll(existingSkills);
		log.info("Skills to add: {}", skillsToAdd);

		Set<PersonSkills> skillsToUpdate = new HashSet<>(existingSkills);
		skillsToUpdate.retainAll(newSkills);
		log.info("Skills to update: {}", skillsToUpdate);


		Set<PersonSkills> skillsToRemove = new HashSet<>(existingSkills);
		skillsToRemove.removeAll(newSkills);
		log.info("Skills to remove: {}", skillsToRemove);

		log.info("skillsToRemove.remove");
		person.getPersonSkills().addAll(skillsToAdd);
		log.info("Skills added to person: {}", skillsToAdd);

		// person.getPersonSkills().removeAll(skillsToRemove);
		log.info("Skills removed from person: {}", skillsToRemove);


		log.info("skillsToUpdate"+skillsToUpdate.size());
		if(skillsToUpdate.size() >1) {
			for (PersonSkills updatedSkill : skillsToUpdate) {
				log.info("Updating existing skill: {}", updatedSkill.getSkill());

				existingSkills.stream()
				.filter(existingSkill -> {
					log.info("existingSkill skill: {}", existingSkill.getSkill());
					//existingSkill.getSkill().equals(updatedSkill.getSkill()));
					Skill existingSkillSkill = existingSkill.getSkill();
					return existingSkillSkill != null && existingSkillSkill.equals(updatedSkill.getSkill());
				})
				.forEach(existingSkill -> {
					log.info("Updating skill level and experience for skill ID: {}", existingSkill.getSkill().getSkillId());
					existingSkill.setSkillLevel(updatedSkill.getSkillLevel());
					existingSkill.setYearsOfExperience(updatedSkill.getYearsOfExperience());
				});
			}
		}

		return person;
	}

	@Transactional
	@CachePut(value = "persons", key = "#personId")
	public ResponseEntity<ResponseStatus<Object>> removeSkillFromPerson(Long personId, Long skillId) {
		try{
			Optional<Person> person = personRepository.findByPersonId(personId);
			Optional<Skill> skill = skillRepository.findBySkillId(skillId);
			if(person.isPresent() && skill.isPresent()){
				Set<PersonSkills> personSkills = person.get().getPersonSkills();
				// Find the skill to remove
				Optional<PersonSkills> optionalSkillToRemove = personSkills.stream()
						.filter(ps -> ps.getSkill() != null && ps.getSkill().getSkillId().equals(skillId))
						.findFirst();
				if (!optionalSkillToRemove.isPresent()) {
					log.error("Skill with ID " + skillId + " not found in person's skills");
					ResponseStatus<Object> status= new ResponseStatus<>("404","ERROR","Skill with ID " + skillId + " not found in person's skills",null);
					return new ResponseEntity<>(status, HttpStatus.OK);
				}else {
					PersonSkills skillToRemove = optionalSkillToRemove.get();
					log.info("Skill to remove from person: {}", skillToRemove);

					// Remove the skill from the person's skills set
					personSkills.remove(skillToRemove);
					personRepository.save(person.get());
				}
				ResponseStatus<Object> status= new ResponseStatus<>("200", "SUCCESS","skill"+skillId +"removed from person record",null);
				return new ResponseEntity<>(status, HttpStatus.OK);

			}else if(!person.isPresent()) {
				ResponseStatus<Object> status= new ResponseStatus<>("204", "ERROR","Person record unavailable for the given personid",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}else if(!skill.isPresent()) {
				ResponseStatus<Object> status= new ResponseStatus<>("204", "ERROR","Skill unavailable for the given skillId",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}else {
				ResponseStatus<Object> status= new ResponseStatus<>("204", "ERROR","Unavailable to identify the details",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
		}catch (Exception e) {
			ResponseStatus<Object> status= new ResponseStatus<>("500", "501", e.getMessage(),null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Transactional
	@CachePut(value = "persons", key = "#result.body.data?.personId ?: 'defaultKey'", unless = "#result.body.data == null || #result.body.data.personId == null")
	public ResponseEntity<ResponseStatus<Object>> createPerson(PersonDTO personDTO) {
		try{
			log.info("Create Person Method");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Person person = new Person();
			Date dob = formatter.parse(personDTO.getDob());
			Optional<Person> duplicatePerson = personRepository.findByFirstNameAndLastNameAndDob(personDTO.getFirstName(),personDTO.getLastName(), dob);
			if(!duplicatePerson.isPresent()){
				person= dtoConverter.convertToPerson(personDTO);
				personRepository.save(person);

				log.info("person" + person);

				ResponseStatus<Object> status= new ResponseStatus<>("200", "SUCCESS","Person details created successfully",person);
				return new ResponseEntity<>(status, HttpStatus.CREATED); 

			} else{
				ResponseStatus<Object> status= new ResponseStatus<>("409", "ERROR","Person Record already exists in the system. Please try to update the information",null);
				return new ResponseEntity<>(status, HttpStatus.CONFLICT);
			}
		}catch(IllegalArgumentException ex) {
			log.error("handleIllegalArgumentException block"+ex.getMessage());
			ResponseStatus<Object> status = new ResponseStatus<>("400", "ERROR", ex.getMessage(), null);
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}catch (ParseException pe) {
			ResponseStatus<Object> status= new ResponseStatus<>("500", "ERROR","Invalid Data format",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e) {
			log.error("Exception block"+e.getMessage());
			e.printStackTrace();
			ResponseStatus<Object> status= new ResponseStatus<>("500", "ERROR",e.getMessage(),null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Transactional(readOnly=true)
	@Cacheable(value = "persons", key = "#id")
	public ResponseEntity<ResponseStatus<PersonDTO>> getPersonById(Long id) {
		try{
			Optional<Person> personOpt= personRepository.findByPersonId(id);
			if(personOpt.isPresent()) {
				PersonDTO personDTO = dtoConverter.convertToPersonDTO(personOpt.get());
				ResponseStatus<PersonDTO> status= new ResponseStatus<>("200", "SUCCESS","Person details retrieved successfully",personDTO);
				return new ResponseEntity<>(status, HttpStatus.OK);
			} else {
				ResponseStatus<PersonDTO> status= new ResponseStatus<>("404", "ERROR","Person Details unavailable",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
		}catch(Exception e) {
			ResponseStatus<PersonDTO> status= new ResponseStatus<>("500", "ERROR","Error occured while retrieving person record by id",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@CacheEvict(value = "persons", key = "#id")
	public ResponseEntity<ResponseStatus<Object>> deletePerson(Long id) {
		try{
			Optional<Person> person=personRepository.findByPersonId(id);
			if(person.isPresent()) {
				personRepository.deleteById(id);
				personSkillRepository.deleteByPerson(person);
				//this.deletePersonById(person.get());
				ResponseStatus<Object> status= new ResponseStatus<>("200", "SUCCESS","Person record deleted successfully",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}else {
				ResponseStatus<Object> status= new ResponseStatus<>("404", "ERROR","Person record not found for the personId",null);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
		}
		catch(Exception e) {
			log.error("Exception block");
			ResponseStatus<Object> status= new ResponseStatus<>("500", "ERROR","Error occured while deleting person record",null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Cacheable(value = "persons", key = "'page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize")
	public Page<Person> getAllPersons(Pageable pageable) {
		Page<Person> person= personRepository.findAll(pageable);
		log.info("Get Person end");
		return person;
	}

	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}
}
