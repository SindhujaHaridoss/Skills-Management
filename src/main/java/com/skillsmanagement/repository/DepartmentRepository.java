package com.skillsmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsmanagement.entity.Department;

public interface DepartmentRepository  extends JpaRepository<Department, Long> {

	Optional<Department> findByNameIgnoreCase(String departmentName);

	Optional<Department> findById(String departmentId);

}
