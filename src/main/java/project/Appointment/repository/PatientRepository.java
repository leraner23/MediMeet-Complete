package project.Appointment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Appointment.entity.Patient;
import project.Appointment.entity.Users;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {


    Patient findByUserEmail(String email);

    Patient getPatientByUser(Users user);

    Optional<Patient> findByUserUserId(Long userId);
}
