package com.hospital.appointment.service;

import com.hospital.appointment.model.*;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.appointment.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private NotificationRepository notificationRepository;

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(String username) {
        Patient patient = patientService.getPatientProfile(username);
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAppointmentsByPractitioner(String username) {
        Practitioner practitioner = practitionerService.getPractitionerProfile(username);
        return appointmentRepository.findByPractitioner(practitioner);
    }

    public List<Appointment> getPendingAppointmentsByPractitioner(String username) {
        Practitioner practitioner = practitionerService.getPractitionerProfile(username);
        return appointmentRepository.findByPractitionerAndStatus(practitioner, AppointmentStatus.PENDING);
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public void confirmAppointment(Long appointmentId) {
        Appointment appointment = findById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);
            
            // Create notification for patient
            Notification notification = new Notification();
            notification.setUser(appointment.getPatient().getUser());
            notification.setMessage("Your appointment with Dr. " + appointment.getPractitioner().getLastName() + 
                                 " on " + appointment.getAppointmentDate() + " has been confirmed.");
            notificationRepository.save(notification);
        }
    }

    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = findById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointment.setReason(reason);
            appointmentRepository.save(appointment);
            
            // Create notification for patient
            Notification notification = new Notification();
            notification.setUser(appointment.getPatient().getUser());
            notification.setMessage("Your appointment with Dr. " + appointment.getPractitioner().getLastName() + 
                                 " on " + appointment.getAppointmentDate() + " has been cancelled. Reason: " + reason);
            notificationRepository.save(notification);
        }
    }

    public void completeAppointment(Long appointmentId) {
        Appointment appointment = findById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment);
            
            // Create notification for patient
            Notification notification = new Notification();
            notification.setUser(appointment.getPatient().getUser());
            notification.setMessage("Your appointment with Dr. " + appointment.getPractitioner().getLastName() + 
                                 " on " + appointment.getAppointmentDate() + " has been marked as completed.");
            notificationRepository.save(notification);
        }
    }

    public List<Appointment> getAvailableTimeSlots(Long practitionerId, Date date) {
        Practitioner practitioner = practitionerService.findById(practitionerId);
        return appointmentRepository.findByPractitionerAndAppointmentDate(practitioner, date);
    }
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }
}