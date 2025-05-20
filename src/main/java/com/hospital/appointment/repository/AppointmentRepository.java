package com.hospital.appointment.repository;

import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentStatus;
import com.hospital.appointment.model.Patient;
import com.hospital.appointment.model.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByPractitioner(Practitioner practitioner);
    List<Appointment> findByPractitionerAndAppointmentDate(Practitioner practitioner, Date appointmentDate);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByPractitionerAndStatus(Practitioner practitioner, AppointmentStatus status);
    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);
}