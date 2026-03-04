package com.example.employeedeptcrud.domain.employee;

import com.example.employeedeptcrud.domain.department.Department;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer salary;

    private LocalDate hireDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @PrePersist
    public void prePersist() {
        if (this.hireDate == null) {
            this.hireDate = LocalDate.now();
        }
    }

    public void changeSalary(Integer salary) {
        this.salary = salary;
    }
}