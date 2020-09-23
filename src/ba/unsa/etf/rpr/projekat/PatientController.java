package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Person.Gender.*;

public class PatientController {

    public TextField textFieldPatientFirstName;
    public TextField textFieldPatientLastName;
    public TextField textFieldPatientHomeAddress;
    public DatePicker datePickerPatientBirthDate;
    public TextField textFieldPatientCitizen;
    public TextField textFieldPatientEmailAddress;
    public RadioButton radioButtonPatientMaleButton;
    public ToggleGroup toggleGroupGender;
    public ChoiceBox<BloodType> choiceBoxPatientBloodType;
    public TextField textFieldPatientPhoneNumber;
    public Spinner spinnerPatientHeight;
    public Spinner spinnerPatientWeight;
    public Label labelPatientBMI;
    private String buttonText;
    private Patient patient;
    private HospitalDAO dao;

    public PatientController(HospitalDAO dao) {
        patient = new Patient();
        this.dao = dao;
        buttonText = "cancel";
    }

    @FXML
    public void initialize() {

        AppointmentController.datePickerConverter(datePickerPatientBirthDate);
        choiceBoxPatientBloodType.setItems(FXCollections.observableArrayList(BloodType.values()));
        spinnerPatientHeight.getValueFactory().setValue(170);
        spinnerPatientWeight.getValueFactory().setValue(70);
    }

    public String getButtonText() {
        return buttonText;
    }

    public Patient getPatient() {
        return patient;
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (textFieldPatientFirstName.getText().trim().isEmpty() ||
                textFieldPatientLastName.getText().trim().isEmpty() ||
                textFieldPatientHomeAddress.getText().trim().isEmpty() ||
                datePickerPatientBirthDate.getValue() == null ||
                textFieldPatientCitizen.getText().trim().isEmpty() ||
                textFieldPatientEmailAddress.getText().trim().isEmpty() ||
                textFieldPatientPhoneNumber.getText().trim().isEmpty() ||
                toggleGroupGender.getSelectedToggle() == null ||
                choiceBoxPatientBloodType.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Patient error");
            alert.setHeaderText("Some fields are empty");
            alert.setContentText("Patient can't be added without all fields");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        if (datePickerPatientBirthDate.getValue().compareTo(LocalDate.now()) > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Patient error");
            alert.setHeaderText("Change birth date");
            alert.setContentText("Birth date can't be in the future");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        try {
            patient.setBirthDateCitizenNumberAndGender(
                    datePickerPatientBirthDate.getValue(), new CitizenNumber(textFieldPatientCitizen.getText()),
                    radioButtonPatientMaleButton.isSelected() ? MALE : FEMALE);

        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Patient error");
            alert.setHeaderText("Change birth date, citizen number or gender");
            alert.setContentText("Birth date, citizen number and gender must be correct and compatible");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        try {
            patient.setPhoneNumber(new PhoneNumber(textFieldPatientPhoneNumber.getText()));
        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Patient error");
            alert.setHeaderText("Change phone number");
            alert.setContentText("Phone number must be in any format used in Bosnia and Herzegovina");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }

        try {
            patient.setEmailAddress(new EmailAddress(textFieldPatientEmailAddress.getText()));
        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Patient error");
            alert.setHeaderText("Change email address");
            alert.setContentText("Email address must be in correct format");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }

        try {
            patient.setHeight(new Patient.Height((Integer) spinnerPatientHeight.getValue()));
            patient.setWeight(new Patient.Weight((Integer) spinnerPatientWeight.getValue()));
        } catch (InvalidInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Patient error");
            alert.setHeaderText("Change height or weight");
            alert.setContentText("Height must be between 50 and 250 cm\nWeight must be between 5 and 500 kg");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }

        patient.setId(dao.determinePatientId());
        patient.setHeight(new Patient.Height((Integer) spinnerPatientHeight.getValue()));
        patient.setWeight(new Patient.Weight((Integer) spinnerPatientWeight.getValue()));
        patient.setFirstName(textFieldPatientFirstName.getText());
        patient.setLastName(textFieldPatientLastName.getText());
        patient.setHomeAddress(textFieldPatientHomeAddress.getText());
        patient.setBloodType(choiceBoxPatientBloodType.getSelectionModel().getSelectedItem());

        dao.addPatient(patient);

        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) textFieldPatientFirstName.getScene().getWindow();
        stage.close();
    }
}
