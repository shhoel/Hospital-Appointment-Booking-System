package com.hospital.appointment.controller;

import com.hospital.appointment.model.User;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.appointment.service.PractitionerService;
import com.hospital.appointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "admin/dashboard";
    }

    @GetMapping("/pending-approvals")
    public String showPendingApprovals(Model model) {
        model.addAttribute("pendingPatients", userService.findUnapprovedUsersByRole("PATIENT"));
        model.addAttribute("pendingPractitioners", userService.findUnapprovedUsersByRole("PRACTITIONER"));
        return "admin/pending_approvals";
    }

    @PostMapping("/approve-user/{userId}")
    public String approveUser(@PathVariable Long userId) {
        userService.approveUser(userId);
        return "redirect:/admin/pending-approvals";
    }

    @GetMapping("/manage-practitioners")
    public String managePractitioners(Model model) {
        model.addAttribute("practitioners", practitionerService.getAllPractitioners());
        return "admin/manage_practitioners";
    }

    @GetMapping("/appointments")
    public String viewAllAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.findAllAppointments());
        return "admin/appointments";
    }

    @PostMapping("/complete-appointment/{appointmentId}")
    public String completeAppointment(@PathVariable Long appointmentId) {
        appointmentService.completeAppointment(appointmentId);
        return "redirect:/admin/appointments";
    }
}