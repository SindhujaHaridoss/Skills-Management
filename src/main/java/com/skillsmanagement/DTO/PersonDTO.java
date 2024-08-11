package com.skillsmanagement.DTO;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class PersonDTO {

	private Long personId;
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	@NotEmpty(message = "First Name is required")
	@Schema(description = "FirstName of the person", example = "Sindhuja")
	private String firstName;
	@NotEmpty(message = "Last Name is required")
	@Schema(description = "LastName of the person", example = "H")
	private String lastName;
	@NotNull(message = "Date of birth is required")
	@Schema(description = "Date of birth must be in the format dd-MM-yyyy",
	example = "12-12-1987")
	private String dob;
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	@Email(message = "Email should be valid")
	@Schema(description = "The email address of the person", example = "xxx@gmail.com")
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<PersonSkillsDTO> getPersonSkills() {
		return personSkills;
	}
	public void setPersonSkills(List<PersonSkillsDTO> personSkills) {
		this.personSkills = personSkills;
	}
	@NotNull(message = "Department id is required")
	@Schema(description = "Department id of the person", example = "1")
	private Long departmentId;
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	private List<PersonSkillsDTO> personSkills;

}