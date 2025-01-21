package project.Appointment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.Appointment.entity.*;
import project.Appointment.enums.Status;
import project.Appointment.repository.AppointmentRepository;
import project.Appointment.service.*;


import javax.print.Doc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {


    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;


    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;


//    @GetMapping("/appointments")
//    public String findAllAppointments(Model model){
//        List<Appointment> appointments = appointmentService.getAllAppointments();
//
//        if(appointments == null) {
//            System.out.println("appointments is NULL");
//        }
//
//        model.addAttribute("appointments", appointments);
//        return "dashboard";
//    }

    @GetMapping("/appointment_details/{userId}")
    public String showAppointment(Model model, @PathVariable Long userId) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("todaysAppointments", todaysAppointments);
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        model.addAttribute("navbar", "navbarAdmin");
        return "AppointmentDetails";
    }


    @GetMapping("/todays_appointments/{userId}")
    public String findAllTodayAppointment(Model model, @PathVariable Long userId) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("todaysAppointments", todaysAppointments);
        model.addAttribute("navbar", "navbarAdmin");
        return "todayAppointment";
    }

    @GetMapping("/past_appointments/{userId}")
    public String findAllPastAppointment(Model model, @PathVariable Long userId) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Appointment> pastsAppointments = appointmentService.getPastAppointments();
        model.addAttribute("pastsAppointments", pastsAppointments);
        model.addAttribute("navbar", "navbarAdmin");
        return "PastAppointments";
    }


    @GetMapping("/doctorAppointment_details/{userId}")
    public String showDoctorAppointment(Model model, @PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        List<Appointment> DoctorAppointments = appointmentService.findAppointmentsByDoctor(doctor);
        model.addAttribute("DoctorAppointments", DoctorAppointments);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "DoctorSeeAppointment";
    }

    @GetMapping("/pastDoctorAppointment_details/{userId}")
    public String showPastDoctorAppointment(Model model, @PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        List<Appointment> DoctorPastAppointments = appointmentService.findPastAppointmentsByDoctorId(doctor.getDoctorId());
        model.addAttribute("DoctorPastAppointments", DoctorPastAppointments);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "DoctorPastAppointments";
    }

    @GetMapping("/todayDoctor_appointments/{userId}")
    public String findTodayAppointmentDoctor(Model model, @PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        List<Appointment> todayDoctorAppointments = appointmentService.findTodaysDoctorAppointments(doctor.getDoctorId());
        model.addAttribute("todayDoctorAppointments", todayDoctorAppointments);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "DoctorTodayAppointments";
    }

    @GetMapping("/check")
    public String checkAppointments(Model model) {
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments();
        List<Appointment> pastAppointments = appointmentService.getPastAppointments();
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        return "patient_dashboard";
    }

    @GetMapping("/Book/{userId}")
    public String bookyourappointment(Model model, @PathVariable Long userId) {
        List<Doctor> doctors = doctorService.findAllDoctors();
        Users user = userService.findUserById(userId);
        Patient patient = patientService.getPatientByUser(user);
        model.addAttribute("doctors", doctors);
        model.addAttribute("user", user);
        model.addAttribute("patient", patient);
        model.addAttribute("userId", userId);
        model.addAttribute("appointment", new Appointment());
        return "BookAppointment";
    }

    @PostMapping("/Book/{userId}")
    public String showYourBooking(
            @ModelAttribute Appointment newAppointment, Model model, @PathVariable Long userId,
            @RequestParam Long doctorId, @RequestParam String date) {
        try {
            Users user = userService.findUserById(userId);
            Patient patient = patientService.getPatientByUser(user);
            Doctor doctor = doctorService.findByDoctorId(doctorId);

            // Parse the date
            LocalDate parsedDate = LocalDate.parse(date);

            // Create a new appointment
            newAppointment.setUser(user);
            newAppointment.setPatient(patient);
            newAppointment.setDoctor(doctor);
            newAppointment.setDate(parsedDate);
            newAppointment.setStatus(Status.PENDING);

            // Save the appointment to the database
            appointmentRepository.save(newAppointment);

            // Redirect to the bookTime controller with the appointmentId
            return "redirect:/appointment/bookTime/" + userId
                    + "?doctorId=" + doctorId
                    + "&date=" + date
                    + "&appointmentId=" + newAppointment.getAppointment_id();

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error creating appointment: " + e.getMessage());
            return "BookAppointment";
        }
    }


    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "No appointment found with the provided ID.");
            return "redirect:/user/PatientDashBoard";
        }
        if (appointment.getStatus() == Status.CANCELED) {
            redirectAttributes.addFlashAttribute("error", "This appointment has already been canceled.");
            return "redirect:/user/PatientDashBoard";
        }
        appointment.setStatus(Status.CANCELED);
        appointmentRepository.save(appointment);
        redirectAttributes.addFlashAttribute("message", "Appointment canceled successfully!");
        return "redirect:/user/PatientDashBoard";
    }


    @PostMapping("/delete")
    public String deleteAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "No appointment found with the provided ID.");
            return "redirect:/user/PatientDashBoard";
        }
        appointmentService.deleteById(appointment_id);
        redirectAttributes.addFlashAttribute("message", "Appointment approved successfully!");
        return "redirect:/user/PatientDashBoard";
    }

    @PostMapping("/accept/{userId}")
    public String acceptAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes, @PathVariable("userId") Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("message", "No appointment found with the provided ID.");
            return "redirect:/user/DoctorDashboard";
        }
        appointment.setStatus(Status.APPROVED);
        appointmentRepository.save(appointment);
        return "redirect:/appointment/doctorAppointment_details/{userId}";
    }

    @PostMapping("/reject/{userId}")
    public String rejectAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes, @PathVariable("userId") Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "No appointment found with the provided ID.");
            return "redirect:/user/DoctorDashboard";
        }
        appointment.setStatus(Status.REJECTED);
        appointmentRepository.save(appointment);

        return "redirect:/appointment/doctorAppointment_details/{userId}";
    }

    @PostMapping("/complete/{userId}")
    public String completeAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes, @PathVariable("userId") Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment.getStatus() == Status.PENDING) {
            redirectAttributes.addFlashAttribute("error", "No Pending appointment can be completed before approving.");
            return "redirect:/user/DoctorDashboard";
        }
        appointment.setStatus(Status.COMPLETED);
        appointmentRepository.save(appointment);

        return "redirect:/appointment/doctorAppointment_details/{userId}";
    }


//    private Long reviewingAppointmentId = null;
//    @PostMapping("/appointment/start-review")
//    public String startReview(@RequestParam("appointment_id") Long appointmentId, Model model) {
//        reviewingAppointmentId = appointmentId;
//
//        model.addAttribute("reviewingAppointmentId", reviewingAppointmentId);
//
//        return "redirect:/user/PatientDashBoard";
//    }
//
//    @PostMapping("/appointment/review")
//    public String submitReview(@RequestParam("appointment_id") Long appointmentId,
//                               @RequestParam("reviewText") String reviewText) {
//        Appointment appointment = appointmentService.findAppointmentByID(appointmentId);
//        appointment.setReview(reviewText);
//        appointmentRepository.save(appointment);
//        reviewingAppointmentId = null;
//        return "redirect:/user/PatientDashBoard";
//    }


    @GetMapping("/appointments/today")
    public String getTodaysAppointments(Model model) {
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("appointments", todaysAppointments);
        return "AppointmentDetails";
    }

    @GetMapping("/bookTime/{userId}")
    public String showAppointmentForm(
            @PathVariable Long userId, Model model,
            @RequestParam Long doctorId, @RequestParam String date, @RequestParam Long appointmentId) {
        try {
            Users user = userService.findUserById(userId);
            LocalDate parsedDate = LocalDate.parse(date);
            Doctor doctor = doctorService.findByDoctorId(doctorId);

            // Retrieve the existing appointment
            Appointment appointment = appointmentService.findAppointmentByID(appointmentId);

            // Fetch available times
            List<String> allTimes = Arrays.asList("9:00am", "10:00am", "11:00am", "12:00pm", "2:00pm", "3:00pm", "4:00pm", "5:00pm");
            List<String> bookedTimes = appointmentService.getBookedTimes(doctorId, parsedDate);

            List<String> availableTimes = allTimes.stream()
                    .filter(time -> !bookedTimes.contains(time))
                    .collect(Collectors.toList());

            // Add attributes to the model
            model.addAttribute("user", user);
            model.addAttribute("doctor", doctor);
            model.addAttribute("date", parsedDate);
            model.addAttribute("doctorId", doctorId);
            model.addAttribute("availableTimes", availableTimes);
            model.addAttribute("appointment", appointment);
            model.addAttribute("appointmentId", appointment.getAppointment_id());
            return "BookingTimeForm";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading appointment form: " + e.getMessage());
            return "error";
        }
    }


    @PostMapping("/bookTime/{userId}")
    public String finalizeAppointment(
            @PathVariable Long userId, @RequestParam Long doctorId, @RequestParam String date,
            @RequestParam String time, @RequestParam Long appointmentId, Model model) {
        try {
            // Parse the date
            LocalDate parsedDate = LocalDate.parse(date);

            // Fetch the user, doctor, and appointment
            Users user = userService.findUserById(userId);
            Doctor doctor = doctorService.findByDoctorId(doctorId);
            Appointment appointment = appointmentService.findAppointmentByID(appointmentId);

            if (appointment == null) {
                throw new IllegalArgumentException("No appointment found for ID: " + appointmentId);
            }

            // Update the existing appointment with finalized details
            appointment.setBookedTime(time);
            appointment.setStatus(Status.PENDING);

            // Save the updated appointment
            appointmentRepository.save(appointment);

            model.addAttribute("successMessage", "Appointment booked successfully!");
            return "redirect:/user/PatientDashBoard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error finalizing appointment: " + e.getMessage());
            return "BookingTimeForm";
        }
    }


    // if the user directly book from view proifle
    @GetMapping("/viewBook")
    public String bookAppointment(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        Long userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    try {
                        // Try to parse the userId from the cookie
                        userId = Long.parseLong(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // Handle invalid cookie value
                        System.err.println("Invalid userId cookie value: " + cookie.getValue());
                        return "redirect:/user/login"; // Redirect to login if invalid
                    }
                    break;
                }
            }
        }

        // If userId is null, redirect to the login page
        if (userId == null) {
            return "redirect:/user/login";
        }

        // If the user is logged in, fetch their details and render the appointment form
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);

        // Render the appointment form view
        return "BookAppointment"; // Ensure you have a template with this name
    }





    @GetMapping("/viewReport/{userId}/{appointmentId}")
    public String viewReportDoctor(@PathVariable Long appointmentId, Model model, @PathVariable Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointmentId);
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointment", appointment);
        model.addAttribute("status", appointment.getStatus());

        System.out.println("Appointment Status: " + appointment.getStatus());
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "ReportViewDoctor";
    }

    @PostMapping("/UpdateViewReport/{userId}/{appointmentId}")
    public String updateViewReport(
            @PathVariable Long appointmentId,
            @PathVariable Long userId,
            @RequestParam String report,
            Model model) {

        // Find the appointment by ID
        Appointment appointment = appointmentService.findAppointmentByID(appointmentId);

        // Check if the appointment exists
        if (appointment != null) {
            // Update the report
            appointment.setResults(report);

            System.out.println("Appointment Status: " + appointment.getStatus());
            // Save the updated appointment
            appointmentRepository.save(appointment); // Ensure this method saves the appointment to the database
        } else {
            // Handle the case where the appointment does not exist (optional)
            model.addAttribute("error", "Appointment not found.");
            return "errorPage"; // Replace with your actual error page
        }

        // Redirect to the view report page
        return "redirect:/appointment/viewReport/" + userId + "/" + appointmentId;
    }


    @GetMapping("/FeedbackReport/{userId}/{appointmentId}")
    public String ReportFeddback(@PathVariable Long appointmentId, Model model, @PathVariable Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointmentId);
        Patient patient = patientService.findPatientByuserId(userId);
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("patient", patient);
        model.addAttribute("appointment", appointment);
        model.addAttribute("status", appointment.getStatus());

        System.out.println("Appointment Status: " + appointment.getStatus());

        return "ReportFeedback";
    }

    @PostMapping("/FeedbackReport/{userId}/{appointmentId}")
    public String ReportFeedback(@PathVariable Long appointmentId, Model model, @PathVariable Long userId, @RequestParam("review") String review) {

        Appointment appointment = appointmentService.findAppointmentByID(appointmentId);

        if (appointment != null) {
            appointment.setReview(review);
            System.out.println("Appointment Status: " + appointment.getStatus());
            appointmentRepository.save(appointment);
        } else {
            // Handle the case where the appointment does not exist (optional)
            model.addAttribute("error", "Appointment not found.");
            return "errorPage";
        }

        // Redirect to the view report page
        return "redirect:/appointment/FeedbackReport/" + userId + "/" + appointmentId;
    }
}













