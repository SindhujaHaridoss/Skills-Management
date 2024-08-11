package com.skillsmanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillsmanagement.entity.Skill;



@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>{
	Optional<Skill> findBySkillNameIgnoreCase(String skillName);
	Page<Skill> findAll(Pageable pageable);
	Optional<Skill> findBySkillId(Long skillId);
}
