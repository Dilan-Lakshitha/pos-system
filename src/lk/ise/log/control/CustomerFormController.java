package lk.ise.log.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ise.log.dao.DataAccessCode;
import lk.ise.log.entity.Customer;
import lk.ise.log.view.tm.CustomerTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


public class CustomerFormController {
    public AnchorPane customerFormContext;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TableView<CustomerTM> tbl;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOption;

    public Button btn;

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadAll("");

        tbl.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                             if(newValue!=null){
                                 setData(newValue);
                              }
                }));
    }

    private void setData(CustomerTM newValue) {
        txtId.setText(newValue.getId());
        txtName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
        btn.setText("Update Customer");
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) customerFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader
                .load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    public void saveCustomer(ActionEvent actionEvent){
        Customer c1 = new Customer(
                txtId.getText(), txtName.getText(), txtAddress.getText()
                , Double.parseDouble(txtSalary.getText())
        );


        if (btn.getText().equals("Save Customer")) {
            try {
                if (new DataAccessCode().saveCustomer(c1)) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Saved!").show();
                    loadAll("");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went Wrong!").show();
                }
            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }

        } else {
            try {
                if (new DataAccessCode().updateCustomer(c1)) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated!").show();
                    loadAll("");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went Wrong!").show();
                }
            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }


        clearData();
    }

    private void clearData() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
    }
    private void loadAll(String searchText) {
        ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();
        try {
            for (Customer c : new DataAccessCode().allCustomer()) {
                Button btn = new Button("Delete");
                CustomerTM tm = new CustomerTM(
                        c.getId(), c.getName(), c.getAddress(), c.getSalary(), btn
                );


                btn.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are yiu sure?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> type = alert.showAndWait();
                    if (type.get() == ButtonType.YES) {
                        try {
                            if (new DataAccessCode().deleteCustomer(c.getId())) {
                                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted!").show();
                                loadAll("");
                            } else {
                                new Alert(Alert.AlertType.INFORMATION, "Somthing went worng!").show();
                            }
                        }catch (ClassNotFoundException | SQLException ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        }
                    }

                });

                tmList.add(tm);
            }
            tbl.setItems(tmList);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }



    public void newCustomer(ActionEvent actionEvent) {
        clearData();
        btn.setText("save customer");
    }
}
