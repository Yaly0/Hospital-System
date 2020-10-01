package ba.unsa.etf.rpr.projekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class SignInController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Label errorLabel;

    public void signInAction() {
        if(!usernameField.getText().equals("admin") || !passwordField.getText().equals("admin")) {
            errorLabel.setText("Incorrect username or password!");
            usernameField.getStyleClass().add("invalidField");
            passwordField.getStyleClass().add("invalidField");
        } else {
            Parent root;
            try {
                Stage oldStage = (Stage) usernameField.getScene().getWindow();
                Stage newStage = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                root = loader.load();

                newStage.setTitle("Hospital");
                newStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                newStage.setResizable(false);

                oldStage.close();
                newStage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
