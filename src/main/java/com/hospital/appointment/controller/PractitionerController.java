package com.hospital.appointment.controller;

import com.hospital.appointment.model.*;
import com.hospital.appointment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/practitioner")
public class PractitionerController {

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/dashboard")
    public String practitionerDashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Practitioner practitioner = practitionerService.findByUserId(user.getId());
        
        model.addAttribute("practitioner", practitioner);
        model.addAttribute("pendingAppointments", appointmentService.getPendingAppointmentsByPractitioner(user.getUsername()));
        model.addAttribute("unreadCount", notificationService.getUnreadNotificationCount(user.getUsername()));
        return "practitioner/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Practitioner practitioner = practitionerService.findByUserId(user.getId());
        model.addAttribute("practitioner", practitioner);
        return "practitioner/profile";
    }

    @GetMapping("/appointments")
    public String viewAppointments(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsByPractitioner(user.getUsername());
        model.addAttribute("appointments", appointments);
        return "practitioner/appointments";
    }

    @PostMapping("/confirm-appointment/{appointmentId}")
    public String confirmAppointment(@PathVariable Long appointmentId) {
        appointmentService.confirmAppointment(appointmentId);
        return "redirect:/practitioner/appointments";
    }

    @PostMapping("/cancel-appointment/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId, @RequestParam String reason) {
        appointmentService.cancelAppointment(appointmentId, reason);
        return "redirect:/practitioner/appointments";
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationService.getUserNotifications(user.getUsername());
        model.addAttribute("notifications", notifications);
        return "practitioner/notifications";
    }

    @PostMapping("/mark-notification-read/{notificationId}")
    public String markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return "redirect:/practitioner/notifications";
    }
}