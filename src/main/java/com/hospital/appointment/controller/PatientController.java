package com.hospital.appointment.controller;

import com.hospital.appointment.model.*;
import com.hospital.appointment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/dashboard")
    public String patientDashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient patient = patientService.findByUserId(user.getId());
        
        model.addAttribute("patient", patient);
        model.addAttribute("unreadCount", notificationService.getUnreadNotificationCount(user.getUsername()));
        return "patient/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient patient = patientService.findByUserId(user.getId());
        model.addAttribute("patient", patient);
        return "patient/profile";
    }

    @GetMapping("/book-appointment")
    public String showBookAppointmentForm(Model model) {
        model.addAttribute("practitioners", practitionerService.getAllPractitioners());
        model.addAttribute("appointment", new Appointment());
        return "patient/book_appointment";
    }

    @PostMapping("/book-appointment")
    public String bookAppointment(@ModelAttribute Appointment appointment, 
                                 @RequestParam Long practitionerId,
                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient patient = patientService.findByUserId(user.getId());
        Practitioner practitioner = practitionerService.findById(practitionerId);
        
        appointment.setPatient(patient);
        appointment.setPractitioner(practitioner);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentService.saveAppointment(appointment);
        
        // Create notification for practitioner
        Notification notification = new Notification();
        notification.setUser(practitioner.getUser());
        notification.setMessage("New appointment request from " + patient.getFirstName() + " " + patient.getLastName() + 
                             " on " + appointment.getAppointmentDate());
        notificationService.createNotification(practitioner.getUser(), notification.getMessage());
        
        return "redirect:/patient/appointments";
    }

    @GetMapping("/appointments")
    public String viewAppointments(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(user.getUsername());
        model.addAttribute("appointments", appointments);
        return "patient/appointments";
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationService.getUserNotifications(user.getUsername());
        model.addAttribute("notifications", notifications);
        return "patient/notifications";
    }

    @PostMapping("/mark-notification-read/{notificationId}")
    public String markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return "redirect:/patient/notifications";
    }
}