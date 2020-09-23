package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TreatmentController {
    public TextField textFieldTreatmentName;
    public ListView<Disease> listViewTreatmentDiseases;
    public ComboBox<Disease> comboBoxTreatmentDisease;
    private String buttonText;
    private boolean edit;
    private String initialTreatmentName;
    private Treatment treatment;
    private HospitalDAO dao;
    private ArrayList<Disease> allDiseases;
    private ArrayList<Disease> diseases;
    private ArrayList<Disease> initialDiseases;

    public TreatmentController(HospitalDAO dao, Treatment treatment) {
        if (treatment != null) {
            this.treatment = treatment;
            edit = true;
        } else {
            this.treatment = new Treatment();
            edit = false;
        }
        this.dao = dao;
        buttonText = "cancel";
        allDiseases = new ArrayList<>(dao.diseases());
        diseases = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        if (edit) {
            initialDiseases = dao.getDiseasesFromTreatment(treatment);
            ArrayList<Disease> otherDiseases = new ArrayList<>();
            for (Disease d : allDiseases) if (!initialDiseases.contains(d)) otherDiseases.add(d);
            allDiseases = new ArrayList<>(otherDiseases);
            diseases = new ArrayList<>(initialDiseases);
            listViewTreatmentDiseases.setItems(FXCollections.observableArrayList(initialDiseases));
            initialTreatmentName = treatment.getTreatmentName();
            textFieldTreatmentName.setText(initialTreatmentName);
        }
        comboBoxTreatmentDisease.setItems(FXCollections.observableArrayList(allDiseases));
        comboBoxTreatmentDisease.getSelectionModel().selectedItemProperty().addListener((obs, oldDisease, newDisease) -> {
            if (newDisease != null) {
                diseases.add(newDisease);
                allDiseases.removeAll(diseases);
                listViewTreatmentDiseases.setItems(FXCollections.observableArrayList(diseases));
                Platform.runLater(() -> comboBoxTreatmentDisease.setItems(FXCollections.observableArrayList(allDiseases)));
            }
        });
    }

    public String getButtonText() {
        return buttonText;
    }

    public void deleteDiseaseAction() {
        Disease disease = listViewTreatmentDiseases.getSelectionModel().getSelectedItem();
        if (disease == null) return;
        allDiseases.add(disease);
        diseases.remove(disease);
        listViewTreatmentDiseases.setItems(FXCollections.observableArrayList(diseases));
        comboBoxTreatmentDisease.setItems(FXCollections.observableArrayList(allDiseases));
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (textFieldTreatmentName.getText().trim().isEmpty() || (!textFieldTreatmentName.getText().equals(initialTreatmentName) && dao.isTreatmentNameDuplicate(textFieldTreatmentName.getText()))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Treatment name error");
            alert.setHeaderText("Change treatment name");
            alert.setContentText(textFieldTreatmentName.getText().isEmpty() ? "Treatment name can't be empty" : textFieldTreatmentName.getText() + " already exists");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        treatment.setTreatmentName(textFieldTreatmentName.getText());
        if (edit) {
            ArrayList<Disease> diseasesForDelete = new ArrayList<>(), diseasesForAdd = new ArrayList<>();
            for (Disease d : initialDiseases) if (!diseases.contains(d)) diseasesForDelete.add(d);
            for (Disease d : diseases) if (!initialDiseases.contains(d)) diseasesForAdd.add(d);
            dao.updateTreatmentDiseases(treatment, diseasesForDelete, diseasesForAdd);
            dao.updateTreatment(treatment);
        } else {
            treatment.setId(dao.determineTreatmentId());
            dao.addTreatment(treatment);
            dao.addTreatmentDiseases(treatment, diseases);
        }
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) textFieldTreatmentName.getScene().getWindow();
        stage.close();
    }
}
