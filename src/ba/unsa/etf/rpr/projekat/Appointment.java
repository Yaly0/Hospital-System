package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private Disease disease;
    private LocalDate date;
    private LocalTime time;
    private Treatment treatment;
    private String treatmentDescription;
    private int diseaseTreatmentRating;
    private String appointmentReport;
    private Appointment previousAppointment;
    private Appointment nextAppointment;

    public Appointment(int id, Patient patient, Doctor doctor, Disease disease, LocalDate date, LocalTime time) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.disease = disease;
        this.date = date;
        this.time = time;
    }

    public Appointment(int id, Patient patient, Doctor doctor, Disease disease, LocalDate date, LocalTime time, Appointment previousAppointment) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.disease = disease;
        this.date = date;
        this.time = time;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public String getTreatmentDescription() {
        return treatmentDescription;
    }

    public void setTreatmentDescription(String treatmentDescription) {
        this.treatmentDescription = treatmentDescription;
    }

    public int getDiseaseTreatmentRating() {
        return diseaseTreatmentRating;
    }

    public void setDiseaseTreatmentRating(Rating diseaseTreatmentRating) {
        this.diseaseTreatmentRating = diseaseTreatmentRating.getRatingValue();
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
                " treatment = " + treatment + '\n' +
                " treatmentDescription = '" + treatmentDescription + '\'' + '\n' +
                " date = " + date + '\n' +
                " time = " + time + '\n' +
                " previousAppointment Date = " + ((previousAppointment != null) ? (previousAppointment.date) : "null") + '\n' +
                " nextAppointment Date = " + ((nextAppointment != null) ? (nextAppointment.date) : "null") + '\n' +
                " diseaseTreatmentRating = " + diseaseTreatmentRating + '\n' +
                '}';
    }
}
