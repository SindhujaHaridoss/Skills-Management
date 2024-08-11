package com.skillsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
		scanBasePackages = {"com.skillsmanagement"}
)
@EnableJpaRepositories({"com.skillsmanagement.repository"})
@EntityScan({"com.skillsmanagement.entity"})
public class SkillsmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillsmanagementApplication.class, args);
	}
}
