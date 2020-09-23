package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AppointmentController {

    public ComboBox<Patient> comboBoxAppointmentPatient;
    public ComboBox<Doctor> comboBoxAppointmentDoctor;
    public ComboBox<Disease> comboBoxAppointmentDisease;
    public DatePicker datePickerAppointmentDate;
    public ComboBox<String> comboBoxAppointmentHour;
    public ComboBox<String> comboBoxAppointmentMinute;
    private String buttonText;
    private Appointment appointment;
    private HospitalDAO dao;

    public AppointmentController(HospitalDAO dao) {
        appointment = new Appointment();
        this.dao = dao;
        buttonText = "cancel";
    }

    @FXML
    public void initialize() {
        comboBoxAppointmentPatient.setItems(FXCollections.observableArrayList(dao.patients()));
        comboBoxAppointmentDoctor.setItems(FXCollections.observableArrayList(dao.doctors()));
        comboBoxAppointmentDisease.setItems(FXCollections.observableArrayList(dao.diseases()));
        comboBoxAppointmentHour.setItems(FXCollections.observableArrayList(hours()));
        comboBoxAppointmentMinute.setItems(FXCollections.observableArrayList(minutes()));

        comboBoxAppointmentDoctor.getSelectionModel().selectedItemProperty().addListener((obs, oldDoctor, newDoctor) -> {
            if (comboBoxAppointmentDisease.getSelectionModel().getSelectedIndex() == -1 && newDoctor != null)
                comboBoxAppointmentDisease.setItems(FXCollections.observableArrayList(dao.diseasesFromDoctor(comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem())));
            if (newDoctor == null) {
                Disease disease = comboBoxAppointmentDisease.getSelectionModel().getSelectedItem();
                comboBoxAppointmentDisease.setItems(FXCollections.observableArrayList(dao.diseases()));
                comboBoxAppointmentDisease.getSelectionModel().select(disease);
            }
        });

        comboBoxAppointmentDisease.getSelectionModel().selectedItemProperty().addListener((obs, oldDisease, newDisease) -> {
            if (comboBoxAppointmentDoctor.getSelectionModel().getSelectedIndex() == -1 && newDisease != null)
                comboBoxAppointmentDoctor.setItems(FXCollections.observableArrayList(dao.doctorsFromDisease(comboBoxAppointmentDisease.getSelectionModel().getSelectedItem())));
            if (newDisease == null) {
                Doctor doctor = comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem();
                comboBoxAppointmentDoctor.setItems(FXCollections.observableArrayList(dao.doctors()));
                comboBoxAppointmentDoctor.getSelectionModel().select(doctor);
            }
        });
        datePickerAppointmentDate.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePickerAppointmentDate.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private ArrayList<String> hours() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) hours.add((i < 10 ? "0" : "") + i);
        return hours;
    }

    private ArrayList<String> minutes() {
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i += 5) minutes.add((i < 10 ? "0" : "") + i);
        return minutes;
    }

    private boolean isTimeOkForDoctor() {
        LocalTime appointmentTime, startTime, endTime, breakStartTime, breakEndTime;
        int hour = Integer.parseInt(comboBoxAppointmentHour.getSelectionModel().getSelectedItem());
        int minute = Integer.parseInt(comboBoxAppointmentMinute.getSelectionModel().getSelectedItem());
        appointmentTime = LocalTime.of(hour, minute);
        Doctor.ShiftHours shiftHours = comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem().getShiftHours();
        startTime = shiftHours.startTime;
        endTime = shiftHours.endTime;
        breakStartTime = shiftHours.breakStartTime;
        breakEndTime = shiftHours.breakEndTime;
        breakStartTime = breakStartTime.minusMinutes(15);

        return !(startTime.compareTo(appointmentTime) > 0 ||
                (appointmentTime.compareTo(breakStartTime) > 0 && breakEndTime.compareTo(appointmentTime) > 0) ||
                appointmentTime.compareTo(endTime) > 0);
    }

    public String getButtonText() {
        return buttonText;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void clearDoctorSelectionAction() {
        comboBoxAppointmentDoctor.getSelectionModel().select(null);
    }

    public void clearDiseaseSelectionAction() {
        comboBoxAppointmentDisease.getSelectionModel().select(null);
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (comboBoxAppointmentPatient.getSelectionModel().getSelectedIndex() == -1 ||
                comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem() == null ||
                comboBoxAppointmentDisease.getSelectionModel().getSelectedItem() == null ||
                datePickerAppointmentDate.getValue() == null ||
                comboBoxAppointmentHour.getSelectionModel().getSelectedIndex() == -1 ||
                comboBoxAppointmentMinute.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment error");
            alert.setHeaderText("Some fields are empty");
            alert.setContentText("Appointment can't be made without all fields");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        if (comboBoxAppointmentDisease.getSelectionModel().getSelectedItem().getMedicalMajor().getId() != comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem().getMedicalMajor().getId()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment error");
            alert.setHeaderText("Change doctor or disease");
            alert.setContentText("Doctor " + comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem() + " doesn't treat " + comboBoxAppointmentDisease.getSelectionModel().getSelectedItem());
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        int hour = Integer.parseInt(comboBoxAppointmentHour.getSelectionModel().getSelectedItem());
        int minute = Integer.parseInt(comboBoxAppointmentMinute.getSelectionModel().getSelectedItem());
        if (!isTimeOkForDoctor()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment error");
            alert.setHeaderText("Change appointment time");
            Doctor doctor = comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem();
            alert.setContentText("Doctor " + doctor + " can't have patients at " + LocalTime.of(hour, minute)
                    + ",\ndoctor " + doctor + "'s working hours are: " + doctor.getShiftHours());
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        appointment.setId(dao.determineAppointmentId());
        appointment.setPatient(comboBoxAppointmentPatient.getSelectionModel().getSelectedItem());
        appointment.setDoctor(comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem());
        appointment.setDisease(comboBoxAppointmentDisease.getSelectionModel().getSelectedItem());
        appointment.setAppointmentDate(datePickerAppointmentDate.getValue());
        appointment.setAppointmentTime(LocalTime.of(hour, minute));
        dao.addAppointment(appointment);
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) comboBoxAppointmentPatient.getScene().getWindow();
        stage.close();
    }
}
