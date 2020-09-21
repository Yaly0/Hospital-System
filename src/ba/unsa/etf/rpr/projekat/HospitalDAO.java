package ba.unsa.etf.rpr.projekat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Patient.*;
import static ba.unsa.etf.rpr.projekat.Doctor.*;

public class HospitalDAO {
    private static HospitalDAO instance = null;
    private Connection connection;

    private PreparedStatement getAppointmentsStatement, getPatientsStatement, getDoctorsStatement, getTreatmentsStatement, getDiseasesStatement, getMedicalMajor;

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
            getDoctorsStatement = connection.prepareStatement("SELECT * FROM doctor");
            getTreatmentsStatement = connection.prepareStatement("SELECT * FROM treatment");
            getDiseasesStatement = connection.prepareStatement("SELECT * FROM disease");
            getAppointmentsStatement = connection.prepareStatement("SELECT * FROM appointment");
            getMedicalMajor = connection.prepareStatement("SELECT major_name FROM medical_major WHERE id = ?");
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
        return new ArrayList<>();
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

        getMedicalMajor.setInt(1, rs.getInt(11));
        ResultSet rs2 = getMedicalMajor.executeQuery();
        MedicalMajor medicalMajor = new MedicalMajor(rs.getInt(11), rs2.getString(1));

        String time = rs.getString(12);
        ShiftHours shiftHours = new ShiftHours(
                LocalTime.of(Integer.valueOf(time.substring(0,2)),Integer.valueOf(time.substring(3,5))),
                LocalTime.of(Integer.valueOf(time.substring(6,8)),Integer.valueOf(time.substring(9,11))),
                time.length() > 12 ? LocalTime.of(Integer.valueOf(time.substring(15,17)),Integer.valueOf(time.substring(18,20))) : LocalTime.of(12,0),
                time.length() > 12 ? LocalTime.of(Integer.valueOf(time.substring(21,23)),Integer.valueOf(time.substring(24,26))) : LocalTime.of(12,0));

        return new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), date, citizenNumber, phoneNumber, emailAddress, gender, bloodType, medicalMajor, shiftHours);
    }

    public ArrayList<Treatment> treatments() {
        ArrayList<Treatment> treatments = new ArrayList<>();
        try {
            ResultSet rs = getTreatmentsStatement.executeQuery();
            while (rs.next()) {
                treatments.add(new Treatment(rs.getInt(1),rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }
}
