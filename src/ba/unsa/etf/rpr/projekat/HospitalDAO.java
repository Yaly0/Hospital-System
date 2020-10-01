package ba.unsa.etf.rpr.projekat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Patient.*;
import static ba.unsa.etf.rpr.projekat.Doctor.*;

class HospitalDAO {
    private static HospitalDAO instance = null;
    private Connection connection;

    private PreparedStatement getAppointmentsStatement, getPatientsStatement, getDoctorsStatement,
            getTreatmentsStatement, getMedicalMajorsStatement, getDiseasesStatement, getMedicalMajorStatement,
            getPatientStatement, getDoctorStatement, getDiseaseStatement, removeTreatmentFromAppointmentStatement,
            removeTreatmentFromDiseaseStatement, removeTreatmentStatement, removeAppointmentFromTreatmentStatement,
            removeAppointmentStatement, removePatientFromAppointmentStatement, removePatientStatement,
            removeDoctorFromAppointmentStatement, removeDoctorStatement, getDiseasesFromMedicalMajorStatement,
            removeMedicalMajorFromDoctorStatement, removeMedicalMajorStatement, removeDiseaseFromAppointmentStatement,
            removeDiseaseStatement, removeDiseasesFromAppointmentTreatmentStatement,
            removeDiseaseFromTreatmentsStatement, determineMedicalMajorIdStatement, addMedicalMajorStatement,
            determineTreatmentIdStatement, addTreatmentStatement, addDiseaseTreatmentStatement,
            determineDiseaseIdStatement, addDiseaseStatement, getMedicalMajorFromDoctorStatement,
            getDoctorFromMedicalMajorStatement, determineAppointmentIdStatement, addAppointmentStatement,
            getDiseasesFromTreatmentStatement, removeDiseaseTreatmentFromDiseasesTreatmentStatement,
            removeDiseaseFromAppointmentTreatmentStatement, updateTreatmentStatement, determinePatientIdStatement,
            addPatientStatement, determineDoctorIdStatement, addDoctorStatement, getDoctorsFromMedicalMajorStatement,
            getDiseaseFromMedicalMajorStatement, updateMedicalMajorStatement, getTreatmentsFromDiseaseStatement,
            getTreatmentStatement, updateDiseaseStatement, getAppointmentsFromPatientStatement, getAppointmentStatement,
            updatePatientStatement, getAppointmentsFromDoctorStatement, updateDoctorStatement,
            removeAppointmentTreatmentStatement, getTreatmentsFromAppointmentStatement, getDiseaseTreatmentStatement,
            removeTreatmentFromAppointmentTreatmentStatement, addAppointmentTreatmentStatement,
            updateAppointmentStatement, removeAppointmentFromAppointmentTreatmentStatement;

    private HospitalDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:hospital.db");
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
            getTreatmentStatement = connection.prepareStatement("SELECT * FROM treatment WHERE id = ?");
            getAppointmentStatement = connection.prepareStatement("SELECT * FROM appointment WHERE id = ?");
            getMedicalMajorStatement = connection.prepareStatement("SELECT medical_major_name FROM medical_major WHERE id = ?");

            getDiseasesFromMedicalMajorStatement = connection.prepareStatement("SELECT * FROM disease WHERE medical_major_id = ?");
            getMedicalMajorFromDoctorStatement = connection.prepareStatement("SELECT medical_major_id FROM doctor WHERE id = ?");
            getDoctorFromMedicalMajorStatement = connection.prepareStatement("SELECT * FROM doctor WHERE medical_major_id = ?");
            getDiseasesFromTreatmentStatement = connection.prepareStatement("SELECT disease_id FROM disease_treatment WHERE treatment_id=?");
            getDoctorsFromMedicalMajorStatement = connection.prepareStatement("SELECT * FROM doctor WHERE medical_major_id=?");
            getDiseaseFromMedicalMajorStatement = connection.prepareStatement("SELECT * FROM disease WHERE medical_major_id=?");
            getTreatmentsFromDiseaseStatement = connection.prepareStatement("SELECT treatment_id FROM disease_treatment WHERE disease_id=?");
            getAppointmentsFromPatientStatement = connection.prepareStatement("SELECT * FROM appointment WHERE patient_id=?");
            getAppointmentsFromDoctorStatement = connection.prepareStatement("SELECT * FROM appointment WHERE doctor_id=?");
            getTreatmentsFromAppointmentStatement = connection.prepareStatement("SELECT treatment_id FROM appointment_treatment WHERE appointment_id=?");
            getDiseaseTreatmentStatement = connection.prepareStatement("SELECT * FROM disease_treatment WHERE disease_id=? AND treatment_id=?");

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
            removeDiseasesFromAppointmentTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE disease_id = ?");
            removeDiseaseFromTreatmentsStatement = connection.prepareStatement("DELETE FROM disease_treatment WHERE disease_id = ?");
            removeDiseaseStatement = connection.prepareStatement("DELETE FROM disease WHERE id = ?");
            removeAppointmentTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE appointment_id = ?");

            removeDiseaseTreatmentFromDiseasesTreatmentStatement = connection.prepareStatement("DELETE FROM disease_treatment WHERE disease_id = ? AND treatment_id=?");
            removeDiseaseFromAppointmentTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE disease_id = ? AND treatment_id=?");
            removeTreatmentFromAppointmentTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE appointment_id = ? AND treatment_id=?");
            removeAppointmentFromAppointmentTreatmentStatement = connection.prepareStatement("DELETE FROM appointment_treatment WHERE appointment_id=?");

            determineMedicalMajorIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM medical_major");
            determineTreatmentIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM treatment");
            determineDiseaseIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM disease");
            determineAppointmentIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM appointment");
            determinePatientIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM patient");
            determineDoctorIdStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM doctor");

            addMedicalMajorStatement = connection.prepareStatement("INSERT INTO medical_major VALUES (?,?)");
            addTreatmentStatement = connection.prepareStatement("INSERT INTO treatment VALUES (?,?)");
            addDiseaseTreatmentStatement = connection.prepareStatement("INSERT INTO disease_treatment VALUES (?,?)");
            addDiseaseStatement = connection.prepareStatement("INSERT INTO disease VALUES (?,?,?)");
            addAppointmentStatement = connection.prepareStatement("INSERT INTO appointment VALUES (?,?,?,?,?,?,NULL,NULL)");
            addPatientStatement = connection.prepareStatement("INSERT INTO patient VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            addDoctorStatement = connection.prepareStatement("INSERT INTO doctor VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            addAppointmentTreatmentStatement = connection.prepareStatement("INSERT INTO appointment_treatment VALUES(?,?,?)");

            updateAppointmentStatement = connection.prepareStatement("UPDATE appointment SET patient_id=?, doctor_id=?, disease_id=?, appointment_date=?, appointment_time=?, treatment_description=?, appointment_report=? WHERE id=?");
            updatePatientStatement = connection.prepareStatement("UPDATE patient SET first_name=?, last_name=?, home_address=?, birth_date=?, citizen_number=?, phone_number=?, email_address=?, gender=?, blood_type=?, height=?, weight=? WHERE id=?");
            updateDoctorStatement = connection.prepareStatement("UPDATE doctor SET first_name=?, last_name=?, home_address=?, birth_date=?, citizen_number=?, phone_number=?, email_address=?, gender=?, blood_type=?, medical_major_id=?, shift_hours=? WHERE id=?");
            updateTreatmentStatement = connection.prepareStatement("UPDATE treatment SET treatment_name=? WHERE id=?");
            updateMedicalMajorStatement = connection.prepareStatement("UPDATE medical_major SET medical_major_name=? WHERE id=?");
            updateDiseaseStatement = connection.prepareStatement("UPDATE disease SET disease_name=?, medical_major_id=? WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerateBase() {
        Scanner input;
        try {
            input = new Scanner(new FileInputStream("hospital.db.sql"));
            StringBuilder sqlStatement = new StringBuilder();
            while (input.hasNext()) {
                sqlStatement.append(input.nextLine());
                if (sqlStatement.length() > 1 && sqlStatement.charAt(sqlStatement.length() - 1) == ';') {
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(sqlStatement.toString());
                        sqlStatement = new StringBuilder();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static HospitalDAO getInstance() {
        if (instance == null) instance = new HospitalDAO();
        return instance;
    }

    ArrayList<Appointment> appointments() {
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
    private Appointment getAppointmentFromResultSet(ResultSet rs)  {
        try {
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

            Appointment appointment = new Appointment(rs.getInt(1), patient, doctor, disease, date, time);
            appointment.setTreatmentsDescription(rs.getString(7));
            appointment.setAppointmentReport(rs.getString(8));

            return appointment;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<Patient> patients() {
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
        Weight weight = new Weight(rs.getInt(12));

        return new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), date, citizenNumber, phoneNumber, emailAddress, gender, bloodType, height, weight);
    }

    ArrayList<Doctor> doctors() {
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
    private Doctor getDoctorFromResultSet(ResultSet rs) {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<Treatment> treatments() {
        ArrayList<Treatment> treatments = new ArrayList<>();
        try {
            ResultSet rs = getTreatmentsStatement.executeQuery();
            while (rs.next()) {
                treatments.add(getTreatmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }
    private Treatment getTreatmentFromResultSet(ResultSet rs) {
        try {
            return new Treatment(rs.getInt(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<MedicalMajor> medicalMajors() {
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

    ArrayList<Disease> diseases() {
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
    private Disease getDiseaseFromResultSet(ResultSet rs) {
        try {
            getMedicalMajorStatement.setInt(1, rs.getInt(3));
            ResultSet rs2 = getMedicalMajorStatement.executeQuery();
            MedicalMajor medicalMajor = new MedicalMajor(rs.getInt(3), rs2.getString(1));
            return new Disease(rs.getInt(1), rs.getString(2), medicalMajor);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void removeOne(int id, PreparedStatement... preparedStatements) {
        try {
            for(PreparedStatement ps: preparedStatements){
                ps.setInt(1, id);
                ps.executeUpdate();
            }
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
    private <T> ArrayList<T> getGroupFromOneFromRelation(int id, Function<ResultSet,T> function, PreparedStatement ps1, PreparedStatement ps2) {
        ArrayList<T> objects = new ArrayList<>();
        try {
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                ps2.setInt(1, rs.getInt(1));
                ResultSet rs1 = ps2.executeQuery();
                objects.add(function.apply(rs1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objects;
    }
    private <T> ArrayList<T> getGroupFromOneFromId(int id, Function<ResultSet,T> function, PreparedStatement ps1) {
        ArrayList<T> objects = new ArrayList<>();
        try {
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                objects.add(function.apply(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objects;
    }


    void removeAppointment(Appointment appointment) {
        removeOne(appointment.getId(), removeAppointmentFromTreatmentStatement, removeAppointmentStatement);
    }
    void removePatient(Patient patient) {
        removeOne(patient.getId(), removePatientFromAppointmentStatement, removePatientStatement);
    }
    void removeDoctor(Doctor doctor) {
        removeOne(doctor.getId(), removeDoctorFromAppointmentStatement, removeDoctorStatement);
    }
    void removeTreatment(Treatment treatment) {
        removeOne(treatment.getId(), removeTreatmentFromAppointmentStatement, removeTreatmentFromDiseaseStatement, removeTreatmentStatement);
    }
    void removeMedicalMajor(MedicalMajor medicalMajor) {
        try {
            getDiseasesFromMedicalMajorStatement.setInt(1, medicalMajor.getId());
            ResultSet rs = getDiseasesFromMedicalMajorStatement.executeQuery();
            while (rs.next()) {
                removeDisease(getDiseaseFromResultSet(rs));
            }
            removeOne(medicalMajor.getId(), removeMedicalMajorFromDoctorStatement, removeMedicalMajorStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void removeDisease(Disease disease) {
        removeOne(disease.getId(), removeDiseaseFromAppointmentStatement, removeDiseasesFromAppointmentTreatmentStatement, removeDiseaseFromTreatmentsStatement, removeDiseaseStatement);
    }


    ArrayList<Disease> getDiseasesFromDoctor(Doctor doctor) {
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
    ArrayList<Doctor> getDoctorsFromDisease(Disease disease) {
        return getGroupFromOneFromId(disease.getMedicalMajor().getId(), this::getDoctorFromResultSet, getDoctorFromMedicalMajorStatement);
    }
    int determineAppointmentId() {
        return determineId(determineAppointmentIdStatement);
    }
    void addAppointment(Appointment appointment) {
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

    int determinePatientId() {
        return determineId(determinePatientIdStatement);
    }
    void addPatient(Patient patient) {
        try {
            addPatientStatement.setInt(1, patient.getId());
            addPatientStatement.setString(2, patient.getFirstName());
            addPatientStatement.setString(3, patient.getLastName());
            addPatientStatement.setString(4, patient.getHomeAddress());
            addPatientStatement.setString(5, patient.getBirthDate().toString());
            addPatientStatement.setString(6, patient.getCitizenNumber().toString());
            addPatientStatement.setString(7, patient.getPhoneNumber().toString());
            addPatientStatement.setString(8, patient.getEmailAddress().toString());
            addPatientStatement.setString(9, patient.getGender().toString());
            addPatientStatement.setString(10, patient.getBloodType().toString());
            addPatientStatement.setInt(11, patient.getHeight().getHeight());
            addPatientStatement.setInt(12, patient.getWeight().getWeight());
            addPatientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int determineDoctorId() {return determineId(determineDoctorIdStatement);}
    void addDoctor(Doctor doctor) {
        try {
            addDoctorStatement.setInt(1, doctor.getId());
            addDoctorStatement.setString(2, doctor.getFirstName());
            addDoctorStatement.setString(3, doctor.getLastName());
            addDoctorStatement.setString(4, doctor.getHomeAddress());
            addDoctorStatement.setString(5, doctor.getBirthDate().toString());
            addDoctorStatement.setString(6, doctor.getCitizenNumber().toString());
            addDoctorStatement.setString(7, doctor.getPhoneNumber().toString());
            addDoctorStatement.setString(8, doctor.getEmailAddress().toString());
            addDoctorStatement.setString(9, doctor.getGender().toString());
            addDoctorStatement.setString(10, doctor.getBloodType().toString());
            addDoctorStatement.setInt(11, doctor.getMedicalMajor().getId());
            addDoctorStatement.setString(12, doctor.getShiftHours().toString());
            addDoctorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean isTreatmentNameDuplicate(String name) {
        ArrayList<Treatment> treatments = treatments();
        for (Treatment t : treatments) {
            if (name.toLowerCase().equals(t.getTreatmentName().toLowerCase())) return true;
        }
        return false;
    }
    int determineTreatmentId() {
        return determineId(determineTreatmentIdStatement);
    }
    void addTreatment(Treatment treatment) {
        try {
            addTreatmentStatement.setInt(1, treatment.getId());
            addTreatmentStatement.setString(2, treatment.getTreatmentName());
            addTreatmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void addTreatmentDiseases(Treatment treatment, List<Disease> diseases) {
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

    boolean isMedicalMajorNameDuplicate(String name) {
        ArrayList<MedicalMajor> medicalMajors = medicalMajors();
        for (MedicalMajor m : medicalMajors) {
            if (name.toLowerCase().equals(m.getMedicalMajorName().toLowerCase())) return true;
        }
        return false;
    }
    int determineMedicalMajorId() {
        return determineId(determineMedicalMajorIdStatement);
    }
    void addMedicalMajor(MedicalMajor medicalMajor) {
        try {
            addMedicalMajorStatement.setInt(1, medicalMajor.getId());
            addMedicalMajorStatement.setString(2, medicalMajor.getMedicalMajorName());
            addMedicalMajorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean isDiseaseNameDuplicate(String name) {
        ArrayList<Disease> diseases = diseases();
        for (Disease d : diseases) {
            if (name.toLowerCase().equals(d.getDiseaseName().toLowerCase())) return true;
        }
        return false;
    }
    int determineDiseaseId() {
        return determineId(determineDiseaseIdStatement);
    }
    void addDisease(Disease disease) {
        try {
            addDiseaseStatement.setInt(1, disease.getId());
            addDiseaseStatement.setString(2, disease.getDiseaseName());
            addDiseaseStatement.setInt(3, disease.getMedicalMajor().getId());
            addDiseaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void addDiseaseTreatments(Disease disease, List<Treatment> treatments) {
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


    ArrayList<Treatment> getTreatmentsFromAppointment(Appointment appointment) {
        return getGroupFromOneFromRelation(appointment.getId(), this::getTreatmentFromResultSet, getTreatmentsFromAppointmentStatement, getTreatmentStatement);
    }
    boolean isTreatmentForDisease(Treatment treatment, Disease disease) {
        try {
            getDiseaseTreatmentStatement.setInt(1,disease.getId());
            getDiseaseTreatmentStatement.setInt(2,treatment.getId());
            ResultSet rs = getDiseaseTreatmentStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    void updateAppointmentTreatments(Appointment appointment, ArrayList<Treatment> treatmentsForDelete, ArrayList<Treatment> treatmentsForAdd) {
        try {
            for (Treatment t : treatmentsForDelete) {
                removeTreatmentFromAppointmentTreatmentStatement.setInt(1, appointment.getId());
                removeTreatmentFromAppointmentTreatmentStatement.setInt(2, t.getId());
                removeTreatmentFromAppointmentTreatmentStatement.executeUpdate();
            }
            for (Treatment t : treatmentsForAdd) {
                addAppointmentTreatmentStatement.setInt(1, appointment.getId());
                addAppointmentTreatmentStatement.setInt(2, t.getId());
                addAppointmentTreatmentStatement.setInt(3, appointment.getDisease().getId());
                addAppointmentTreatmentStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void updateAppointment(Appointment appointment) {
        try {
            updateAppointmentStatement.setInt(1, appointment.getPatient().getId());
            updateAppointmentStatement.setInt(2, appointment.getDoctor().getId());
            updateAppointmentStatement.setInt(3, appointment.getDisease().getId());
            updateAppointmentStatement.setString(4, String.valueOf(appointment.getAppointmentDate()));
            updateAppointmentStatement.setString(5, String.valueOf(appointment.getAppointmentTime()));
            updateAppointmentStatement.setString(6, appointment.getTreatmentsDescription());
            updateAppointmentStatement.setString(7, appointment.getAppointmentReport());
            updateAppointmentStatement.setInt(8, appointment.getId());
            updateAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void updateAppointmentDiseases(Appointment appointment) {
        try {
            removeAppointmentFromAppointmentTreatmentStatement.setInt(1,appointment.getId());
            removeAppointmentFromAppointmentTreatmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateDiseaseAppointments(Disease disease) {
        try {
            removeDiseaseFromAppointmentStatement.setInt(1,disease.getId());
            removeDiseaseFromAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ArrayList<Appointment> getAppointmentsFromPatient(Patient patient) {
        return getGroupFromOneFromRelation(patient.getId(), this::getAppointmentFromResultSet, getAppointmentsFromPatientStatement, getAppointmentStatement);
    }
    void updatePatient(Patient patient) {
        try {
            updatePatientStatement.setString(1, patient.getFirstName());
            updatePatientStatement.setString(2, patient.getLastName());
            updatePatientStatement.setString(3, patient.getHomeAddress());
            updatePatientStatement.setString(4, patient.getBirthDate().toString());
            updatePatientStatement.setString(5, patient.getCitizenNumber().toString());
            updatePatientStatement.setString(6, patient.getPhoneNumber().toString());
            updatePatientStatement.setString(7, patient.getEmailAddress().toString());
            updatePatientStatement.setString(8, patient.getGender().toString());
            updatePatientStatement.setString(9, patient.getBloodType().toString());
            updatePatientStatement.setInt(10, patient.getHeight().getHeight());
            updatePatientStatement.setInt(11, patient.getWeight().getWeight());
            updatePatientStatement.setInt(12, patient.getId());
            updatePatientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Appointment> getAppointmentsFromDoctor(Doctor doctor) {
        return getGroupFromOneFromRelation(doctor.getId(), this::getAppointmentFromResultSet, getAppointmentsFromDoctorStatement, getAppointmentStatement);
    }
    void updateDoctor(Doctor doctor) {
        try {
            updateDoctorStatement.setString(1, doctor.getFirstName());
            updateDoctorStatement.setString(2, doctor.getLastName());
            updateDoctorStatement.setString(3, doctor.getHomeAddress());
            updateDoctorStatement.setString(4, doctor.getBirthDate().toString());
            updateDoctorStatement.setString(5, doctor.getCitizenNumber().toString());
            updateDoctorStatement.setString(6, doctor.getPhoneNumber().toString());
            updateDoctorStatement.setString(7, doctor.getEmailAddress().toString());
            updateDoctorStatement.setString(8, doctor.getGender().toString());
            updateDoctorStatement.setString(9, doctor.getBloodType().toString());
            updateDoctorStatement.setInt(10, doctor.getMedicalMajor().getId());
            updateDoctorStatement.setString(11, doctor.getShiftHours().toString());
            updateDoctorStatement.setInt(12, doctor.getId());
            updateDoctorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void updateDoctorAppointments(Doctor doctor) {
        try {
            getAppointmentsFromDoctorStatement.setInt(1,doctor.getId());
            ResultSet rs = getAppointmentsFromDoctorStatement.executeQuery();
            while (rs.next()) {
                removeAppointmentTreatmentStatement.setInt(1, rs.getInt(1));
                removeAppointmentTreatmentStatement.executeUpdate();
            }
            removeDoctorFromAppointmentStatement.setInt(1,doctor.getId());
            removeDoctorFromAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Disease> getDiseasesFromTreatment(Treatment treatment) {
        return getGroupFromOneFromRelation(treatment.getId(), this::getDiseaseFromResultSet, getDiseasesFromTreatmentStatement, getDiseaseStatement);
    }
    void updateTreatmentDiseases(Treatment treatment, ArrayList<Disease> diseasesForDelete, ArrayList<Disease> diseasesForAdd) {
        try {
            for (Disease d : diseasesForDelete) {
                removeDiseaseTreatmentFromDiseasesTreatmentStatement.setInt(1, d.getId());
                removeDiseaseTreatmentFromDiseasesTreatmentStatement.setInt(2, treatment.getId());
                removeDiseaseTreatmentFromDiseasesTreatmentStatement.executeUpdate();
                removeDiseaseFromAppointmentTreatmentStatement.setInt(1, d.getId());
                removeDiseaseFromAppointmentTreatmentStatement.setInt(2, treatment.getId());
                removeDiseaseFromAppointmentTreatmentStatement.executeUpdate();
            }
            for (Disease d : diseasesForAdd) {
                addDiseaseTreatmentStatement.setInt(1, d.getId());
                addDiseaseTreatmentStatement.setInt(2, treatment.getId());
                addDiseaseTreatmentStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void updateTreatment(Treatment treatment) {
        try {
            updateTreatmentStatement.setString(1,treatment.getTreatmentName());
            updateTreatmentStatement.setInt(2,treatment.getId());
            updateTreatmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Doctor> getDoctorsFromMedicalMajor(MedicalMajor medicalMajor) {
        return getGroupFromOneFromId(medicalMajor.getId(), this::getDoctorFromResultSet, getDoctorsFromMedicalMajorStatement);
    }
    ArrayList<Disease> getDiseasesFromMedicalMajor(MedicalMajor medicalMajor) {
        return getGroupFromOneFromId(medicalMajor.getId(), this::getDiseaseFromResultSet, getDiseaseFromMedicalMajorStatement);
    }
    void updateMedicalMajor(MedicalMajor medicalMajor) {
        try {
            updateMedicalMajorStatement.setString(1,medicalMajor.getMedicalMajorName());
            updateMedicalMajorStatement.setInt(2,medicalMajor.getId());
            updateMedicalMajorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Treatment> getTreatmentsFromDisease(Disease disease) {
        return getGroupFromOneFromRelation(disease.getId(), this::getTreatmentFromResultSet, getTreatmentsFromDiseaseStatement, getTreatmentStatement);
    }
    void updateDiseaseTreatments(Disease disease, ArrayList<Treatment> treatmentsForDelete, ArrayList<Treatment> treatmentsForAdd) {
        try {
            for (Treatment t : treatmentsForDelete) {
                removeDiseaseTreatmentFromDiseasesTreatmentStatement.setInt(1, disease.getId());
                removeDiseaseTreatmentFromDiseasesTreatmentStatement.setInt(2, t.getId());
                removeDiseaseTreatmentFromDiseasesTreatmentStatement.executeUpdate();
                removeDiseaseFromAppointmentTreatmentStatement.setInt(1, disease.getId());
                removeDiseaseFromAppointmentTreatmentStatement.setInt(2, t.getId());
                removeDiseaseFromAppointmentTreatmentStatement.executeUpdate();
            }
            for (Treatment t : treatmentsForAdd) {
                addDiseaseTreatmentStatement.setInt(1, disease.getId());
                addDiseaseTreatmentStatement.setInt(2, t.getId());
                addDiseaseTreatmentStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void updateDisease(Disease disease) {
        try {
            updateDiseaseStatement.setString(1,disease.getDiseaseName());
            updateDiseaseStatement.setInt(2,disease.getMedicalMajor().getId());
            updateDiseaseStatement.setInt(3,disease.getId());
            updateDiseaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
