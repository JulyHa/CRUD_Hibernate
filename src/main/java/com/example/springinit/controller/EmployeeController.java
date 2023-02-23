package com.example.springinit.controller;

import com.example.springinit.model.Employee;
import com.example.springinit.model.EmployeeForm;
import com.example.springinit.service.EmployeeListService;
import com.example.springinit.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("employees")
public class EmployeeController {
//    @Value("${file-upload}")
//    private String fileUpload;
    @Autowired
    EmployeeListService service;
    @GetMapping
    public String getAll(Model model){
        model.addAttribute("employees", service.findAll());
        return "list";
    }
    @GetMapping("/create")
    public ModelAndView create(){
        ModelAndView modelAndView = new ModelAndView("form");
        modelAndView.addObject("employee", new EmployeeForm());
        return modelAndView;
    }

    @PostMapping("/create")
    public String addEmployee(@ModelAttribute  EmployeeForm employeeForm, RedirectAttributes redirectAttributes) {
        boolean res = service.create(employeeForm);
        if(res){
            redirectAttributes.addFlashAttribute("message", "Done!");
        }
        else {
            redirectAttributes.addFlashAttribute("message","Error!");
        }
        return "redirect:/employees/";
    }
    @GetMapping("/update/{id}")
    public ModelAndView update(@PathVariable int id){
        ModelAndView modelAndView = new ModelAndView("form");
        modelAndView.addObject("employee", service.findById(id));
        return modelAndView;
    }
    @PostMapping("/update/{id}")
    public String updateEmployee(@ModelAttribute EmployeeForm employeeForm, @PathVariable int id) {
        service.update(id, employeeForm);
        return "redirect:/employees";
    }
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id , RedirectAttributes redirectAttributes) {
        boolean res = service.delete(id);
        if(res){
            redirectAttributes.addFlashAttribute("message", "Done!");
        }
        else {
            redirectAttributes.addFlashAttribute("message","Error!");
        }
        return "redirect:/employees";
    }
}
