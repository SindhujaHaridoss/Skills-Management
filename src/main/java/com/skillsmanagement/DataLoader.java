package com.skillsmanagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.skillsmanagement.entity.Department;
import com.skillsmanagement.entity.Person;
import com.skillsmanagement.entity.PersonSkills;
import com.skillsmanagement.entity.Skill;
import com.skillsmanagement.helper.SkillLevel;
import com.skillsmanagement.repository.DepartmentRepository;
import com.skillsmanagement.repository.PersonRepository;
import com.skillsmanagement.repository.PersonSkillRepository;
import com.skillsmanagement.repository.SkillRepository;

@Configuration
public class DataLoader implements CommandLineRunner {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private PersonSkillRepository personSkillRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	public DataLoader(PersonRepository personRepository, SkillRepository skillRepository) {
		this.personRepository = personRepository;
		this.skillRepository = skillRepository;
		this.departmentRepository=departmentRepository;
	}
	@Bean
	public CommandLineRunner loadData() {
		return args -> {
			try{
				System.out.println("CommandLineRunner is executing...");
				Department engineering = new Department();
				engineering.setName("Engineering");

				Department productManagement = new Department();
				productManagement.setName("Product Management");

				Department qa = new Department();
				qa.setName("QA");

				Department sales = new Department();
				sales.setName("Sales");

				Department hr = new Department();
				hr.setName("HR");

				List<Department> departments = Arrays.asList(engineering, productManagement, qa, sales, hr);
				departmentRepository.saveAll(departments);
				//create skills
				for (int i = 1; i <= 100; i++) {
					Skill skill = new Skill();
					skill.setSkillName("Skill " + i);
					skillRepository.save(skill);
				}

				List<Skill> allSkills = skillRepository.findAll();

				// Create persons and assign 5-10 skills to each person
				Random random = new Random();
				List<Person> personList = new ArrayList<>();

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

				for (int i = 1; i <= 10; i++) { // Example with 10 persons
					Person person = new Person();
					Department department = departments.get(random.nextInt(departments.size()));

					person.setFirstName("Fname" + i);
					person.setLastName("Lname" +i);
					person.setDepartment(department);
					try {
						String dateString = "1990-01-01";
						Date dob = dateFormat.parse(dateString); 
						person.setDob(dob);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					personList.add(person);

				}
				personRepository.saveAll(personList);
				List<PersonSkills> personSkills = new ArrayList<>();
				for (Person person : personList) {
					// Each employee will have between 5 and 15 skills randomly assigned
					Set<Integer> assignedSkillIndexes = new HashSet<>();
					int numSkills = 5 + random.nextInt(6);
					while (assignedSkillIndexes.size() < numSkills) {
						int skillIndex = random.nextInt(allSkills.size());
						// if (!assignedSkillIndexes.contains(skillIndex)) {
						if (assignedSkillIndexes.add(skillIndex)) {
							Skill skill = allSkills.get(skillIndex);
							SkillLevel skillLevel = SkillLevel.values()[random.nextInt(SkillLevel.values().length)];
							int yearsOfExperience = 1 + random.nextInt(10);
							PersonSkills personSkill = new PersonSkills();

							personSkill.setPerson(person);
							personSkill.setSkill(skill);
							personSkill.setSkillLevel(skillLevel);
							personSkill.setYearsOfExperience(yearsOfExperience);

							personSkills.add(personSkill);
						}
					}
				}
				personSkillRepository.saveAll(personSkills);
				System.out.println("Data loaded.");                

			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception: " + e.getMessage()); 
			}
		};
	}
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

	}
}
