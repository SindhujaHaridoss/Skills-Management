package com.skillsmanagement.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillsmanagement.DTO.PersonDTO;
import com.skillsmanagement.DTO.UpdatePersonDTO;
import com.skillsmanagement.entity.Department;
import com.skillsmanagement.entity.Person;
import com.skillsmanagement.entity.Skill;
import com.skillsmanagement.helper.ResponseStatus;
import com.skillsmanagement.services.impl.SkillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Skills and Person API", description = "Endpoints related to create/update/read/delete person and skills information")
@RequestMapping("/v1/api")
@SecurityRequirement(name = "adminuser")
public class SkillController {
	private static final Logger log = LoggerFactory
			.getLogger(SkillController.class);
	@Autowired
	private SkillService skillService;

	@GetMapping("/skills/getAllSkills")
	@Operation(summary = "Get all Skills", description = "Retrieve a list of all skills.")
	public Page<Skill> getAllSkills(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return skillService.getAllSkills(pageable);
	}

	@GetMapping("/skills/SkillById/{skillId}")
	@Operation(summary = "Get skill using ID", description = "Retrieve skill info by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Skill Details retrieved",
					content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseStatus.class),
					examples = @ExampleObject(value = "{\n" +
							"  \"statusCode\": \"200\",\n" +
							"  \"statusDescription\": \"Skill Details retrieved\",\n" +
							"  \"statusType\": \"SUCCESS\"\n" +
							"}"))),
			@ApiResponse(responseCode = "401",
			description = "Authentication Failure"),

			@ApiResponse(responseCode = "404",
			description = "skill not found",
			content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
			description = "Server error",
			content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ResponseStatus<Skill>> getSkillById(@PathVariable Long skillId) {
		return skillService.getSkillById(skillId);
	}

	@GetMapping("/skills/organizationskills")
	public Page<Map<String, Object>> getOrganizationSkills( 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return skillService.getOrganizationSkills(pageable);
	}

	@PutMapping("/skills/updateSkillToPerson")
	@Operation(summary = "Add skill to person", description = "Add Skill info to person")
	public ResponseEntity<ResponseStatus<Person>> updateSkillToPerson(@RequestBody UpdatePersonDTO personDTO) {
		return skillService.updateSkillToPerson(personDTO);
	}

	@DeleteMapping("/skills/removeSkillFromPerson/{personId}/{skillId}")
	@Operation(summary = "Remove skill from person", description = "Remove skill info from person")
	public ResponseEntity<ResponseStatus<Object>> removeSkillFromPerson(@RequestParam  Long personId,@RequestParam Long skillId) {
		return skillService.removeSkillFromPerson(personId,skillId);
	}

	@DeleteMapping("/person/deletePerson/{personId}") 
	@Operation(summary = "Remove person", description = "Remove person info")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Person details retrieved successfully"),
			@ApiResponse(responseCode = "401",
			description = "Authentication Failure"),

			@ApiResponse(responseCode = "404",
			description = "Person not found",
			content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
			description = "Server error",
			content = @Content(mediaType = "application/json"))})
	public ResponseEntity<ResponseStatus<Object>> deletePerson(@PathVariable Long personId) {
		return skillService.deletePerson(personId);
	}

	@GetMapping("/person/getAllPersons")
	@Operation(summary = "Get all person", description = "Retrieve all person information")
	public Page<Person> getAllPersons(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		log.info("Get Person started");
		return skillService.getAllPersons(pageable);
	}

	@GetMapping("/person/getPersonById/{pId}")
	@Operation(summary = "Get Person using id", description = "Retrieve person info using id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Person details retrieved successfully"),
			@ApiResponse(responseCode = "404",
			description = "Person not found"),
			@ApiResponse(responseCode = "401",
			description = "Authentication Failure"),
			@ApiResponse(responseCode = "500",
			description = "Server error")})
	public ResponseEntity<ResponseStatus<PersonDTO>> getPersonById(@PathVariable Long pId) {
		return skillService.getPersonById(pId);
	}

	@PostMapping("/person/createNewPerson")
	@Operation(summary = "Create Person", description = "Create person info")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Person details created successfully"),
			@ApiResponse(responseCode = "409",
			description = "Person Record already exists in the system. Please try to update the information"),
			@ApiResponse(responseCode = "401",
			description = "Authentication Failure"),
			@ApiResponse(responseCode = "500",
			description = "Server error")})
	public ResponseEntity<ResponseStatus<Object>> createPerson(@Valid @RequestBody PersonDTO personRequest) {
		return skillService.createPerson(personRequest);
	}

	@GetMapping("/skills/getAllDepartment")
	@Operation(summary = "Get all Department", description = "Retrieve all Department")

	public List<Department> getAllDepartment() {
		return skillService.getAllDepartment();
	}
}