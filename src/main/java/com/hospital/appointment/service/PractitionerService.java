package com.hospital.appointment.service;

import com.hospital.appointment.model.Practitioner;
import com.hospital.appointment.model.User;
import com.hospital.appointment.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private UserService userService;

    public Practitioner savePractitioner(Practitioner practitioner) {
        return practitionerRepository.save(practitioner);
    }

    public Practitioner findByUserId(Long userId) {
        return practitionerRepository.findByUser_Id(userId);
    }

    public Practitioner getPractitionerProfile(String username) {
        User user = (User) userService.loadUserByUsername(username);
        return practitionerRepository.findByUser_Id(user.getId());
    }

    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }

    public List<Practitioner> findBySpecialization(String specialization) {
        return practitionerRepository.findBySpecialization(specialization);
    }

    public Practitioner findById(Long id) {
        return practitionerRepository.findById(id).orElse(null);
    }
}