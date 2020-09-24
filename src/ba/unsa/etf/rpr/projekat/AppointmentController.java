package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
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

    private boolean edit;
    public Label labelAppointmentTreatment;
    public ComboBox<Treatment> comboBoxAppointmentTreatment;
    public Label labelAppointmentTreatments;
    public ListView<Treatment> listViewAppointmentTreatments;
    public Label labelAppointmentTreatmentsDescription;
    public TextArea textAreaAppointmentTreatmentsDescription;
    public Label labelAppointmentsReport;
    public TextArea textAreaAppointmentsReport;
    public Button buttonDeleteTreatment;
    private ArrayList<Treatment> allTreatments;
    private ArrayList<Treatment> treatments;
    private ArrayList<Treatment> initialTreatments;
    private Disease initialDisease;

    public AppointmentController(HospitalDAO dao, Appointment appointment) {
        if (appointment != null) {
            this.appointment = appointment;
            edit = true;
        } else {
            this.appointment = new Appointment();
            edit = false;
        }
        this.dao = dao;
        buttonText = "cancel";
        allTreatments = new ArrayList<>();
        treatments = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        datePickerConverter(datePickerAppointmentDate);
        comboBoxAppointmentPatient.setItems(FXCollections.observableArrayList(dao.patients()));
        comboBoxAppointmentDoctor.setItems(FXCollections.observableArrayList(dao.doctors()));
        comboBoxAppointmentDisease.setItems(FXCollections.observableArrayList(dao.diseases()));
        comboBoxAppointmentHour.setItems(FXCollections.observableArrayList(hours()));
        comboBoxAppointmentMinute.setItems(FXCollections.observableArrayList(minutes()));

        if (edit) {
            labelAppointmentTreatment.setDisable(false);
            comboBoxAppointmentTreatment.setDisable(false);
            labelAppointmentTreatments.setDisable(false);
            listViewAppointmentTreatments.setDisable(false);
            labelAppointmentTreatmentsDescription.setDisable(false);
            textAreaAppointmentTreatmentsDescription.setDisable(false);
            labelAppointmentsReport.setDisable(false);
            textAreaAppointmentsReport.setDisable(false);
            buttonDeleteTreatment.setDisable(false);

            comboBoxAppointmentPatient.getSelectionModel().select(appointment.getPatient());
            comboBoxAppointmentDoctor.getSelectionModel().select(appointment.getDoctor());
            comboBoxAppointmentDisease.getSelectionModel().select(appointment.getDisease());
            datePickerAppointmentDate.setValue(appointment.getAppointmentDate());
            comboBoxAppointmentHour.getSelectionModel().select(intToString(appointment.getAppointmentTime().getHour()));
            comboBoxAppointmentMinute.getSelectionModel().select(intToString(appointment.getAppointmentTime().getMinute()));
            textAreaAppointmentTreatmentsDescription.setText(appointment.getTreatmentsDescription());
            textAreaAppointmentsReport.setText(appointment.getAppointmentReport());

            allTreatments = dao.getTreatmentsFromDisease(appointment.getDisease());
            initialTreatments = dao.getTreatmentsFromAppointment(appointment);
            ArrayList<Treatment> otherTreatments = new ArrayList<>();
            for (Treatment t : allTreatments) if (!initialTreatments.contains(t)) otherTreatments.add(t);
            allTreatments = new ArrayList<>(otherTreatments);
            treatments = new ArrayList<>(initialTreatments);
            listViewAppointmentTreatments.setItems(FXCollections.observableArrayList(initialTreatments));

            initialDisease = new Disease(appointment.getDisease().getId(), appointment.getDisease().getDiseaseName(), appointment.getDisease().getMedicalMajor());
        }

        comboBoxAppointmentTreatment.setItems(FXCollections.observableArrayList(allTreatments));

        comboBoxAppointmentDoctor.getSelectionModel().selectedItemProperty().addListener((obs, oldDoctor, newDoctor) -> {
            if (comboBoxAppointmentDisease.getSelectionModel().getSelectedIndex() == -1 && newDoctor != null)
                comboBoxAppointmentDisease.setItems(FXCollections.observableArrayList(dao.getDiseasesFromDoctor(comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem())));
            if (newDoctor == null) {
                Disease disease = comboBoxAppointmentDisease.getSelectionModel().getSelectedItem();
                comboBoxAppointmentDisease.setItems(FXCollections.observableArrayList(dao.diseases()));
                comboBoxAppointmentDisease.getSelectionModel().select(disease);
            }
        });

        comboBoxAppointmentDisease.getSelectionModel().selectedItemProperty().addListener((obs, oldDisease, newDisease) -> {
            if (newDisease != null) allTreatments = dao.getTreatmentsFromDisease(newDisease);
            if (comboBoxAppointmentDoctor.getSelectionModel().getSelectedIndex() == -1 && newDisease != null)
                comboBoxAppointmentDoctor.setItems(FXCollections.observableArrayList(dao.getDoctorsFromDisease(comboBoxAppointmentDisease.getSelectionModel().getSelectedItem())));
            if (newDisease == null) {
                Doctor doctor = comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem();
                comboBoxAppointmentDoctor.setItems(FXCollections.observableArrayList(dao.doctors()));
                comboBoxAppointmentDoctor.getSelectionModel().select(doctor);
            }
        });

        comboBoxAppointmentTreatment.getSelectionModel().selectedItemProperty().addListener((obs, oldTreatment, newTreatment) -> {
            if (newTreatment != null) {
                treatments.add(newTreatment);
                allTreatments.removeAll(treatments);
                listViewAppointmentTreatments.setItems(FXCollections.observableArrayList(treatments));
                Platform.runLater(() -> comboBoxAppointmentTreatment.setItems(FXCollections.observableArrayList(allTreatments)));
            }
        });
    }

    private String intToString(int time) {
        return (time <= 9 ? "0" : "") + String.valueOf(time);
    }

    static void datePickerConverter(DatePicker datePickerPatientBirthDate) {
        datePickerPatientBirthDate.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePickerPatientBirthDate.setPromptText(pattern.toLowerCase());
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

    public void clearDoctorSelectionAction() {
        comboBoxAppointmentDoctor.getSelectionModel().select(null);
    }

    public void clearDiseaseSelectionAction() {
        comboBoxAppointmentDisease.getSelectionModel().select(null);
    }

    public void deleteTreatmentAction() {
        Treatment treatment = listViewAppointmentTreatments.getSelectionModel().getSelectedItem();
        if (treatment == null) return;
        allTreatments.add(treatment);
        treatments.remove(treatment);
        listViewAppointmentTreatments.setItems(FXCollections.observableArrayList(treatments));
        comboBoxAppointmentTreatment.setItems(FXCollections.observableArrayList(allTreatments));
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (comboBoxAppointmentPatient.getSelectionModel().getSelectedItem() == null ||
                comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem() == null ||
                comboBoxAppointmentDisease.getSelectionModel().getSelectedItem() == null ||
                datePickerAppointmentDate.getValue() == null ||
                comboBoxAppointmentHour.getSelectionModel().getSelectedItem() == null ||
                comboBoxAppointmentMinute.getSelectionModel().getSelectedItem() == null) {
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
        for (Treatment t : listViewAppointmentTreatments.getItems())
            if (!dao.isTreatmentForDisease(t, comboBoxAppointmentDisease.getSelectionModel().getSelectedItem())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment error");
                alert.setHeaderText("Change treatments");
                alert.setContentText("Treatments and diseases aren't compatible");
                alert.showAndWait();
                buttonText = "cancel";
                return;
            }
        appointment.setPatient(comboBoxAppointmentPatient.getSelectionModel().getSelectedItem());
        appointment.setDoctor(comboBoxAppointmentDoctor.getSelectionModel().getSelectedItem());
        appointment.setDisease(comboBoxAppointmentDisease.getSelectionModel().getSelectedItem());
        appointment.setAppointmentDate(datePickerAppointmentDate.getValue());
        appointment.setAppointmentTime(LocalTime.of(hour, minute));

        if (edit) {
            appointment.setTreatmentsDescription(textAreaAppointmentTreatmentsDescription.getText());
            appointment.setAppointmentReport(textAreaAppointmentsReport.getText());

            ArrayList<Treatment> treatmentsForDelete = new ArrayList<>(), treatmentsForAdd = new ArrayList<>();
            for (Treatment t : initialTreatments) if (!treatments.contains(t)) treatmentsForDelete.add(t);
            for (Treatment t : treatments) if (!initialTreatments.contains(t)) treatmentsForAdd.add(t);
            dao.updateAppointmentTreatments(appointment, treatmentsForDelete, treatmentsForAdd);
            dao.updateAppointment(appointment);
            if (!initialDisease.equals(appointment.getDisease())) {
                dao.updateAppointmentDiseases(appointment);
            }
        } else {
            appointment.setId(dao.determineAppointmentId());
            dao.addAppointment(appointment);
        }
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) comboBoxAppointmentPatient.getScene().getWindow();
        stage.close();
    }


}
