package com.skillsmanagement.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "skillId")
@EqualsAndHashCode(exclude = {"personSkill"})
@Table(name = "skill", indexes = {
	    @Index(name = "idx_skill_name", columnList = "skillName") 
	})
public class Skill implements Serializable {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @JsonProperty
	    private Long skillId;
	    @JsonProperty
	    private String skillName;
	    @OneToMany(mappedBy = "skill",fetch = FetchType.EAGER)
	    @JsonIgnore
		@JsonBackReference
	    private List<PersonSkills> personSkills;
		
}
