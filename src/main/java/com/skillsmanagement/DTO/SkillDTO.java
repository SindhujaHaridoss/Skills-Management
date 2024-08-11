package com.skillsmanagement.DTO;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

public class SkillDTO {
	@NotNull(message = "Skill id is required")
	@Schema(description = "Skill id of the skill", example = "1")
	private Long skillId;

	public Long getSkillId() {
		return skillId;
	}
	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}
	@JsonIgnore
	@Schema(description = "SkillName", example = "1")
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public List<PersonSkillsDTO> getPersonSkills() {
		return personSkills;
	}
	public void setPersonSkills(List<PersonSkillsDTO> personSkills) {
		this.personSkills = personSkills;
	}
	@JsonIgnore
	private String skillName;
	@JsonIgnore
	private List<PersonSkillsDTO> personSkills;
	@JsonIgnore
	private String skillLevel;

	public String getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

}
