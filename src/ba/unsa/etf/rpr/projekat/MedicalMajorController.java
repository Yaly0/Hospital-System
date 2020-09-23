package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MedicalMajorController {

    public TextField textFieldMedicalMajorName;
    public ListView listViewMedicalMajorDoctors;
    public ListView listViewMedicalMajorDiseases;
    public Label labelMedicalMajorDoctors;
    public Label labelMedicalMajorDiseases;
    private boolean edit;
    private String initialMedicalMajorName;
    private String buttonText;
    private MedicalMajor medicalMajor;
    private HospitalDAO dao;

    public MedicalMajorController(HospitalDAO dao, MedicalMajor medicalMajor) {
        if (medicalMajor != null) {
            this.medicalMajor = medicalMajor;
            edit = true;
        } else {
            this.medicalMajor = new MedicalMajor();
            edit = false;
        }
        this.dao = dao;
        buttonText = "cancel";
    }

    @FXML
    public void initialize() {
        if (edit) {
            labelMedicalMajorDoctors.setDisable(false);
            labelMedicalMajorDiseases.setDisable(false);
            listViewMedicalMajorDoctors.setDisable(false);
            listViewMedicalMajorDiseases.setDisable(false);
            textFieldMedicalMajorName.setText(medicalMajor.getMedicalMajorName());
            listViewMedicalMajorDoctors.setItems(FXCollections.observableArrayList(dao.getDoctorsFromMedicalMajor(medicalMajor)));
            listViewMedicalMajorDiseases.setItems(FXCollections.observableArrayList(dao.getDiseasesFromMedicalMajor(medicalMajor)));
            initialMedicalMajorName = medicalMajor.getMedicalMajorName();
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
        if(textFieldMedicalMajorName.getText().trim().isEmpty() || (!textFieldMedicalMajorName.getText().equals(initialMedicalMajorName)  && dao.isMedicalMajorNameDuplicate(textFieldMedicalMajorName.getText()))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Medical major name error");
            alert.setHeaderText("Change medical major name");
            alert.setContentText(textFieldMedicalMajorName.getText().isEmpty() ? "Medical major name can't be empty" : textFieldMedicalMajorName.getText() + " already exists");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        medicalMajor.setMedicalMajorName(textFieldMedicalMajorName.getText());
        if (edit) {
            dao.updateMedicalMajor(medicalMajor);
        } else {
            medicalMajor.setId(dao.determineMedicalMajorId());
            medicalMajor.setMedicalMajorName(textFieldMedicalMajorName.getText());
            dao.addMedicalMajor(medicalMajor);
        }
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) textFieldMedicalMajorName.getScene().getWindow();
        stage.close();
    }
}
