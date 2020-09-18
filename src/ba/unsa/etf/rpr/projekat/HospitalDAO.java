package ba.unsa.etf.rpr.projekat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class HospitalDAO {
    private static HospitalDAO instance = null;
    private Connection connection;

    private PreparedStatement getPatientsStatement, getDoctorsStatement, getTreatmmentsStatement, getDiseasesStatement;

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
}
