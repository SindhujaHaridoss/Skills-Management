package com.skillsmanagement.DTO;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillsmanagement.helper.SkillLevel;

import io.swagger.v3.oas.annotations.media.Schema;

public class PersonSkillsDTO {
	@JsonIgnore
	private Long id;
	@JsonIgnore
	private PersonDTO person;
	public PersonDTO getPerson() {
		return person;
	}
	public void setPerson(PersonDTO person) {
		this.person = person;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public SkillDTO getSkill() {
		return skill;
	}
	public void setSkill(SkillDTO skill) {
		this.skill = skill;
	}

	public int getYearsOfExperience() {
		return yearsOfExperience;
	}
	public void setYearsOfExperience(int yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
	private SkillDTO skill;
	@NotNull
	@Schema(description = "The level of the skill",
	example = "EXPERT",
	allowableValues = {"BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"})
	private SkillLevel skillLevel;
	public SkillLevel getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(SkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}
	private int yearsOfExperience;
}
