package com.example.employeedeptcrud.runner;

import com.example.employeedeptcrud.domain.department.Department;
import com.example.employeedeptcrud.domain.employee.Employee;
import com.example.employeedeptcrud.repository.department.DepartmentRepository;
import com.example.employeedeptcrud.repository.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class DemoRunner implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String input = sc.nextLine().trim();

                switch (input) {
                    case "1" -> createEmployeeFlow(sc);
                    case "2" -> readAllEmployees();
                    case "3" -> searchByNameFlow(sc);
                    case "4" -> updateSalaryFlow(sc);
                    case "5" -> deleteEmployeeFlow(sc);
                    case "6" -> {
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    }
                    default -> System.out.println("메뉴는 1~6 중에서 입력하세요.");
                }
            }
        }
    }

    private void printMenu() {
        System.out.println("\n============================");
        System.out.println("  사원/부서 CRUD 콘솔 메뉴");
        System.out.println("============================");
        System.out.println("1. 사원 등록(부서 포함)");
        System.out.println("2. 전체 사원 조회");
        System.out.println("3. 이름 검색(급여 내림차순)");
        System.out.println("4. 급여 수정");
        System.out.println("5. 사원 삭제");
        System.out.println("6. 종료");
        System.out.print("선택> ");
    }

    @Transactional
    private void createEmployeeFlow(Scanner sc) {
        // 부서 정보 입력받기
        System.out.print("부서명 입력> ");
        String deptName = sc.nextLine().trim();

        System.out.print("부서 위치 입력> ");
        String deptLocation = sc.nextLine().trim();

        Department dept = departmentRepository.save(
                Department.builder().name(deptName).location(deptLocation).build()
        );

        // 사원 정보 입력받기
        System.out.print("사원명 입력> ");
        String empName = sc.nextLine().trim();

        System.out.print("급여 입력(숫자)> ");
        int salary = Integer.parseInt(sc.nextLine().trim());

        // hireDate는 엔티티 @PrePersist로 자동 저장되게 했으니 build에서 안 넣어도 됨
        Employee saved = employeeRepository.save(
                Employee.builder()
                        .name(empName)
                        .salary(salary)
                        .department(dept)
                        .build()
        );

        System.out.println("[등록 완료] ID=" + saved.getId() + ", 이름=" + saved.getName());
    }

    private void readAllEmployees() {
        System.out.println("\n=== 전체 사원 조회 ===");
        List<Employee> list = employeeRepository.findAll();
        if (list.isEmpty()) {
            System.out.println("(데이터 없음)");
            return;
        }
        list.forEach(this::printEmployee);
    }

    private void searchByNameFlow(Scanner sc) {
        System.out.print("검색할 이름(부분 포함 가능) 입력> ");
        String keyword = sc.nextLine().trim();

        System.out.println("\n=== 이름에 '" + keyword + "' 포함 + 급여 내림차순 ===");
        List<Employee> list = employeeRepository.findByNameContainingIgnoreCaseOrderBySalaryDesc(keyword);
        if (list.isEmpty()) {
            System.out.println("(검색 결과 없음)");
            return;
        }
        list.forEach(this::printEmployee);
    }

    @Transactional
    private void updateSalaryFlow(Scanner sc) {
        System.out.print("수정할 사원 ID 입력> ");
        long id = Long.parseLong(sc.nextLine().trim());

        Optional<Employee> opt = employeeRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("해당 ID의 사원이 없습니다.");
            return;
        }

        System.out.print("새 급여 입력(숫자)> ");
        int newSalary = Integer.parseInt(sc.nextLine().trim());

        Employee e = opt.get();
        e.changeSalary(newSalary); // 더티체킹 업데이트

        System.out.println("[수정 완료] ID=" + e.getId() + ", 이름=" + e.getName() + ", 급여=" + e.getSalary());
    }

    @Transactional
    private void deleteEmployeeFlow(Scanner sc) {
        System.out.print("삭제할 사원 ID 입력> ");
        long id = Long.parseLong(sc.nextLine().trim());

        if (!employeeRepository.existsById(id)) {
            System.out.println("해당 ID의 사원이 없습니다.");
            return;
        }

        employeeRepository.deleteById(id);
        System.out.println("[삭제 완료] ID=" + id);
    }

    private void printEmployee(Employee e) {
        System.out.println(
                e.getId() + " | "
                        + e.getName() + " | "
                        + e.getSalary() + " | "
                        + e.getHireDate() + " | "
                        + "부서(" + e.getDepartment().getId() + ", "
                        + e.getDepartment().getName() + ", "
                        + e.getDepartment().getLocation() + ")"
        );
    }
}