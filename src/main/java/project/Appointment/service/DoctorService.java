package project.Appointment.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Users;
import project.Appointment.repository.AppointmentRepository;
import project.Appointment.repository.DoctorRepository;
import project.Appointment.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;
@Autowired
private UserRepository userRepository;

    public Doctor createNewDoctor(Users user) throws Exception {
        if(user==null) throw new Exception("Empty doctor");
        Doctor doctor=new Doctor();
        doctor.setUser(user);
        return doctorRepository.save(doctor);
    }

    public Doctor findByDoctorId(Long doctorId) {
       return doctorRepository.findById(doctorId).orElse(null);

    }

    public Doctor deleteDoctorByID(long doctorId) throws Exception {
        Doctor doctor=findByDoctorId(doctorId);
        if(doctor!=null){
             doctorRepository.deleteById(doctorId);
             return doctor;
        }
      throw new Exception("doctor not found");
    }

    public Doctor updateDoctorById(Doctor oldDoctor, Doctor doctor) {


        oldDoctor.setExperience(doctor.getExperience());
        oldDoctor.setQualification(doctor.getQualification());
        oldDoctor.setSpecialization(doctor.getSpecialization());
        return doctorRepository.save(oldDoctor);
    }

    public List<Doctor> findAllDoctors() {
       return doctorRepository.findAll();
    }

    public Doctor findByUserEmail(String email) {
        return doctorRepository.findByUserEmail(email);
    }


    public Doctor findByUser(Users user) {
        return doctorRepository.findByUser(user);   }

    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for userId: " + userId));
    }



    public void updateDoctorProfile(Long userId, Doctor updatedDoctor) {
        // Check if user exists
        Optional<Users> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist");
        }

        // Fetch the associated doctor
        Users user = userOptional.get();
        Doctor existingDoctor = getDoctorByUser(user);
        if (existingDoctor == null) {
            throw new IllegalArgumentException("No doctor profile found for user with ID " + userId);
        }

        // Update doctor profile
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setExperience(updatedDoctor.getExperience());
        existingDoctor.setQualification(updatedDoctor.getQualification());

        // Save the updated doctor profile
        doctorRepository.save(existingDoctor);
    }





    public Doctor getDoctorByUser(Users user) {
        return  doctorRepository.findByUser(user);
    }


    public long countTotalDoctorAppointment(Long docterId) {
        return appointmentRepository.countDoctorAppointment(docterId);
    }

    public Long countTodayTotal() {
        return appointmentRepository.countByDateEquals(LocalDate.now());
    }

    public long countTodayTotalApproved(Long docterId) {
        return appointmentRepository.countTodayTotalApprovedDoctor(docterId);
    }

    public long countTotalAppointmentDone(Long docterId) {
        return appointmentRepository.countTotalAppointmentCompletedDoctor(docterId);
    }

    public long countTotalDoctor() {
        return userRepository.countTotalDoctor();
    }

    public void adminUpdateDoctorProfile(Long doctorId, Doctor updatedDoctor) {




        Doctor existingDoctor = findByDoctorId(doctorId);
        if (existingDoctor == null) {
            throw new IllegalArgumentException("No doctor profile found for doctor with ID " + doctorId);
        }

        // Update doctor profile
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setExperience(updatedDoctor.getExperience());
        existingDoctor.setQualification(updatedDoctor.getQualification());
        existingDoctor.setEducationTraining(updatedDoctor.getEducationTraining());
        existingDoctor.setQualificationDescription(updatedDoctor.getQualificationDescription());
        existingDoctor.setWorkExperience(updatedDoctor.getWorkExperience());
        existingDoctor.setAwards(updatedDoctor.getAwards());
        existingDoctor.setSchedule(updatedDoctor.getSchedule());

        // Save the updated doctor profile
        doctorRepository.save(existingDoctor);
    }


}
