package net.javaguides.springboot.service;


import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
    }

    @Test
    public void testSaveEmployee() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals(savedEmployee.getFirstName(), "Ramesh");
        assertEquals(savedEmployee, employee);
        assertEquals(savedEmployee, employee);

    }

    @Test
    public void testSaveEmployee_thenThrowsException() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        Employee employee1 = Employee.builder()
                .firstName("David")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee1);
        });

        String expectedMessage = "Employee already exists with given email:" + employee1.getEmail();
        String actualMessage = exception.getMessage();
        System.out.println(expectedMessage);
        System.out.println(actualMessage);
        assert actualMessage.contains(expectedMessage);
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage.equals(expectedMessage));

    }

    @Test
    public void testGetAllEmployees() {
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();

        when(employeeRepository.findAll()).thenReturn(List.of(employee,employee1));

        List<Employee> employeeList = employeeService.getAllEmployees();

        assertEquals(employeeList.size(), 2);
        assertNotNull(employeeList);
    }

    @Test
    public void testGetAllEmployees_returnEmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).isEmpty();
        assertEquals(employeeList.size(), 0);

    }

    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Employee employee1 = employeeService.getEmployeeById(employee.getId()).get();

        assertEquals(employee1, employee);
    }

    @Test
    public void testUpdateEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        employee.setFirstName("David");

        Employee updatedEmployee =  employeeService.updateEmployee(employee);

        verify(employeeRepository, times(1)).save(employee);

        assertEquals(updatedEmployee.getFirstName(), "David");
        assertEquals(updatedEmployee, employee);
    }

    @Test
    public void testDeleteEmployee() {
        long employeeId = 1L;

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

}
