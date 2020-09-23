package ba.unsa.etf.rpr.projekat;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MedicalMajorController {

    public TextField fieldMedicalMajorName;
    private String buttonText;
    private MedicalMajor medicalMajor;
    private HospitalDAO dao;

    public MedicalMajorController(HospitalDAO dao) {
        medicalMajor = new MedicalMajor();
        this.dao = dao;
        buttonText = "cancel";
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
        if(dao.isMedicalMajorNameDuplicate(fieldMedicalMajorName.getText()) || fieldMedicalMajorName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Medical major name error");
            alert.setHeaderText("Change medical major name");
            alert.setContentText(fieldMedicalMajorName.getText().isEmpty() ? "Medical major name can't be empty" : fieldMedicalMajorName.getText() + " already exists");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        medicalMajor.setId(dao.determineMedicalMajorId());
        medicalMajor.setMedicalMajorName(fieldMedicalMajorName.getText());
        dao.addMedicalMajor(medicalMajor);
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) fieldMedicalMajorName.getScene().getWindow();
        stage.close();
    }
}
