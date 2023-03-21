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
import lk.ise.log.entity.Item;
import lk.ise.log.view.tm.ItemTM;

import java.io.IOException;
import java.sql.SQLException;

public class ItemFormController {
    public AnchorPane ItemContext;
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtQty;
    public TextField txtUnitprice;
    public TableView itemTBL;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colOption;
    public Button ItemBtn;

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadAll("");
    }
    public void saveItem(ActionEvent actionEvent) {
        Item D1=new Item(txtCode.getText(),txtDescription.getText(),
                Integer.parseInt(txtQty.getText()),Double.parseDouble(txtUnitprice.getText())
        );
        if(ItemBtn.getText().equals("Save Item")){
            try {
                if(new DataAccessCode().saveItem(D1)){
                    new Alert(Alert.AlertType.INFORMATION, "Item Saved!").show();
                    loadAll("");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went Wrong!").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                if (new DataAccessCode().updateItem(D1)) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Updated!").show();
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
        txtCode.clear();
        txtDescription.clear();
        txtQty.clear();
        txtUnitprice.clear();
    }

    private void loadAll(String s) {
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();
        try {
            for (Item i : new DataAccessCode().allItems()) {
                Button btn = new Button("Delete");
                ItemTM tm = new ItemTM(
                        i.getCode(), i.getDescription(), i.getQtyOnHand(), i.getUnitPrice(), btn
                );


                /*ItemBtn.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are yiu sure?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> type = alert.showAndWait();
                    if (type.get() == ButtonType.YES) {
                        try {
                            if (new DataAccessCode().deleteItem(i.getCode())) {
                                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted!").show();
                                loadAll("");
                            } else {
                                new Alert(Alert.AlertType.INFORMATION, "Somthing went worng!").show();
                            }
                        } catch (ClassNotFoundException | SQLException ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                        }
                    }

                });*/
                tmList.add(tm);
            }
            itemTBL.setItems(tmList);
        }catch (SQLException | ClassNotFoundException e){
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) ItemContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader
                .load(getClass().getResource("../view/DashboardForm.fxml"))));
    }
}
