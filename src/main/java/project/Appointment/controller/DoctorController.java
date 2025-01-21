package project.Appointment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.Appointment.dto.CreateUserDto;
import project.Appointment.entity.Doctor;

import project.Appointment.entity.Users;
import project.Appointment.enums.Role;
import project.Appointment.repository.DoctorRepository;
import project.Appointment.service.DoctorService;

import project.Appointment.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {


    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;


    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/getDoctorById/{doctorId}")
    public Doctor findDoctorBbyId(@PathVariable(name="doctorId") Long doctorId) throws Exception {
       Doctor doctor= doctorService.findByDoctorId(doctorId);
       if(doctor==null) throw new Exception("Doctor Not Found");
       return doctor;
    }

    @GetMapping("/getDoctorByUserEmail")
    public Doctor findDoctorBYEmail(@PathVariable(name = "email") String email ){

        return doctorService.findByUserEmail(email);
    }


    @GetMapping("/doctor_details/{userId}")
    public String findAllDoctors(Model model, @PathVariable Long userId)  {
        Users user= userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Doctor> doctors = doctorService.findAllDoctors();
     model.addAttribute("doctors", doctors);
        model.addAttribute("navbar", "navbarAdmin");
     return "DoctorLists";
    }


    @DeleteMapping("/deleteDoctorById/{doctorId}")
    public boolean deleteDoctorById(@PathVariable(name = "doctorId") long doctorId) throws Exception {
        Doctor doctor= doctorService.deleteDoctorByID(doctorId);
        if(doctor!=null){
            return true;
        }
        return false;
    }


    @PutMapping("/UpdateDoctorById/{doctorId}")
    public Doctor updateDoctorById(@PathVariable(name = "doctorId") long doctorId, @RequestBody Doctor doctor) throws Exception {
        Doctor oldDoctor=doctorService.findByDoctorId(doctorId);
        if(oldDoctor==null) throw  new Exception("doctor not found");

        return doctorService.updateDoctorById(oldDoctor,doctor);

}

    @GetMapping("/updateYourProfile/{userId}")
    public String showDoctorUpdate(Model model , @PathVariable Long userId){
        Users user= userService.findUserById(userId);
        Doctor doctor = doctorService.getDoctorByUser(user);
        model.addAttribute("user", user);
           model.addAttribute("doctor", doctor);
          model.addAttribute("userId", userId);
        return "UpdateDoctor";
       }

       @PostMapping("/updateYourProfile/{userId}")
         public String getDoctorUpdate(@PathVariable Long userId, @ModelAttribute Doctor doctor){
           doctorService.updateDoctorProfile(userId, doctor);
           return "redirect:/doctor/profile/{userId}";
       }



    @GetMapping("/profile/{userId}")
    public String showProfile(Model model , @PathVariable Long userId){
        Users user= userService.findUserById(userId);
        Doctor doctor = doctorService.getDoctorByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "Doctorprofile";

    }

    @GetMapping("/add_doctor/{userId}")
    public String showDoctor(Model model, @PathVariable Long userId){
        model.addAttribute("userId", userId);
        model.addAttribute("user", new CreateUserDto());
    return "AdminAddDoctor";

    }

    @PostMapping("/add_doctor/{userId}")
    public String createDoctor(@ModelAttribute("user") CreateUserDto dto, Model model,  @PathVariable Long userId) {
        try {
            // Set role to DOCTOR explicitly
            dto.setRole(Role.DOCTOR);
            Users newUser = userService.createNewUser(dto);

            if (newUser.getRole() == Role.DOCTOR) {
                Doctor doctor = doctorService.createNewDoctor(newUser);
                if (doctor == null) throw new Exception("Failed to register doctor.");
            } else {
                throw new Exception("Only doctors can be registered through this form.");
            }

            model.addAttribute("successMessage", "Doctor registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error registering doctor: " + e.getMessage());
            return "AdminAddDoctor";
        }
        return "redirect:/doctor/doctor_details/{userId}";
    }


    @GetMapping("/updateDoctorProfile/{userId}/{doctorId}")
    public String showDoctorProfileUpdate(Model model , @PathVariable Long userId, @PathVariable Long doctorId){
        Doctor doctor = doctorService.findByDoctorId(doctorId);
        Users user = userService.findUserById(userId);
        model.addAttribute("doctor", doctor);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        return "AdminUpdateDoctor";
    }

    @PostMapping("/updateDoctorProfile/{userId}/{doctorId}")
    public String getDoctorProfileUpdate(@PathVariable Long userId,  @PathVariable Long doctorId, @ModelAttribute Doctor doctor){
        doctorService.adminUpdateDoctorProfile(doctorId, doctor);
        return "redirect:/doctor/doctor_details/{userId}";
    }



    @GetMapping("/userViewProfile")
    public String showUserDoctorProfile(Model model , @RequestParam Long doctorId){
        Doctor doctor = doctorService.findByDoctorId(doctorId);
        model.addAttribute("doctor", doctor);
        return "UserViewProfile";
    }


    @GetMapping("/seeDoctor/{userId}")
    public String seeAllDoctors(Model model, @PathVariable Long userId)  {
        Users user= userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Doctor> doctors = doctorService.findAllDoctors();
        model.addAttribute("doctors", doctors);
        return "PatientSeeDoctorLists";
    }
}

