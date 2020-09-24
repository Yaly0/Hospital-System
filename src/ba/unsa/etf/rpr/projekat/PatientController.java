package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Person.Gender.*;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PatientController {

    public TextField textFieldPatientFirstName;
    public TextField textFieldPatientLastName;
    public TextField textFieldPatientHomeAddress;
    public DatePicker datePickerPatientBirthDate;
    public TextField textFieldPatientCitizen;
    public TextField textFieldPatientEmailAddress;
    public RadioButton radioButtonPatientMaleButton;
    public RadioButton radioButtonPatientFemaleButton;
    public ToggleGroup toggleGroupGender;
    public ChoiceBox<BloodType> choiceBoxPatientBloodType;
    public TextField textFieldPatientPhoneNumber;
    public Spinner spinnerPatientHeight;
    public Spinner spinnerPatientWeight;
    public Label labelPatientBMI;

    private boolean edit;
    public Label labelPatientAppointments;
    public TableView<Appointment> tableViewPatientAppointments;
    public TableColumn columnPatientAppointmentDisease;
    public TableColumn columnPatientAppointmentDoctor;
    public TableColumn columnPatientAppointmentDate;
    public TableColumn columnPatientAppointmentTime;
    private String buttonText;
    private Patient patient;
    private HospitalDAO dao;

    public PatientController(HospitalDAO dao, Patient patient) {
        if (patient != null) {
            this.patient = patient;
            edit = true;
        } else {
            this.patient = new Patient();
            edit = false;
        }
        this.dao = dao;
        buttonText = "cancel";
    }

    @FXML
    public void initialize() {
        AppointmentController.datePickerConverter(datePickerPatientBirthDate);
        choiceBoxPatientBloodType.setItems(FXCollections.observableArrayList(BloodType.values()));
        if (edit) {
            labelPatientBMI.setDisable(false);
            labelPatientAppointments.setDisable(false);
            tableViewPatientAppointments.setDisable(false);
            textFieldPatientFirstName.setText(patient.getFirstName());
            textFieldPatientLastName.setText(patient.getLastName());
            textFieldPatientHomeAddress.setText(patient.getHomeAddress());
            datePickerPatientBirthDate.setValue(patient.getBirthDate());
            textFieldPatientCitizen.setText(patient.getCitizenNumber().toString());
            textFieldPatientEmailAddress.setText(patient.getEmailAddress().toString());
            if (patient.getGender().toString().equals("Male"))
                radioButtonPatientMaleButton.setSelected(true);
            else radioButtonPatientFemaleButton.setSelected(true);
            choiceBoxPatientBloodType.getSelectionModel().select(patient.getBloodType());
            textFieldPatientPhoneNumber.setText(patient.getPhoneNumber().toString());
            spinnerPatientHeight.getValueFactory().setValue(patient.getHeight().getHeight());
            spinnerPatientWeight.getValueFactory().setValue(patient.getWeight().getWeight());

            labelPatientBMI.setText(String.valueOf(patient.getBMI()));
            tableViewPatientAppointments.setItems(FXCollections.observableArrayList(dao.getAppointmentsFromPatient(patient)));
            columnPatientAppointmentDisease.setCellValueFactory(new PropertyValueFactory("disease"));
            columnPatientAppointmentDoctor.setCellValueFactory(new PropertyValueFactory("doctor"));
            columnPatientAppointmentDate.setCellValueFactory(new PropertyValueFactory("appointmentDate"));
            columnPatientAppointmentTime.setCellValueFactory(new PropertyValueFactory("appointmentTime"));
            tableViewPatientAppointments.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                openAppointmentAction();
            }
        });
        } else {
            spinnerPatientHeight.getValueFactory().setValue(170);
            spinnerPatientWeight.getValueFactory().setValue(70);
        }
    }

    private void openAppointmentAction() {
        Appointment appointment = tableViewPatientAppointments.getSelectionModel().getSelectedItem();
        if(appointment == null) return;
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"));
            AppointmentController appointmentController = new AppointmentController(dao, appointment);
            loader.setController(appointmentController);
            loader.load();

            stage.setTitle("Appointment");
            stage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setMaxWidth(400);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setOnHiding(event -> {
                tableViewPatientAppointments.setItems(FXCollections.observableArrayList(dao.getAppointmentsFromPatient(patient)));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getButtonText() {
        return buttonText;
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
        patient.setHeight(new Patient.Height((Integer) spinnerPatientHeight.getValue()));
        patient.setWeight(new Patient.Weight((Integer) spinnerPatientWeight.getValue()));
        patient.setFirstName(textFieldPatientFirstName.getText());
        patient.setLastName(textFieldPatientLastName.getText());
        patient.setHomeAddress(textFieldPatientHomeAddress.getText());
        patient.setBloodType(choiceBoxPatientBloodType.getSelectionModel().getSelectedItem());
        if (edit) {
            dao.updatePatient(patient);
        } else {
            patient.setId(dao.determinePatientId());
            dao.addPatient(patient);
        }
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) textFieldPatientFirstName.getScene().getWindow();
        stage.close();
    }
}
