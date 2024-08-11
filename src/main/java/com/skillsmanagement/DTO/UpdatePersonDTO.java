package com.skillsmanagement.DTO;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdatePersonDTO {
	 @NotNull(message = "Person id is required")
	    private Long personId;
	    public Long getPersonId() {
			return personId;
		}
		public void setPersonId(Long personId) {
			this.personId = personId;
		}
	
	    @Schema(description = "FirstName of the person", example = "Sindhuja")
		private String firstName;
		
		@Schema(description = "LastName of the person", example = "H")
	    private String lastName;
		
		 @Schema(description = "Date of birth must be in the format dd-MM-yyyy",
		            example = "12-12-1987")
	    private String dob;
		public String getDob() {
			return dob;
		}
		public void setDob(String dob) {
			this.dob = dob;
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
		private Long departmentId;
	    public Long getDepartmentId() {
			return departmentId;
		}
		public void setDepartmentId(Long departmentId) {
			this.departmentId = departmentId;
		}
		private List<PersonSkillsDTO> personSkills;

}
