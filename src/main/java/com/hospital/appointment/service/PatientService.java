package com.hospital.appointment.service;

import com.hospital.appointment.model.Patient;
import com.hospital.appointment.model.User;
import com.hospital.appointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient findByUserId(Long userId) {
        return patientRepository.findByUser_Id(userId);
    }

    public Patient getPatientProfile(String username) {
        User user = (User) userService.loadUserByUsername(username);
        return patientRepository.findByUser_Id(user.getId());
    }
}