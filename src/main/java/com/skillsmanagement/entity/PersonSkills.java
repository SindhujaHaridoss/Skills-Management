package com.skillsmanagement.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skillsmanagement.helper.SkillLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"person","skill"})
@ToString(exclude = {"person", "skill"})
@Table(name = "person_skills", indexes = {
	    @Index(name = "idx_person_id", columnList = "person_id"), 
	    @Index(name = "idx_skill_id", columnList = "skill_id"), 
	    @Index(name = "idx_person_skill", columnList = "person_id, skill_id") // Composite index on person_id and skill_id
	})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PersonSkills implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	@JsonBackReference
	private Person person;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "skill_id", nullable = false)
	@JsonManagedReference
	@JsonIgnore
	private Skill skill;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SkillLevel skillLevel;

	private int yearsOfExperience; 
}
