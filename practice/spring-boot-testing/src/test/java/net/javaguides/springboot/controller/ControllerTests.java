package net.javaguides.springboot.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaveEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);


        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }

    @Test
    public void testGetAllEmployee() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        when(employeeService.getAllEmployees()).thenReturn(listOfEmployees);

        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        when(employeeService.getEmployeeById(employee.getId())).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    public void testGetEmployeeById_throwException() throws Exception {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        when(employeeService.getEmployeeById(employee.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void testUpdateEmployee() throws  Exception {
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(savedEmployee));
        when(employeeService.updateEmployee(any(Employee.class)))
                .thenReturn(updatedEmployee);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));

    }

    @Test
    public void testUpdateEmployee_throwException() throws Exception {
        long employeeId = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
        //when(employeeService.updateEmployee(any(Employee.class))).thenReturn(updatedEmployee);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));


        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(status().isNotFound());;

    }

    @Test
    public void testDeleteEmployee() throws Exception {
        long employeeId = 1L;

        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
