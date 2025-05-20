package com.hospital.appointment.repository;

import com.hospital.appointment.model.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    Practitioner findByUser_Id(Long userId);
    List<Practitioner> findBySpecialization(String specialization);
}