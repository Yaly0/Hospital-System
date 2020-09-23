package ba.unsa.etf.rpr.projekat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Patient.*;
import static ba.unsa.etf.rpr.projekat.Doctor.*;

public class HospitalDAO {
    private static HospitalDAO instance = null;
    private Connection connection;

    private PreparedStatement getAppointmentsStatement, getPatientsStatement, getDoctorsStatement, getTreatmentsStatement,
            getMedicalMajorsStatement, getDiseasesStatement, getMedicalMajorStatement, getPatientStatement, getDoctorStatement,
            getDiseaseStatement, removeTreatmentFromAppointmentStatement, removeTreatmentFromDiseaseStatement, removeTreatmentStatement,
            removeAppointmentFromTreatmentStatement, removeAppointmentStatement, removePatientFromAppointmentStatement,
            removePatientStatement, removeDoctorFromAppointmentStatement, removeDoctorStatement, getDiseasesFromMedicalMajorStatement,
            removeMedicalMajorFromDoctorStatement, removeMedicalMajorStatement, removeDiseaseFromAppointmentStatement, removeDiseaseStatement,
            removeDiseaseFromAppointmentTreatmentStatement, removeDiseaseFromTreatmentStatement, determineMedicalMajorIdStatement,
            addMedicalMajorStatement, determineTreatmentIdStatement, addTreatmentStatement, addDiseaseTreatmentStatement,
            determineDiseaseIdStatement, addDiseaseStatement, getMedicalMajorFromDoctorStatement, getDoctorFromMedicalMajorStatement,
            determineAppointmentIdStatement, addAppointmentStatement;

    private HospitalDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:hospital.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            getPatientsStatement = connection.prepareStatement("SELECT * FROM patient");
        } catch (SQLException e1) {
            regenerateBase();
            try {
                getPatientsStatement = connection.prepareStatement("SELECT * FROM patient");
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        try {
            getAppointmentsStatement = connection.prepareStatement("SELECT * FROM appointment");
            getDoctorsStatement = connection.prepareStatement("SELECT * FROM doctor");
            getTreatmentsStatement = connection.prepareStatement("SELECT * FROM treatment");
            getMedicalMajorsStatement = connection.prepareStatement("SELECT * FROM medical_major");
            getDiseasesStatement = connection.prepareStatement("SELECT * FROM disease");

            getPatientStatement = connection.prepareStatement("SELECT * FROM patient WHERE id = ?");
            getDoctorStatement = connection.prepareStatement("SELECT * FROM doctor WHERE id = ?");
            getDiseaseStatement = connection.prepareStatement("SELECT * FROM disease WHERE id = ?");
            getMedicalMajorStatement = connection.prepareStatement("SELECT major_name FROM medical_major WHERE id = ?");
            getDiseasesFromMedicalMajorStatement = connection.prepareStatement("SELECT * FROM disease WHERE medical_major_id = ?");
            getMedicalMajorFromDoctorStatement = connection.prepareStatement("SELECT medical_major_id FROM doctor WHERE id = ?");
            getDoctorFromMedicalMajorStatement = connection.prepareStatement("SELECT * FROM doctor WHERE medical_major_id = ?");

            removeTreatmentFromAppointmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE treatment_id = ?");
            removeTreatmentFromDiseaseStatement = connection.prepareStatement("DELETE FROM disease_treatment WHERE treatment_id = ?");
            removeTreatmentStatement = connection.prepareStatement("DELETE FROM treatment WHERE id = ?");
            removeAppointmentFromTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE appointment_id = ?");
            removeAppointmentStatement = connection.prepareStatement("DELETE FROM appointment WHERE id = ?");
            removePatientFromAppointmentStatement = connection.prepareStatement("DELETE FROM appointment WHERE patient_id = ?");
            removePatientStatement = connection.prepareStatement("DELETE FROM patient WHERE id = ?");
            removeDoctorFromAppointmentStatement = connection.prepareStatement("DELETE FROM appointment WHERE doctor_id = ?");
            removeDoctorStatement = connection.prepareStatement("DELETE FROM doctor WHERE id = ?");
            removeMedicalMajorFromDoctorStatement = connection.prepareStatement("DELETE FROM doctor WHERE medical_major_id = ?");
            removeMedicalMajorStatement = connection.prepareStatement("DELETE FROM medical_major WHERE id = ?");
            removeDiseaseFromAppointmentStatement = connection.prepareStatement("DELETE FROM appointment WHERE disease_id = ?");
            removeDiseaseFromAppointmentTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE disease_id = ?");
            removeDiseaseFromTreatmentStatement = connection.prepareStatement("DELETE FROM disease_treatment WHERE disease_id = ?");
            removeDiseaseStatement = connection.prepareStatement("DELETE FROM disease WHERE id = ?");

            determineMedicalMajorIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM medical_major");
            addMedicalMajorStatement = connection.prepareStatement("INSERT INTO medical_major VALUES (?,?)");
            determineTreatmentIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM treatment");
            addTreatmentStatement = connection.prepareStatement("INSERT INTO treatment VALUES (?,?)");
            addDiseaseTreatmentStatement = connection.prepareStatement("INSERT INTO disease_treatment VALUES (?,?)");
            determineDiseaseIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM disease");
            addDiseaseStatement = connection.prepareStatement("INSERT INTO disease VALUES (?,?,?)");
            determineAppointmentIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM appointment");
            addAppointmentStatement = connection.prepareStatement("INSERT INTO appointment VALUES (?,?,?,?,?,?,NULL,NULL)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerateBase() {
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileInputStream("hospital.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if (sqlUpit.length() > 1 && sqlUpit.charAt(sqlUpit.length() - 1) == ';') {
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static HospitalDAO getInstance() {
        if (instance == null) instance = new HospitalDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance != null) {
            try {
                instance.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }


    public ArrayList<Appointment> appointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        try {
            ResultSet rs = getAppointmentsStatement.executeQuery();
            while (rs.next()) {
                appointments.add(getAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    private Appointment getAppointmentFromResultSet(ResultSet rs) throws SQLException {

        ResultSet rs2;
        getPatientStatement.setInt(1, rs.getInt(2));
        rs2 = getPatientStatement.executeQuery();
        Patient patient = getPatientFromResultSet(rs2);

        getDoctorStatement.setInt(1, rs.getInt(3));
        rs2 = getDoctorStatement.executeQuery();
        Doctor doctor = getDoctorFromResultSet(rs2);

        getDiseaseStatement.setInt(1, rs.getInt(4));
        rs2 = getDiseaseStatement.executeQuery();
        Disease disease = getDiseaseFromResultSet(rs2);

        LocalDate date = LocalDate.of(
                Integer.valueOf(rs.getString(5).substring(0, 4)),
                Integer.valueOf(rs.getString(5).substring(5, 7)),
                Integer.valueOf(rs.getString(5).substring(8, 10)));

        LocalTime time = LocalTime.of(
                Integer.valueOf(rs.getString(6).substring(0, 2)),
                Integer.valueOf(rs.getString(6).substring(3, 5)));

        return new Appointment(rs.getInt(1), patient, doctor, disease, date, time);
    }

    public ArrayList<Patient> patients() {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            ResultSet rs = getPatientsStatement.executeQuery();
            while (rs.next()) {
                patients.add(getPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    private Patient getPatientFromResultSet(ResultSet rs) throws SQLException {
        LocalDate date = LocalDate.of(
                Integer.valueOf(rs.getString(5).substring(0, 4)),
                Integer.valueOf(rs.getString(5).substring(5, 7)),
                Integer.valueOf(rs.getString(5).substring(8, 10)));
        CitizenNumber citizenNumber = new CitizenNumber(rs.getString(6));
        PhoneNumber phoneNumber = new PhoneNumber(rs.getString(7));
        EmailAddress emailAddress = new EmailAddress(rs.getString(8));
        Gender gender = !(rs.getString(9).equals("Male")) ? !(rs.getString(9).equals("Female")) ? null : Gender.FEMALE : Gender.MALE;
        BloodType bloodType = BloodType.fromString(rs.getString(10));
        Height height = new Height(rs.getInt(11));
        Weight weight = new Weight(rs.getDouble(12));

        return new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), date, citizenNumber, phoneNumber, emailAddress, gender, bloodType, height, weight);
    }

    public ArrayList<Doctor> doctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        try {
            ResultSet rs = getDoctorsStatement.executeQuery();
            while (rs.next()) {
                doctors.add(getDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private Doctor getDoctorFromResultSet(ResultSet rs) throws SQLException {
        LocalDate date = LocalDate.of(
                Integer.valueOf(rs.getString(5).substring(0, 4)),
                Integer.valueOf(rs.getString(5).substring(5, 7)),
                Integer.valueOf(rs.getString(5).substring(8, 10)));
        CitizenNumber citizenNumber = new CitizenNumber(rs.getString(6));
        PhoneNumber phoneNumber = new PhoneNumber(rs.getString(7));
        EmailAddress emailAddress = new EmailAddress(rs.getString(8));
        Gender gender = !(rs.getString(9).equals("Male")) ? !(rs.getString(9).equals("Female")) ? null : Gender.FEMALE : Gender.MALE;
        BloodType bloodType = BloodType.fromString(rs.getString(10));

        getMedicalMajorStatement.setInt(1, rs.getInt(11));
        ResultSet rs2 = getMedicalMajorStatement.executeQuery();
        MedicalMajor medicalMajor = new MedicalMajor(rs.getInt(11), rs2.getString(1));

        String time = rs.getString(12);
        ShiftHours shiftHours = new ShiftHours(
                LocalTime.of(Integer.valueOf(time.substring(0, 2)), Integer.valueOf(time.substring(3, 5))),
                LocalTime.of(Integer.valueOf(time.substring(6, 8)), Integer.valueOf(time.substring(9, 11))),
                time.length() > 12 ? LocalTime.of(Integer.valueOf(time.substring(15, 17)), Integer.valueOf(time.substring(18, 20))) : LocalTime.of(12, 0),
                time.length() > 12 ? LocalTime.of(Integer.valueOf(time.substring(21, 23)), Integer.valueOf(time.substring(24, 26))) : LocalTime.of(12, 0));

        return new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), date, citizenNumber, phoneNumber, emailAddress, gender, bloodType, medicalMajor, shiftHours);
    }

    public ArrayList<Treatment> treatments() {
        ArrayList<Treatment> treatments = new ArrayList<>();
        try {
            ResultSet rs = getTreatmentsStatement.executeQuery();
            while (rs.next()) {
                treatments.add(new Treatment(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }

    public ArrayList<MedicalMajor> medicalMajors() {
        ArrayList<MedicalMajor> medicalMajors = new ArrayList<>();
        try {
            ResultSet rs = getMedicalMajorsStatement.executeQuery();
            while (rs.next()) {
                medicalMajors.add(new MedicalMajor(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicalMajors;
    }

    public ArrayList<Disease> diseases() {
        ArrayList<Disease> diseases = new ArrayList<>();
        try {
            ResultSet rs = getDiseasesStatement.executeQuery();
            while (rs.next()) {
                diseases.add(getDiseaseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diseases;
    }

    private Disease getDiseaseFromResultSet(ResultSet rs) throws SQLException {
        getMedicalMajorStatement.setInt(1, rs.getInt(3));
        ResultSet rs2 = getMedicalMajorStatement.executeQuery();
        MedicalMajor medicalMajor = new MedicalMajor(rs.getInt(3), rs2.getString(1));
        return new Disease(rs.getInt(1), rs.getString(2), medicalMajor);
    }

    public void removeTreatment(Treatment treatment) {
        try {
            removeTreatmentFromAppointmentStatement.setInt(1, treatment.getId());
            removeTreatmentFromAppointmentStatement.executeUpdate();
            removeTreatmentFromDiseaseStatement.setInt(1, treatment.getId());
            removeTreatmentFromDiseaseStatement.executeUpdate();
            removeTreatmentStatement.setInt(1, treatment.getId());
            removeTreatmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAppointment(Appointment appointment) {
        try {
            removeAppointmentFromTreatmentStatement.setInt(1, appointment.getId());
            removeAppointmentFromTreatmentStatement.executeUpdate();
            removeAppointmentStatement.setInt(1, appointment.getId());
            removeAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePatient(Patient patient) {
        try {
            removePatientFromAppointmentStatement.setInt(1, patient.getId());
            removePatientFromAppointmentStatement.executeUpdate();
            removePatientStatement.setInt(1, patient.getId());
            removePatientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDoctor(Doctor doctor) {
        try {
            removeDoctorFromAppointmentStatement.setInt(1, doctor.getId());
            removeDoctorFromAppointmentStatement.executeUpdate();
            removeDoctorStatement.setInt(1, doctor.getId());
            removeDoctorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMedicalMajor(MedicalMajor medicalMajor) {
        try {
            getDiseasesFromMedicalMajorStatement.setInt(1, medicalMajor.getId());
            ResultSet rs = getDiseasesFromMedicalMajorStatement.executeQuery();
            while (rs.next()) {
                removeDisease(getDiseaseFromResultSet(rs));
            }
            removeMedicalMajorFromDoctorStatement.setInt(1, medicalMajor.getId());
            removeMedicalMajorFromDoctorStatement.executeUpdate();
            removeMedicalMajorStatement.setInt(1, medicalMajor.getId());
            removeMedicalMajorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDisease(Disease disease) {
        try {
            removeDiseaseFromAppointmentStatement.setInt(1, disease.getId());
            removeDiseaseFromAppointmentStatement.executeUpdate();
            removeDiseaseFromAppointmentTreatmentStatement.setInt(1, disease.getId());
            removeDiseaseFromAppointmentTreatmentStatement.executeUpdate();
            removeDiseaseFromTreatmentStatement.setInt(1, disease.getId());
            removeDiseaseFromTreatmentStatement.executeUpdate();
            removeDiseaseStatement.setInt(1, disease.getId());
            removeDiseaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int determineId(PreparedStatement ps) {
        try {
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isMedicalMajorNameDuplicate(String name) {
        ArrayList<MedicalMajor> medicalMajors = medicalMajors();
        for (MedicalMajor m : medicalMajors) {
            if (name.toLowerCase().equals(m.getMedicalMajorName().toLowerCase())) return true;
        }
        return false;
    }
    public int determineMedicalMajorId() {
        return determineId(determineMedicalMajorIdStatement);
    }
    public void addMedicalMajor(MedicalMajor medicalMajor) {
        try {
            addMedicalMajorStatement.setInt(1, medicalMajor.getId());
            addMedicalMajorStatement.setString(2, medicalMajor.getMedicalMajorName());
            addMedicalMajorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isTreatmentNameDuplicate(String name) {
        ArrayList<Treatment> treatments = treatments();
        for (Treatment t : treatments) {
            if (name.toLowerCase().equals(t.getTreatmentName().toLowerCase())) return true;
        }
        return false;
    }
    public int determineTreatmentId() {
        return determineId(determineTreatmentIdStatement);
    }
    public void addTreatment(Treatment treatment) {
        try {
            addTreatmentStatement.setInt(1, treatment.getId());
            addTreatmentStatement.setString(2, treatment.getTreatmentName());
            addTreatmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addTreatmentDiseases(Treatment treatment, List<Disease> diseases) {
        try {
            for (Disease d : diseases) {
                addDiseaseTreatmentStatement.setInt(1, d.getId());
                addDiseaseTreatmentStatement.setInt(2, treatment.getId());
                addDiseaseTreatmentStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDiseaseNameDuplicate(String name) {
        ArrayList<Disease> diseases = diseases();
        for (Disease d : diseases) {
            if (name.toLowerCase().equals(d.getDiseaseName().toLowerCase())) return true;
        }
        return false;
    }
    public int determineDiseaseId() {
        return determineId(determineDiseaseIdStatement);
    }
    public void addDisease(Disease disease) {
        try {
            addDiseaseStatement.setInt(1, disease.getId());
            addDiseaseStatement.setString(2, disease.getDiseaseName());
            addDiseaseStatement.setInt(3, disease.getMedicalMajor().getId());
            addDiseaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDiseaseTreatments(Disease disease, List<Treatment> treatments) {
        try {
            for (Treatment t : treatments) {
                addDiseaseTreatmentStatement.setInt(1, disease.getId());
                addDiseaseTreatmentStatement.setInt(2, t.getId());
                addDiseaseTreatmentStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Disease> diseasesFromDoctor(Doctor doctor) {
        ArrayList<Disease> diseases = new ArrayList<>();
        try {
            getMedicalMajorFromDoctorStatement.setInt(1, doctor.getId());
            ResultSet rs = getMedicalMajorFromDoctorStatement.executeQuery();

            getDiseasesFromMedicalMajorStatement.setInt(1, rs.getInt(1));
            ResultSet rs1 = getDiseasesFromMedicalMajorStatement.executeQuery();
            while (rs1.next()) diseases.add(getDiseaseFromResultSet(rs1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diseases;
    }
    public ArrayList<Doctor> doctorsFromDisease(Disease disease) {
        ArrayList<Doctor> doctors = new ArrayList<>();
        try {
            getDoctorFromMedicalMajorStatement.setInt(1,disease.getMedicalMajor().getId());
            ResultSet rs = getDoctorFromMedicalMajorStatement.executeQuery();
            while (rs.next()) doctors.add(getDoctorFromResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    public int determineAppointmentId() {
        return determineId(determineAppointmentIdStatement);
    }
    public void addAppointment(Appointment appointment) {
        try {
            addAppointmentStatement.setInt(1, appointment.getId());
            addAppointmentStatement.setInt(2, appointment.getPatient().getId());
            addAppointmentStatement.setInt(3, appointment.getDoctor().getId());
            addAppointmentStatement.setInt(4, appointment.getDisease().getId());
            addAppointmentStatement.setString(5, String.valueOf(appointment.getAppointmentDate()));
            addAppointmentStatement.setString(6, String.valueOf(appointment.getAppointmentTime()));
            addAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
