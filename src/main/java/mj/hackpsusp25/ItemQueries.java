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
        connection = DBConnection.getConnection();
        
        try {
            addItem = connection.prepareStatement(
                "INSERT INTO app.items (barcode, name, description, brands, quantity, imageUrl) VALUES (?, ?, ?, ?, ?, ?)"
            );
            addItem.setString(1, item.getBarcode());
            addItem.setString(2, item.getName());
            addItem.setString(3, item.getDescription());
            addItem.setString(4, item.getBrands());
            addItem.setInt(5, item.getQuantity());
            addItem.setString(6, item.getImageUrl());
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
                    resultSet.getString("description"),
                    resultSet.getString("brands"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("imageUrl")
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
                    resultSet.getString("description"),
                    resultSet.getString("brands"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("imageUrl")
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
                "UPDATE app.items SET name = ?, description = ?, brands = ?, quantity = ?, imageUrl = ? WHERE barcode = ?"
            );
            updateItem.setString(1, item.getName());
            updateItem.setString(2, item.getDescription());
            updateItem.setString(3, item.getBrands());
            updateItem.setInt(4, item.getQuantity());
            updateItem.setString(5, item.getImageUrl());
            updateItem.setString(6, item.getBarcode());
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
