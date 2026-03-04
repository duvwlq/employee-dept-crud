package com.example.employeedeptcrud.repository.department;

import com.example.employeedeptcrud.domain.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}