package com.example.student;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private String email;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Employee> xxx = new ArrayList<>();
        xxx = employeeRepository.findAll();
        model.addAttribute("allemplist", xxx);
        return "index";
    }

    @GetMapping("/addnew")
    public String addNewEmployee(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "newemployee";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        // Validate email using a regular expression
        String emailPattern = "^[a-zA-Z0-9_]+@mail\\.valenciacollege\\.edu$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(employee.getEmail());

        if (matcher.matches()) {
            employeeRepository.save(employee);
            return "redirect:/";
        } else {
            // Invalid email format
            // Handle the error, redirect to an error page or show an error message
            return "newemployee"; // Redirect to an error page or handle accordingly
        }
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable(value = "id") long id, Model model) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()) {
            model.addAttribute("employee", employee);
        }
        else{
            model.addAttribute("employee", new Employee());
        }
        return "update";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteThroughId(@PathVariable(value = "id") long id) {
        employeeRepository.deleteById(id);
        return "redirect:/";

    }
}
