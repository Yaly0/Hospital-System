package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private Disease disease;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private List<Treatment> treatments;
    private HashMap<Treatment, Rating> treatmentsDiseaseRating;
    private String treatmentsDescription;

    private String appointmentReport;
    private Appointment previousAppointment;
    private Appointment nextAppointment;

    public Appointment(int id, Patient patient, Doctor doctor, Disease disease, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.disease = disease;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public Appointment(int id, Patient patient, Doctor doctor, Disease disease, LocalDate appointmentDate, LocalTime appointmentTime, Appointment previousAppointment) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.disease = disease;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.previousAppointment = previousAppointment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public String getTreatmentsDescription() {
        return treatmentsDescription;
    }

    public void setTreatmentsDescription(String treatmentsDescription) {
        this.treatmentsDescription = treatmentsDescription;
    }

    public HashMap<Treatment, Rating> getTreatmentsDiseaseRating() {
        return treatmentsDiseaseRating;
    }

    public void setTreatmentsDiseaseRating(HashMap<Treatment, Rating> treatmentsDiseaseRating) {
        this.treatmentsDiseaseRating = treatmentsDiseaseRating;
    }

    public String getAppointmentReport() {
        return appointmentReport;
    }

    public void setAppointmentReport(String appointmentReport) {
        this.appointmentReport = appointmentReport;
    }

    public Appointment getPreviousAppointment() {
        return previousAppointment;
    }

    public void setPreviousAppointment(Appointment previousAppointment) {
        this.previousAppointment = previousAppointment;
    }

    public Appointment getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(Appointment nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    @Override
    public String toString() {
        return "Appointment{" + '\n' +
                " patient = " + patient + '\n' +
                " doctor = " + doctor + '\n' +
                " disease = " + disease + '\n' +
                " treatments = " + treatments + '\n' +
                " treatmentsDescription = '" + treatmentsDescription + '\'' + '\n' +
                " treatmentsDiseaseRating = " + treatmentsDiseaseRating + '\n' +
                " appointmentReport = '" + appointmentReport + '\'' + '\n' +
                " appointmentDate = " + appointmentDate + '\n' +
                " appointmentTime = " + appointmentTime + '\n' +
                " previousAppointment Date = " + ((previousAppointment != null) ? (previousAppointment.appointmentDate) : "null") + '\n' +
                " nextAppointment Date = " + ((nextAppointment != null) ? (nextAppointment.appointmentDate) : "null") + '\n' +
                '}';
    }
}
