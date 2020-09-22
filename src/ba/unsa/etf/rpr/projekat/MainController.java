package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    public TableView<Appointment> tableViewAppointments;
    public TableView<Patient> tableViewPatients;
    public TableView<Doctor> tableViewDoctors;
    public TableView<Treatment> tableViewTreatments;
    public TableView<MedicalMajor> tableViewMedicalMajors;
    public TableView<Disease> tableViewDiseases;

    public TableColumn columnAppointmentsId;
    public TableColumn columnAppointmentsPatient;
    public TableColumn columnAppointmentsDoctor;
    public TableColumn columnAppointmentsDiseases;
    public TableColumn columnAppointmentsDate;
    public TableColumn columnAppointmentsTime;
    public TableColumn columnPatientsId;
    public TableColumn columnPatientsFirstName;
    public TableColumn columnPatientsLastName;
    public TableColumn columnPatientsBirthDate;
    public TableColumn columnPatientsCitizenNumber;
    public TableColumn columnPatientsPhoneNumber;
    public TableColumn columnDoctorsId;
    public TableColumn columnDoctorsFirstName;
    public TableColumn columnDoctorsLastName;
    public TableColumn columnDoctorsBirthDate;
    public TableColumn columnDoctorsMedicalMajor;
    public TableColumn columnDoctorsPhoneNumber;
    public TableColumn columnTreatmentsId;
    public TableColumn columnTreatmentsName;
    public TableColumn columnMedicalMajorsId;
    public TableColumn columnMedicalMajorsName;
    public TableColumn columnDiseasesId;
    public TableColumn columnDiseasesName;
    public TableColumn columnDiseasesMedicalMajor;

    public TextField fieldSearchAppointment;
    public TextField fieldSearchPatient;
    public TextField fieldSearchDoctor;
    public TextField fieldSearchTreatment;
    public TextField fieldSearchMedicalMajor;
    public TextField fieldSearchDisease;

    private HospitalDAO dao;
    private ObservableList<Appointment> listAppointments;
    private ObservableList<Patient> listPatients;
    private ObservableList<Doctor> listDoctors;
    private ObservableList<Treatment> listTreatments;
    private ObservableList<MedicalMajor> listMedicalMajors;
    private ObservableList<Disease> listDiseases;

    public MainController() {
        dao = HospitalDAO.getInstance();
        listAppointments = FXCollections.observableArrayList(dao.appointments());
        listPatients = FXCollections.observableArrayList(dao.patients());
        listDoctors = FXCollections.observableArrayList(dao.doctors());
        listTreatments = FXCollections.observableArrayList(dao.treatments());
        listMedicalMajors = FXCollections.observableArrayList(dao.medicalMajors());
        listDiseases = FXCollections.observableArrayList(dao.diseases());
    }

    @FXML
    public void initialize() {
        tableViewAppointments.setItems(listAppointments);
        columnAppointmentsId.setCellValueFactory(new PropertyValueFactory("id"));
        columnAppointmentsPatient.setCellValueFactory(new PropertyValueFactory("patient"));
        columnAppointmentsDoctor.setCellValueFactory(new PropertyValueFactory("doctor"));
        columnAppointmentsDiseases.setCellValueFactory(new PropertyValueFactory("disease"));
        columnAppointmentsDate.setCellValueFactory(new PropertyValueFactory("appointmentDate"));
        columnAppointmentsTime.setCellValueFactory(new PropertyValueFactory("appointmentTime"));

        tableViewPatients.setItems(listPatients);
        columnPatientsId.setCellValueFactory(new PropertyValueFactory("id"));
        columnPatientsFirstName.setCellValueFactory(new PropertyValueFactory("firstName"));
        columnPatientsLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        columnPatientsBirthDate.setCellValueFactory(new PropertyValueFactory("birthDate"));
        columnPatientsCitizenNumber.setCellValueFactory(new PropertyValueFactory("citizenNumber"));
        columnPatientsPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));

        tableViewDoctors.setItems(listDoctors);
        columnDoctorsId.setCellValueFactory(new PropertyValueFactory("id"));
        columnDoctorsFirstName.setCellValueFactory(new PropertyValueFactory("firstName"));
        columnDoctorsLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        columnDoctorsBirthDate.setCellValueFactory(new PropertyValueFactory("birthDate"));
        columnDoctorsMedicalMajor.setCellValueFactory(new PropertyValueFactory("medicalMajor"));
        columnDoctorsPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));

        tableViewTreatments.setItems(listTreatments);
        columnTreatmentsId.setCellValueFactory(new PropertyValueFactory("id"));
        columnTreatmentsName.setCellValueFactory(new PropertyValueFactory("treatmentName"));

        tableViewMedicalMajors.setItems(listMedicalMajors);
        columnMedicalMajorsId.setCellValueFactory(new PropertyValueFactory("id"));
        columnMedicalMajorsName.setCellValueFactory(new PropertyValueFactory("medicalMajorName"));

        tableViewDiseases.setItems(listDiseases);
        columnDiseasesId.setCellValueFactory(new PropertyValueFactory("id"));
        columnDiseasesName.setCellValueFactory(new PropertyValueFactory("diseaseName"));
        ;
        columnDiseasesMedicalMajor.setCellValueFactory(new PropertyValueFactory("medicalMajor"));

        fieldSearchAppointment.textProperty().addListener((obs, oldSearch, newSearch) -> {
            if (newSearch.isEmpty()) {
                tableViewAppointments.setItems(listAppointments);
            } else {
                ObservableList<Appointment> searched = FXCollections.observableArrayList();
                for (Appointment i : listAppointments) {
                    if (i.getDoctor().getFirstName().toLowerCase().contains(fieldSearchAppointment.getText().toLowerCase()) ||
                            i.getDoctor().getLastName().toLowerCase().contains(fieldSearchAppointment.getText().toLowerCase()) ||
                            i.getPatient().getFirstName().toLowerCase().contains(fieldSearchAppointment.getText().toLowerCase()) ||
                            i.getPatient().getLastName().toLowerCase().contains(fieldSearchAppointment.getText().toLowerCase())
                    )
                        searched.add(i);
                }
                tableViewAppointments.setItems(searched);
            }
        });
        fieldSearchPatient.textProperty().addListener((obs, oldSearch, newSearch) -> {
            if (newSearch.isEmpty()) {
                tableViewPatients.setItems(listPatients);
            } else {
                ObservableList<Patient> searched = FXCollections.observableArrayList();
                for (Patient i : listPatients) {
                    if (i.getFirstName().toLowerCase().contains(fieldSearchPatient.getText().toLowerCase()) ||
                            i.getLastName().toLowerCase().contains(fieldSearchPatient.getText().toLowerCase())
                    )
                        searched.add(i);
                }
                tableViewPatients.setItems(searched);
            }
        });
        fieldSearchDoctor.textProperty().addListener((obs, oldSearch, newSearch) -> {
            if (newSearch.isEmpty()) {
                tableViewDoctors.setItems(listDoctors);
            } else {
                ObservableList<Doctor> searched = FXCollections.observableArrayList();
                for (Doctor i : listDoctors) {
                    if (i.getFirstName().toLowerCase().contains(fieldSearchDoctor.getText().toLowerCase()) ||
                            i.getLastName().toLowerCase().contains(fieldSearchDoctor.getText().toLowerCase())
                    )
                        searched.add(i);
                }
                tableViewDoctors.setItems(searched);
            }
        });
        fieldSearchTreatment.textProperty().addListener((obs, oldSearch, newSearch) -> {
            if (newSearch.isEmpty()) {
                tableViewTreatments.setItems(listTreatments);
            } else {
                ObservableList<Treatment> searched = FXCollections.observableArrayList();
                for (Treatment i : listTreatments) {
                    if (i.getTreatmentName().toLowerCase().contains(fieldSearchTreatment.getText().toLowerCase()))
                        searched.add(i);
                }
                tableViewTreatments.setItems(searched);
            }
        });
        fieldSearchMedicalMajor.textProperty().addListener((obs, oldSearch, newSearch) -> {
            if (newSearch.isEmpty()) {
                tableViewMedicalMajors.setItems(listMedicalMajors);
            } else {
                ObservableList<MedicalMajor> searched = FXCollections.observableArrayList();
                for (MedicalMajor i : listMedicalMajors) {
                    if (i.getMedicalMajorName().toLowerCase().contains(fieldSearchMedicalMajor.getText().toLowerCase()))
                        searched.add(i);
                }
                tableViewMedicalMajors.setItems(searched);
            }
        });
        fieldSearchDisease.textProperty().addListener((obs, oldSearch, newSearch) -> {
            if (newSearch.isEmpty()) {
                tableViewDiseases.setItems(listDiseases);
            } else {
                ObservableList<Disease> searched = FXCollections.observableArrayList();
                for (Disease i : listDiseases) {
                    if (i.getDiseaseName().toLowerCase().contains(fieldSearchDisease.getText().toLowerCase()))
                        searched.add(i);
                }
                tableViewDiseases.setItems(searched);
            }
        });

    }
}
