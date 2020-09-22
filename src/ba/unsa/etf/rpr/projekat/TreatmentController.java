package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class TreatmentController {

    public TextField fieldTreatmentName;
    public ListView<Disease> listViewTreatmentDiseases;
    public ChoiceBox<Disease> choiceBoxTreatmentDisease;
    private String buttonText;
    private Treatment treatment;
    private HospitalDAO dao;
    private ObservableList<Disease> allDiseases;
    private ObservableList<Disease> diseases;

    public TreatmentController(HospitalDAO dao) {
        treatment = new Treatment();
        this.dao = dao;
        buttonText = "cancel";
        allDiseases = FXCollections.observableArrayList(dao.diseases());
        diseases = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        choiceBoxTreatmentDisease.setItems(allDiseases);
        choiceBoxTreatmentDisease.getSelectionModel().selectedItemProperty().addListener((obs, oldDisease, newDisease) -> {
            diseases.addAll(newDisease);
            listViewTreatmentDiseases.setItems(diseases);
            allDiseases.removeAll(diseases);
        });
    }

    public String getButtonText() {
        return buttonText;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void deleteDiseaseAction() {
        Disease disease = listViewTreatmentDiseases.getSelectionModel().getSelectedItem();
        if (disease == null) return;
        allDiseases.addAll(disease);
        diseases.removeAll(disease);
        listViewTreatmentDiseases.setItems(diseases);
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (dao.isTreatmentNameDuplicate(fieldTreatmentName.getText()) || fieldTreatmentName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Treatment name error");
            alert.setHeaderText("Change treatment name");
            alert.setContentText(fieldTreatmentName.getText().isEmpty() ? "Treatment name can't be empty" : "Treatment name already exists");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        treatment.setId(dao.determineTreatmentId());
        treatment.setTreatmentName(fieldTreatmentName.getText());
        dao.addTreatment(treatment);
        dao.addTreatmentDiseases(treatment, diseases);
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) fieldTreatmentName.getScene().getWindow();
        stage.close();
    }
}
