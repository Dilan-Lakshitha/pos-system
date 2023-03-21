package lk.ise.log.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ise.log.dao.DataAccessCode;
import lk.ise.log.entity.Customer;
import lk.ise.log.entity.Item;
import lk.ise.log.entity.Order;
import lk.ise.log.entity.OrderDetails;
import lk.ise.log.view.tm.CartTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PlaceOderFormController {
    public ComboBox<String> cmbCustomerId;
    public TextField txtCustomerName;
    public TextField txtAddress;
    public TextField txtSalary;
    public ComboBox<String> cmbItemCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TableView<CartTm> tblCart;
    public TableColumn colId;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOption;
    public Label lblOrderId;
    public TextField txtRequsetqty;
    public Label LblTotal;
    @FXML
    private AnchorPane context;


    public void initialize(){
        //..................
        colId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        //..................
        loadCustomerId();
        loadItemCode();
        loadOrderId();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if ( newValue!= null) {
            setCustomerData(newValue);
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                setItemData(newValue);
            }
        });
    }

    private void loadOrderId() {
        try {
            List<Order> orderList=DataAccessCode.allOrder();
            if (new DataAccessCode().allOrder().size() > 0) {
                Order order = orderList.get(orderList.size() - 1);
                String selectedOrderId = order.getOrderId();
                String splitId= selectedOrderId.split("[A-Z]")[1];
                int i=Integer.parseInt(splitId);
                i++;
                lblOrderId.setText("D"+i);
            } else {
                lblOrderId.setText("D1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setItemData(String code) {
        Item item = null;
        try {
            item = new DataAccessCode().allItems().stream().filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
            if (item!=null){
                txtDescription.setText(item.getDescription());
                txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));

            }else{
                new Alert(Alert.AlertType.WARNING,"Not Found").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadItemCode() {
        List<Item> itemList = null;
        try {
            itemList = new DataAccessCode().allItems();
            for(Item data:itemList){
                cmbItemCode.getItems().add(data.getCode());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




    private void setCustomerData(String id) {
        try{
            System.out.println(id);
            Customer customer= new DataAccessCode().allCustomer().stream().filter(e ->e.getId().equals(id)).findFirst().orElse(null);
            System.out.println(customer);
            if (customer!=null){
                txtCustomerName.setText(customer.getName());
                txtAddress.setText(customer.getAddress());
                txtSalary.setText(String.valueOf(customer.getSalary()));
                txtSalary.setText(customer.getSalary() + "");
            }else {
                new Alert(Alert.AlertType.WARNING,"Not Found").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadCustomerId(){
        List<Customer> customerList = null;
        try {
            customerList = new DataAccessCode().allCustomer();
            for ( Customer data : customerList) {
                cmbCustomerId.getItems().add(data.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    ObservableList<CartTm> tmList=FXCollections.observableArrayList();
    public void addToCartOnAction(ActionEvent actionEvent) {
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtRequsetqty.getText());
        double total = unitPrice * qty;

        if (isExists(cmbItemCode.getValue())) {
            for (CartTm t : tmList
            ) {
                if (t.getCode().equals(cmbItemCode.getValue())) {
                    t.setQty(t.getQty() + qty);
                    t.setTotal(t.getTotal() + total);
                    tblCart.refresh();
                }
            }
        }else {

            Button btn = new Button("Delete");
            CartTm tm = new CartTm(cmbItemCode.getValue(),
                    txtDescription.getText(), unitPrice, qty, total, btn);

            btn.setOnAction(event -> {
                tmList.remove(tm);
                tblCart.refresh();
                calculateTOTAL();

            });


            tmList.add(tm);
        }
        clear();
        tblCart.setItems(tmList);
        calculateTOTAL();
    }

    private void calculateTOTAL() {
        double total = 0;
        for (CartTm tm : tmList) {
            total += tm.getTotal();
        }
        LblTotal.setText(String.valueOf(total));
    }

    private boolean isExists(Object code){
        Optional<CartTm> selectedCartTm =
                tmList.stream().filter(e -> e.getCode().equals(code)).findFirst();
        return selectedCartTm.isPresent();
    }
    private void clear(){
        cmbItemCode.setValue(null);
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtRequsetqty.clear();
    }
    public void BackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader
                .load(getClass().getResource("../view/DashboardForm.fxml"))));
    }


    public void SaveOrder(ActionEvent actionEvent) {
        ArrayList<OrderDetails> products=new ArrayList<>();
        System.out.println("do");
        try {
        for (CartTm tm:tmList){
            products.add(new OrderDetails(tm.getCode(), tm.getUnitPrice(),tm.getQty()));
            manageQty(tm.getCode(), tm.getQty());
        }

        Order order= new Order(lblOrderId.getText(),cmbCustomerId.getValue(),new Date(),
                Double.parseDouble(LblTotal.getText()),products);

        new DataAccessCode().SaveOrder(order);

        new Alert(Alert.AlertType.INFORMATION,"order completed").show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadOrderId();
        tmList.clear();
        tblCart.refresh();
        lblOrderId.setText(String.valueOf(0));
    }

    private void manageQty(String code ,int qty) throws SQLException, ClassNotFoundException {
        List<Item> itemList=null;
                itemList=new DataAccessCode().allItems();
        for(Item i:itemList){
            if(i.getCode().equals(code)){
                i.setQtyOnHand(i.getQtyOnHand()-qty);
                return;

            }
        }
    }

}
