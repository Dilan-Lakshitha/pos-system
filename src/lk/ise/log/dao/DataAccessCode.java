package lk.ise.log.dao;

import lk.ise.log.entity.Customer;
import lk.ise.log.entity.Item;
import lk.ise.log.entity.Order;
import lk.ise.log.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class DataAccessCode {
    

    //..................customer mange code..............
    public boolean saveCustomer(Customer c) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute("INSERT INTO customer VALUES(?,?,?,?)",
                c.getId(),c.getName(),c.getAddress(),c.getSalary());
    }


    public boolean updateCustomer(Customer c) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute("UPDATE customer SET name=?, address=?, salary=? WHERE id=?",
                c.getName(),c.getAddress(),c.getSalary(),c.getId());
    }

    public Customer findCustomer(String id) throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE id=?",id);
        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
                    );
        }
        return null;
    }

    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE id=?",id);
    }

    public static List<Customer> allCustomer() throws SQLException , ClassNotFoundException{
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()){
            customerList.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            ));
        }
        return customerList;
    }

    public boolean saveUser (User u1) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user VALUES(?,?)",
                u1.getUsername(),u1.getPassword());
    }
    public static List<User> allUsers() throws SQLException , ClassNotFoundException{
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user");
        List<User> userList = new ArrayList<>();
        while (resultSet.next()){
            userList.add(new User(
                    resultSet.getString(1),
                    resultSet.getString(2)
            ));
        }
        return userList;
    }
    public User findUser(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE username=?", username);
        if (resultSet.next()){
            return new User(resultSet.getString(1),resultSet.getString(2));
        }
        return null;
    }

    //============item manage code============

    public boolean saveItem(Item i) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO item VALUES(?,?,?,?)",
                i.getCode(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice());
    }
    public boolean updateItem(Item i) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?",
                i.getCode(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice());
    }
    public Item findItem(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE code=?",code);
        if (resultSet.next()) {
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );
        }
        return null;
    }
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE code=?",code);
    }
    public static List<Item> allItems() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item");
        List<Item> itemList = new ArrayList<>();
        while (resultSet.next()){
            itemList.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            ));
        }
        return itemList;
    }

    //============ORDER manage code============
   public boolean SaveOrder(Order i) throws SQLException, ClassNotFoundException {
       return CrudUtil.execute("INSERT INTO customer VALUES(?,?,?,?)",
               i.getOrderId(),i.getCustomer(),i.getDate(),i.getTotal());
   }
    public static List<Order> allOrder() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders");
        List<Order> orderList = new ArrayList<>();
        while (resultSet.next()){
            orderList.add(new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    (Date) resultSet.getDate(3),
                    resultSet.getDouble(4)
            ));
        }
        return orderList;
    }

}
