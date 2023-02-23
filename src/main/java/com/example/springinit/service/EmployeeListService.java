package com.example.springinit.service;

import com.example.springinit.model.Employee;
import com.example.springinit.model.EmployeeForm;
import com.example.springinit.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeListService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Value("${file-upload}")
    private String fileUpload;

    public Employee splitPath(EmployeeForm employeeForm) {
        MultipartFile multipartFile = employeeForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(employeeForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new Employee(employeeForm.getName(), employeeForm.getAddress(), fileName);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    public Employee findById(int id){
        return employeeRepository.findById(id);
    }
    public boolean create(EmployeeForm employeeForm) {
        Employee employee = splitPath(employeeForm);
        return employeeRepository.create(employee);
    }

    public boolean update(int id, EmployeeForm employeeForm) {
        Employee employee = splitPath(employeeForm);
        return employeeRepository.update(id, employee);
    }

    public boolean delete(int id) {
        return employeeRepository.delete(id);
    }
}
