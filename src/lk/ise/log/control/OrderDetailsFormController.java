package lk.ise.log.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ise.log.dao.DataAccessCode;
import lk.ise.log.entity.Order;
import lk.ise.log.entity.OrderDetails;
import lk.ise.log.view.tm.OrderDetailsTM;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

public class OrderDetailsFormController {
    public TextField txtId;
    public TextField txtName;
    public TableView<OrderDetailsTM> tblOrderDetails;
    public TableColumn<OrderDetailsTM,String> colItem;
    public TableColumn<OrderDetailsTM,String> colDescription;
    public TableColumn<OrderDetailsTM,Integer> colQty;
    public TableColumn<OrderDetailsTM,Double> colUnitPrice;
    public TextField txtDate;
    public TextField txtCost;

    public void initialize(){
        colItem.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        
    }
    

    public void setOrder(String orderId){
        Optional<Order> order= null;
        try {
            order = Optional.of(DataAccessCode.allOrder().stream().filter(e -> e.getOrderId().equals(orderId)).findFirst().get());
        if(!order.isPresent()){
            new Alert(Alert.AlertType.WARNING,"NOT FOUND").show();
            return;
        }
        txtId.setText(order.get().getOrderId());
            Optional<Order> finalOrder = order;
            txtName.setText(DataAccessCode.allCustomer().stream().
                filter(e->e.getId().equals(finalOrder.get().getCustomer())).
                findFirst().get().getName());
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(order.get().getDate()));
        txtCost.setText(String.valueOf(order.get().getTotal()));
        loadTable(orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTable(String orderId) throws SQLException, ClassNotFoundException {
        Optional<Order> selectedOrder = null;
            selectedOrder = new DataAccessCode().allOrder().stream().filter(e -> e.getOrderId().equals(orderId)).findFirst();
            if (!selectedOrder.isPresent()) {
                new Alert(Alert.AlertType.INFORMATION, "NOT Found").show();
                return;
            }
            ArrayList<OrderDetails> items = selectedOrder.get().getProducts();
            ObservableList<OrderDetailsTM> tmList = FXCollections.observableArrayList();
            for (OrderDetails d : items) {
                tmList.add(new OrderDetailsTM(d.getCode(),
                        DataAccessCode.allItems().stream().filter(e -> e.getCode().equals(d.getCode())).findFirst().get().getDescription(),
                        d.getQty(),
                        d.getUnitPrice())
                );
        }
        tblOrderDetails.setItems(tmList);
    }
}
