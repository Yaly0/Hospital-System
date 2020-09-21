package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    private HospitalDAO dao;
    private ObservableList<Appointment> listAppointments;
    private ObservableList<Patient> listPatients;
    private ObservableList<Doctor> listDoctors;
    private ObservableList<Treatment> listTreatments;
    private ObservableList<MedicalMajor> listMedicalMajors;
    private ObservableList<Disease> listDiseases;

    public MainController() {
        dao = HospitalDAO.getInstance();
        //listAppointments = FXCollections.observableArrayList(dao.appointments());
        listPatients = FXCollections.observableArrayList(dao.patients());
    }

    @FXML
    public void initialize() {
        tableViewPatients.setItems(listPatients);
        columnPatientsId.setCellValueFactory(new PropertyValueFactory("id"));
        columnPatientsFirstName.setCellValueFactory(new PropertyValueFactory("firstName"));
        columnPatientsLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        columnPatientsBirthDate.setCellValueFactory(new PropertyValueFactory("birthDate"));
        columnPatientsCitizenNumber.setCellValueFactory(new PropertyValueFactory("citizenNumber"));
        columnPatientsPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
    }
}
