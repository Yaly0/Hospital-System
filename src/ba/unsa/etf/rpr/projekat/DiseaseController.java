package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DiseaseController {

    public TextField fieldDiseaseName;
    public ListView<Treatment> listViewDiseaseTreatments;
    public ChoiceBox<Treatment> choiceBoxDiseaseTreatment;
    public ChoiceBox<MedicalMajor> choiceBoxDiseaseMeidcalMajor;
    private String buttonText;
    private Disease disease;
    private HospitalDAO dao;
    private ObservableList<Treatment> allTreatments;
    private ObservableList<Treatment> treatments;

    public DiseaseController(HospitalDAO dao) {
        disease = new Disease();
        this.dao = dao;
        buttonText = "cancel";
        allTreatments = FXCollections.observableArrayList(dao.treatments());
        treatments = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        choiceBoxDiseaseMeidcalMajor.setItems(FXCollections.observableArrayList(dao.medicalMajors()));
        choiceBoxDiseaseTreatment.setItems(allTreatments);
        choiceBoxDiseaseTreatment.getSelectionModel().selectedItemProperty().addListener((obs, oldTreatment, newTreatment) -> {
            treatments.addAll(newTreatment);
            listViewDiseaseTreatments.setItems(treatments);
            allTreatments.removeAll(treatments);
        });
    }

    public String getButtonText() {
        return buttonText;
    }

    public Disease getDisease() {
        return disease;
    }

    public void deleteDiseaseAction() {
        Treatment treatment = listViewDiseaseTreatments.getSelectionModel().getSelectedItem();
        if (treatment == null) return;
        allTreatments.addAll(treatment);
        treatments.removeAll(treatment);
        listViewDiseaseTreatments.setItems(treatments);
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (dao.isDiseaseNameDuplicate(fieldDiseaseName.getText()) || fieldDiseaseName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Treatment name error");
            alert.setHeaderText("Change disease name");
            alert.setContentText(fieldDiseaseName.getText().isEmpty() ? "Disease name can't be empty" : fieldDiseaseName.getText() + " already exists");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        if (choiceBoxDiseaseMeidcalMajor.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Medical major error");
            alert.setHeaderText("Choose medical major");
            alert.setContentText("Medical major must be chosen");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        disease.setId(dao.determineDiseaseId());
        disease.setDiseaseName(fieldDiseaseName.getText());
        disease.setMedicalMajor(choiceBoxDiseaseMeidcalMajor.getSelectionModel().getSelectedItem());
        dao.addDisease(disease);
        dao.addDiseaseTreatments(disease, treatments);
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) fieldDiseaseName.getScene().getWindow();
        stage.close();
    }
}
