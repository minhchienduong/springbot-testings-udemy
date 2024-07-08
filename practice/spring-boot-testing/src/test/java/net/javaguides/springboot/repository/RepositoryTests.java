package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class RepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
    }

    @Test
    public void saveEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee);
        assertTrue(savedEmployee.getId() > 0);
    }

    @Test
    public void testFindAll() {
        Employee employee1 = Employee.builder()
                .firstName("David")
                .lastName("Beckham")
                .email("david@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        List<Employee> employeeList = employeeRepository.findAll();

        assertTrue(employeeList.size() == 2);
        assertTrue(employeeList.get(0).getFirstName().equals("Ramesh"));
        assertEquals(employeeList.get(0).getLastName(), "Fadatare");
        assertNotNull(employeeList.get(1));
    }

    @DisplayName("Test find by Id")
    @Test
    public void testFindById() {
        employeeRepository.save(employee);

        Employee foundEmployee = employeeRepository.findById(employee.getId()).get();

        assertNotNull(foundEmployee);
    }

    @Test
    public void testDeleteById() {
        employeeRepository.save(employee);

        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        assertThat(employeeOptional).isEmpty();
    }

}
