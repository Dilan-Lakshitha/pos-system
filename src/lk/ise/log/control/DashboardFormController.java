package lk.ise.log.control;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardFormController {
    public AnchorPane dashboardContext;
    public Label txtDateAndTime;

    public void initialize(){

        manageDateAndTime();
    }

    private void manageDateAndTime() {
        Timeline timeAndDate=new Timeline(new KeyFrame(Duration.ZERO,
                e-> txtDateAndTime.setText(LocalDateTime.now().
                    format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))),
                new KeyFrame(Duration.seconds(1)));
        timeAndDate.setCycleCount(Animation.INDEFINITE);
        timeAndDate.play();

    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
        new Alert(Alert.AlertType.WARNING,"LOGGED OUT!").show();
    }

    public void openCustomerForm(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm");
    }

    public void openItemForm(ActionEvent actionEvent) throws IOException {
        setUi("ItemForm");
    }

    public void openNewOrderForm(ActionEvent actionEvent) throws IOException {
        setUi("PlaceOderForm");
    }

    public void openOrderForm(ActionEvent actionEvent) throws IOException {
        setUi("OrderForm");
    }

    public void openIncomeForm(ActionEvent actionEvent) {
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) dashboardContext.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")))
        );
        stage.centerOnScreen();
    }



}
