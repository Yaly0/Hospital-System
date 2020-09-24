package ba.unsa.etf.rpr.projekat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DiseaseController {

    public TextField textFieldDiseaseName;
    public ListView<Treatment> listViewDiseaseTreatments;
    public ComboBox<Treatment> comboBoxDiseaseTreatment;
    public ComboBox<MedicalMajor> comboBoxDiseaseMedicalMajor;
    private String buttonText;
    private boolean edit;
    private String initialDiseaseName;
    private Disease disease;
    private HospitalDAO dao;
    private ArrayList<Treatment> allTreatments;
    private ArrayList<Treatment> treatments;
    private ArrayList<Treatment> initialTreatments;
    private MedicalMajor initialMedicalMajor;

    public DiseaseController(HospitalDAO dao, Disease disease) {
        if (disease != null) {
            this.disease = disease;
            edit = true;
        } else {
            this.disease = new Disease();
            edit = false;
        }
        this.dao = dao;
        buttonText = "cancel";
        allTreatments = new ArrayList<>(dao.treatments());
        treatments = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        comboBoxDiseaseMedicalMajor.setItems(FXCollections.observableArrayList(dao.medicalMajors()));
        if (edit) {
            initialTreatments = dao.getTreatmentsFromDisease(disease);
            ArrayList<Treatment> otherTreatments = new ArrayList<>();
            for (Treatment t : allTreatments) if (!initialTreatments.contains(t)) otherTreatments.add(t);
            allTreatments = new ArrayList<>(otherTreatments);
            treatments = new ArrayList<>(initialTreatments);
            listViewDiseaseTreatments.setItems(FXCollections.observableArrayList(initialTreatments));
            initialDiseaseName = disease.getDiseaseName();
            textFieldDiseaseName.setText(initialDiseaseName);
            comboBoxDiseaseMedicalMajor.getSelectionModel().select(disease.getMedicalMajor());
            initialMedicalMajor = new MedicalMajor(disease.getMedicalMajor().getId(), disease.getMedicalMajor().getMedicalMajorName());
        }
        comboBoxDiseaseTreatment.setItems(FXCollections.observableArrayList(allTreatments));
        comboBoxDiseaseTreatment.getSelectionModel().selectedItemProperty().addListener((obs, oldTreatment, newTreatment) -> {
            if (newTreatment != null) {
                treatments.add(newTreatment);
                allTreatments.removeAll(treatments);
                listViewDiseaseTreatments.setItems(FXCollections.observableArrayList(treatments));
                Platform.runLater(() -> comboBoxDiseaseTreatment.setItems(FXCollections.observableArrayList(allTreatments)));
            }
        });
    }

    public String getButtonText() {
        return buttonText;
    }

    public void deleteTreatmentAction() {
        Treatment treatment = listViewDiseaseTreatments.getSelectionModel().getSelectedItem();
        if (treatment == null) return;
        allTreatments.add(treatment);
        treatments.remove(treatment);
        listViewDiseaseTreatments.setItems(FXCollections.observableArrayList(treatments));
        comboBoxDiseaseTreatment.setItems(FXCollections.observableArrayList(allTreatments));
    }

    public void cancelAction() {
        buttonText = "cancel";
        closeWindows();
    }

    public void okAction() {
        buttonText = "ok";
        if (textFieldDiseaseName.getText().trim().isEmpty() || (!textFieldDiseaseName.getText().equals(initialDiseaseName) && dao.isDiseaseNameDuplicate(textFieldDiseaseName.getText()))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Treatment name error");
            alert.setHeaderText("Change disease name");
            alert.setContentText(textFieldDiseaseName.getText().trim().isEmpty() ? "Disease name can't be empty" : textFieldDiseaseName.getText() + " already exists");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        if (comboBoxDiseaseMedicalMajor.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Medical major error");
            alert.setHeaderText("Choose medical major");
            alert.setContentText("Medical major must be chosen");
            alert.showAndWait();
            buttonText = "cancel";
            return;
        }
        disease.setDiseaseName(textFieldDiseaseName.getText());
        disease.setMedicalMajor(comboBoxDiseaseMedicalMajor.getSelectionModel().getSelectedItem());
        if (edit) {
            ArrayList<Treatment> treatmentsForDelete = new ArrayList<>(), treatmentsForAdd = new ArrayList<>();
            for (Treatment t : initialTreatments) if (!treatments.contains(t)) treatmentsForDelete.add(t);
            for (Treatment t : treatments) if (!initialTreatments.contains(t)) treatmentsForAdd.add(t);
            dao.updateDiseaseTreatments(disease, treatmentsForDelete, treatmentsForAdd);
            dao.updateDisease(disease);
            if (!initialMedicalMajor.equals(disease.getMedicalMajor())) {
                dao.updateDiseaseAppointments(disease);
            }
        } else {
            disease.setId(dao.determineDiseaseId());
            dao.addDisease(disease);
            dao.addDiseaseTreatments(disease, treatments);
        }
        closeWindows();
    }

    private void closeWindows() {
        Stage stage = (Stage) textFieldDiseaseName.getScene().getWindow();
        stage.close();
    }
}
