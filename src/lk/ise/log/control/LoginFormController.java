package lk.ise.log.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ise.log.dao.DataAccessCode;
import lk.ise.log.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginFormContext;
    public TextField txtUsername;
    public TextField pwd;
    public Button registerbtn;

    public void initialize() {
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        try {
            User selectedUser=new DataAccessCode().findUser(txtUsername.getText());
            if (selectedUser != null) {
                if (BCrypt.checkpw(pwd.getText(), selectedUser.getPassword())) {
                    Stage stage = (Stage) loginFormContext.getScene().getWindow();
                    stage.setScene(
                            new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml")))
                    );
                    stage.centerOnScreen();
                } else {
                    System.out.println("Wrong password!");
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "User name not found!").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void RegisterOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) loginFormContext.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/RegisterForm.fxml")))
        );
    }
}

