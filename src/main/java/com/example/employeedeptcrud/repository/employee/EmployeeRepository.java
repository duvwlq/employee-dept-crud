package com.example.employeedeptcrud.repository.employee;

import com.example.employeedeptcrud.domain.employee.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = "department")
    List<Employee> findByNameContainingIgnoreCaseOrderBySalaryDesc(String name);

    @Override
    @EntityGraph(attributePaths = "department")
    List<Employee> findAll();
}