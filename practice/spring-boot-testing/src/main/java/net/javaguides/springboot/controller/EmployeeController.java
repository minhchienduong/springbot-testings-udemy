package net.javaguides.springboot.controller;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Employee createEmployee1(@RequestBody Employee employee) { return employeeService.saveEmployee(employee);}

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

//    @GetMapping
//    public List<Employee> getAllEmployees1()  {return employeeService.getAllEmployees();}

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employmentId) {
        return employeeService.getEmployeeById(employmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @GetMapping("{id}")
//    public ResponseEntity<Employee> getEmployeeById1(@PathVariable("id") long employeeId) {
//        return employeeService.getEmployeeById(employeeId)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,
                                                   @RequestBody Employee employee) {
        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                    savedEmployee.setFirstName(employee.getFirstName());
                    savedEmployee.setLastName(employee.getLastName());
                    savedEmployee.setEmail(employee.getEmail());

                    Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PutMapping("id")
//    public ResponseEntity<Employee> updateEmployee1(@PathVariable("id") long employeeId,
//                                                    @RequestBody Employee employee) {
//        return employeeService.getEmployeeById(employeeId)
//                .map(savedEmployee -> {
//                    savedEmployee.setFirstName(employee.getFirstName());
//                    savedEmployee.setLastName(employee.getLastName());
//                    savedEmployee.setEmail(employee.getEmail());
//
//                    Employee updatedEmployee = employeeService.saveEmployee(savedEmployee);
//                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
//                }).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){

        employeeService.deleteEmployee(employeeId);

        return new ResponseEntity<String>("Employee deleted successfully!.", HttpStatus.OK);

    }

//    @DeleteMapping("id")
//    public ResponseEntity<String> deleteEmployee1(@PathVariable("id") long emplpyeeId) {
//        employeeService.deleteEmployee(emplpyeeId);
//
//        return new ResponseEntity<>("Student with ID " + emplpyeeId + " deleted successfully.", HttpStatus.OK);
//    }



}
