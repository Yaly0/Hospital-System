package ba.unsa.etf.rpr.projekat;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MedicalMajorController {

    public TextField fieldMedicalMajor;
    private String buttonText;
    private MedicalMajor medicalMajor;
    private HospitalDAO dao;

    public MedicalMajorController(HospitalDAO dao) {
        medicalMajor = new MedicalMajor();
        this.dao = dao;
    }

    public String getButtonText() {
        return buttonText;
    }

    public MedicalMajor getMedicalMajor() {
        return medicalMajor;
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if(dao.isNameDuplicate(fieldMedicalMajor.getText()) || fieldMedicalMajor.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Medical major name error");
            alert.setHeaderText("Change medical major name");
            alert.setContentText(fieldMedicalMajor.getText().isEmpty() ? "Medical major name can't be empty" : "Medical major name already exists");
            alert.showAndWait();
            return;
        }
        medicalMajor.setId(dao.determineMedicalMajorId());
        medicalMajor.setMedicalMajorName(fieldMedicalMajor.getText());
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) fieldMedicalMajor.getScene().getWindow();
        stage.close();
    }
}
