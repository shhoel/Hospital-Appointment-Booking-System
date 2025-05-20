package com.hospital.appointment.controller;

import com.hospital.appointment.model.*;
import com.hospital.appointment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register-patient")
    public String showPatientRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "register_patient";
    }

    @PostMapping("/register-patient")
    public String registerPatient(@ModelAttribute Patient patient, 
                                @RequestParam String email,
                                @RequestParam String password) {
        // Create user
        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        //user.setRole("PATIENT");
        user.setRole(User.Role.valueOf("PATIENT"));
        user.setApproved(false); // Needs admin approval
        
        User savedUser = userService.saveUser(user);
        
        // Create patient
        patient.setUser(savedUser);
        patientService.savePatient(patient);
        
        return "redirect:/login?registered";
    }

    @GetMapping("/register-practitioner")
    public String showPractitionerRegistrationForm(Model model) {
        model.addAttribute("practitioner", new Practitioner());
        return "register_practitioner";
    }

    @PostMapping("/register-practitioner")
    public String registerPractitioner(@ModelAttribute Practitioner practitioner,
                                     @RequestParam String email,
                                     @RequestParam String password) {
        // Create user
        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        //user.setRole("PRACTITIONER");
        user.setRole(User.Role.valueOf("PRACTITIONER"));
        user.setApproved(false); // Needs admin approval
        
        User savedUser = userService.saveUser(user);
        
        // Create practitioner
        practitioner.setUser(savedUser);
        practitionerService.savePractitioner(practitioner);
        
        return "redirect:/login?registered";
    }
}