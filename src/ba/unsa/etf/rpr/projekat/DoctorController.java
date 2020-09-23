package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Person.Gender.*;

public class DoctorController {

    public TextField textFieldDoctorFirstName;
    public TextField textFieldDoctorLastName;
    public TextField textFieldDoctorHomeAddress;
    public DatePicker datePickerDoctorBirthDate;
    public TextField textFieldDoctorCitizen;
    public TextField textFieldDoctorEmailAddress;
    public RadioButton radioButtonDoctorMaleButton;
    public ToggleGroup toggleGroupGender;
    public ChoiceBox<BloodType> choiceBoxDoctorBloodType;
    public TextField textFieldDoctorPhoneNumber;
    public ChoiceBox<MedicalMajor> choiceBoxDoctorMedicalMajor;

    public ChoiceBox<String> choiceBoxDoctorStartTimeHour;
    public ChoiceBox<String> choiceBoxDoctorStartTimeMinute;
    public ChoiceBox<String> choiceBoxDoctorEndTimeHour;
    public ChoiceBox<String> choiceBoxDoctorEndTimeMinute;
    public ChoiceBox<String> choiceBoxDoctorBreakStartTimeHour;
    public ChoiceBox<String> choiceBoxDoctorBreakStartTimeMinute;
    public ChoiceBox<String> choiceBoxDoctorBreakEndTimeHour;
    public ChoiceBox<String> choiceBoxDoctorBreakEndTimeMinute;


    private String buttonText;
    private Doctor doctor;
    private HospitalDAO dao;

    public DoctorController(HospitalDAO dao) {
        doctor = new Doctor();
        this.dao = dao;
        buttonText = "cancel";
    }

    @FXML
    public void initialize() {
        AppointmentController.datePickerConverter(datePickerDoctorBirthDate);
        choiceBoxDoctorMedicalMajor.setItems(FXCollections.observableArrayList(dao.medicalMajors()));

        choiceBoxDoctorBloodType.setItems(FXCollections.observableArrayList(BloodType.values()));
        choiceBoxDoctorStartTimeHour.setItems(FXCollections.observableArrayList(hours()));
        choiceBoxDoctorEndTimeHour.setItems(FXCollections.observableArrayList(hours()));
        choiceBoxDoctorBreakStartTimeHour.setItems(FXCollections.observableArrayList(hours()));
        choiceBoxDoctorBreakEndTimeHour.setItems(FXCollections.observableArrayList(hours()));
        choiceBoxDoctorStartTimeMinute.setItems(FXCollections.observableArrayList(minutes()));
        choiceBoxDoctorEndTimeMinute.setItems(FXCollections.observableArrayList(minutes()));
        choiceBoxDoctorBreakStartTimeMinute.setItems(FXCollections.observableArrayList(minutes()));
        choiceBoxDoctorBreakEndTimeMinute.setItems(FXCollections.observableArrayList(minutes()));
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
    public String getButtonText() {
        return buttonText;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }
    public void okAction() {
        buttonText = "ok";
        if (textFieldDoctorFirstName.getText().trim().isEmpty() ||
                textFieldDoctorLastName.getText().trim().isEmpty() ||
                textFieldDoctorHomeAddress.getText().trim().isEmpty() ||
                datePickerDoctorBirthDate.getValue() == null ||
                textFieldDoctorCitizen.getText().trim().isEmpty() ||
                textFieldDoctorEmailAddress.getText().trim().isEmpty() ||
                textFieldDoctorPhoneNumber.getText().trim().isEmpty() ||
                toggleGroupGender.getSelectedToggle() == null ||
                choiceBoxDoctorBloodType.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorStartTimeHour.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorEndTimeHour.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorBreakStartTimeHour.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorBreakEndTimeHour.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorStartTimeMinute.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorEndTimeMinute.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorBreakStartTimeMinute.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorBreakEndTimeMinute.getSelectionModel().getSelectedIndex() == -1 ||
                choiceBoxDoctorMedicalMajor.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doctor error");
            alert.setHeaderText("Some fields are empty");
            alert.setContentText("Doctor can't be added without all fields");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        if (datePickerDoctorBirthDate.getValue().compareTo(LocalDate.now()) > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doctor error");
            alert.setHeaderText("Change birth date");
            alert.setContentText("Birth date can't be in the future");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        try {
            doctor.setBirthDateCitizenNumberAndGender(
                    datePickerDoctorBirthDate.getValue(), new CitizenNumber(textFieldDoctorCitizen.getText()),
                    radioButtonDoctorMaleButton.isSelected() ? MALE : FEMALE);

        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doctor error");
            alert.setHeaderText("Change birth date, citizen number or gender");
            alert.setContentText("Birth date, citizen number and gender must be correct and compatible");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        try {
            doctor.setPhoneNumber(new PhoneNumber(textFieldDoctorPhoneNumber.getText()));
        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doctor error");
            alert.setHeaderText("Change phone number");
            alert.setContentText("Phone number must be in any format used in Bosnia and Herzegovina");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }

        try {
            doctor.setEmailAddress(new EmailAddress(textFieldDoctorEmailAddress.getText()));
        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doctor error");
            alert.setHeaderText("Change email address");
            alert.setContentText("Email address must be in correct format");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }

        try {
            LocalTime startTime = LocalTime.of(Integer.parseInt(choiceBoxDoctorStartTimeHour.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(choiceBoxDoctorStartTimeMinute.getSelectionModel().getSelectedItem()));

            LocalTime endTime = LocalTime.of(Integer.parseInt(choiceBoxDoctorEndTimeHour.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(choiceBoxDoctorEndTimeMinute.getSelectionModel().getSelectedItem()));

            LocalTime breakStartTime = LocalTime.of(Integer.parseInt(choiceBoxDoctorBreakStartTimeHour.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(choiceBoxDoctorBreakStartTimeMinute.getSelectionModel().getSelectedItem()));

            LocalTime breakEndTime = LocalTime.of(Integer.parseInt(choiceBoxDoctorBreakEndTimeHour.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(choiceBoxDoctorBreakEndTimeMinute.getSelectionModel().getSelectedItem()));

            doctor.setShiftHours(new Doctor.ShiftHours(startTime,endTime,breakStartTime,breakEndTime));
        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doctor error");
            alert.setHeaderText("Change working or break hours");
            alert.setContentText("Hours must be logical\nIf there isn't break, choose same time \nfor start and end between start and end times");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }

        doctor.setId(dao.determineDoctorId());
        doctor.setFirstName(textFieldDoctorFirstName.getText());
        doctor.setLastName(textFieldDoctorLastName.getText());
        doctor.setHomeAddress(textFieldDoctorHomeAddress.getText());
        doctor.setBloodType(choiceBoxDoctorBloodType.getSelectionModel().getSelectedItem());
        doctor.setMedicalMajor(choiceBoxDoctorMedicalMajor.getSelectionModel().getSelectedItem());

        dao.addDoctor(doctor);

        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) textFieldDoctorFirstName.getScene().getWindow();
        stage.close();
    }
}
