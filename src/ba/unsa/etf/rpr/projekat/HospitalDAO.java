package ba.unsa.etf.rpr.projekat;

import java.sql.Connection;
import java.sql.SQLException;

public class HospitalDAO {
    private static HospitalDAO instance = null;
    private Connection connection;

    private HospitalDAO() {

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
