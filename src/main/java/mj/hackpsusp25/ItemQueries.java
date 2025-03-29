package mj.hackpsusp25;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles database operations for ItemEntry objects.
 * 
 * @author icy
 */
public class ItemQueries {
    private static Connection connection;
    private static PreparedStatement addItem;
    private static PreparedStatement getAllItems;
    private static PreparedStatement getItemByBarcode;
    private static PreparedStatement updateItem;
    private static PreparedStatement deleteItem;
    private static ResultSet resultSet;
    
    // Add a new item to the database
    public static void addItem(ItemEntry item) {
        
        if(getItemByBarcode(item.getBarcode()) != null){
            item.setQuantity(item.getQuantity()+1);
            updateItem(item);
            return;
        }
        
        connection = DBConnection.getConnection();
        
        try {
            addItem = connection.prepareStatement(
                "INSERT INTO app.items (barcode, name, brands, quantity, IMGURL) VALUES (?, ?, ?, ?, ?)"
            );
            
            //addItem.setString(1, String.valueOf(id++));
            addItem.setString(1, item.getBarcode());
            addItem.setString(2, item.getName());
            addItem.setString(3, item.getBrands());
            addItem.setInt(4, item.getQuantity());
            addItem.setString(5, item.getImageUrl());
            addItem.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    // Retrieve all items from the database
    public static ArrayList<ItemEntry> getAllItems() {
        connection = DBConnection.getConnection();
        ArrayList<ItemEntry> items = new ArrayList<>();
        try {
            getAllItems = connection.prepareStatement("SELECT * FROM app.items ORDER BY name");
            resultSet = getAllItems.executeQuery();

            while (resultSet.next()) {
                ItemEntry item = new ItemEntry(
                    resultSet.getString("barcode"),
                    resultSet.getString("name"),
                    resultSet.getString("brands"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("imageurl")
                );
                items.add(item);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return items;
    }

    // Retrieve an item by barcode
    public static ItemEntry getItemByBarcode(String barcode) {
        connection = DBConnection.getConnection();
        try {
            getItemByBarcode = connection.prepareStatement("SELECT * FROM app.items WHERE barcode = ?");
            getItemByBarcode.setString(1, barcode);
            resultSet = getItemByBarcode.executeQuery();

            if (resultSet.next()) {
                return new ItemEntry(
                    resultSet.getString("barcode"),
                    resultSet.getString("name"),
                    resultSet.getString("brands"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("IMGURL")
                );
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // Update item details
    public static void updateItem(ItemEntry item) {
        connection = DBConnection.getConnection();
        try {
            updateItem = connection.prepareStatement(
                "UPDATE app.items SET quantity = ? WHERE barcode = ?"
            );

            updateItem.setInt(1, item.getQuantity());
            updateItem.setString(2, item.getBarcode());
            updateItem.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // Delete an item from the database
    public static void deleteItem(String barcode) {
        connection = DBConnection.getConnection();
        try {
            deleteItem = connection.prepareStatement("DELETE FROM app.items WHERE barcode = ?");
            deleteItem.setString(1, barcode);
            deleteItem.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
