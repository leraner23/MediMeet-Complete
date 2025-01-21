package project.Appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.Appointment.entity.Appointment;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Patient;
import project.Appointment.repository.AppointmentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;


    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
        }


    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findByDateAfter(LocalDate.now());
    }

    public List<Appointment> getPastAppointments() {
        return appointmentRepository.findByDateBefore(LocalDate.now());
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment createnewappointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    public List<Appointment> findAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }


    public List<Appointment> findAppointmentsByPatient(Patient patient) {
        return  appointmentRepository.findByPatient(patient);
    }

    public Appointment findAppointmentByID(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }


    public void deleteById(long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }


    public long countTotal() {
        return appointmentRepository.count();
    }

    public List<Appointment> getTodaysAppointments() {
        return appointmentRepository.findAppointmentByDate(LocalDate.now());
    }

    public Long countTodayTotal() {
        return appointmentRepository.countByDateEquals(LocalDate.now());
    }

    public List<Appointment> findPastAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByPastDoctorAppointment(doctorId);
    }

    public List<Appointment> findTodaysDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByTodayDoctorAppointment(doctorId);
    }

    private static final List<String> ALL_TIMES = Arrays.asList(
            "9:00am", "10:00am", "11:00am", "12:00am", "2:00pm", "3:00pm", "4:00pm", "5:00pm"
    );



    public List<String> getBookedTimes(Long doctorId, LocalDate date) {
        return appointmentRepository.findBookedTimesByDoctorAndDate(doctorId, date);
    }

    public Appointment findExistingAppointment(Long userId, Long doctorId, LocalDate date) {
        return appointmentRepository.findByUserIdAndDoctorIdAndDate(userId, doctorId, date)
                .orElse(null); // Use Optional for safe retrieval
    }

}

