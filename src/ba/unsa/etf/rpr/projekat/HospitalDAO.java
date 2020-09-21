package ba.unsa.etf.rpr.projekat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static ba.unsa.etf.rpr.projekat.Person.*;
import static ba.unsa.etf.rpr.projekat.Patient.*;
import static ba.unsa.etf.rpr.projekat.Person.BloodType.O_NEGATIVE;

public class HospitalDAO {
    private static HospitalDAO instance = null;
    private Connection connection;

    private PreparedStatement getPatientsStatement, getDoctorsStatement, getTreatmmentsStatement, getDiseasesStatement, getAppointmentsStatement;

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
            getTreatmmentsStatement = connection.prepareStatement("SELECT * FROM treatment");
            getDiseasesStatement = connection.prepareStatement("SELECT * FROM disease");
            getAppointmentsStatement = connection.prepareStatement("SELECT * FROM appointment");
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
        ArrayList<Patient> rezultat = new ArrayList();
        try {
            ResultSet rs = getPatientsStatement.executeQuery();
            while (rs.next()) {
                rezultat.add(getPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
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
}
