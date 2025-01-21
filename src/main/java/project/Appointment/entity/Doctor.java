package project.Appointment.entity;

import jakarta.persistence.*;


@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private  Long doctorId;


    private String specialization;

    @Column(columnDefinition = "TEXT")
    private String specializationDescription;


    private String experience;

    @Column(columnDefinition = "TEXT")
    private String workExperience;


    private String qualification;

    @Column(columnDefinition = "TEXT")
    private String qualificationDescription;


    private String schedule;

    @Column(length = 1000)
    private String awards;

    @Column(columnDefinition = "TEXT")
    private String educationTraining;

    @OneToOne
    @JoinColumn(name= "user_id", referencedColumnName = "userId")
    private Users user;

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }


    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getSpecializationDescription() {
        return specializationDescription;
    }

    public void setSpecializationDescription(String specializationDescription) {
        this.specializationDescription = specializationDescription;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getQualificationDescription() {
        return qualificationDescription;
    }

    public void setQualificationDescription(String qualificationDescription) {
        this.qualificationDescription = qualificationDescription;
    }

    public String getEducationTraining() {
        return educationTraining;
    }

    public void setEducationTraining(String educationTraining) {
        this.educationTraining = educationTraining;
    }
}
