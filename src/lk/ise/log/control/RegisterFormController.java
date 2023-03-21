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
import lk.ise.log.dao.util.PasswordConfig;
import lk.ise.log.entity.User;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterFormController {
    public TextField txtUsername;
    public TextField txtPassword;
    public AnchorPane tblregister;
    public Button btn;

    public void saveUser(ActionEvent actionEvent) {
        User u1=new User(
                txtUsername.getText(),new PasswordConfig().encryptPassword(txtPassword.getText())
        );
        if(btn.getText().equals("Register")){
            try{
                if(new DataAccessCode().saveUser(u1)){
                    new Alert(Alert.AlertType.INFORMATION, "User Saved!").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void signinOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) tblregister.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader
                .load(getClass().getResource("../view/LoginForm.fxml"))));
    }
}
