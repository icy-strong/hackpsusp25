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
        
        ItemEntry checkItem = getItemByBarcode(item.getBarcode());
        if(checkItem != null){
            item.setQuantity(item.getQuantity()+checkItem.getQuantity());
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
                    resultSet.getString("IMGURL")
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
    
    public static ItemEntry getItemByName(String name) {
        connection = DBConnection.getConnection();
        try {
            getItemByBarcode = connection.prepareStatement("SELECT * FROM app.items WHERE name = ?");
            getItemByBarcode.setString(1, name);
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
    
    public static void addItemToCategory(String barcode, int categoryId) {
        connection = DBConnection.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO item_categories (barcode, category_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
            );
            stmt.setString(1, barcode);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static void removeItemFromCategory(String barcode, int categoryId) {
        connection = DBConnection.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM item_categories WHERE item_id = ? AND category_id = ?"
            );
            stmt.setString(1, barcode);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ItemEntry> searchItemsByName(String partialName) {
    connection = DBConnection.getConnection();
    ArrayList<ItemEntry> items = new ArrayList<>();
    try {
        // Use the LIKE operator with % to match any part of the name
        PreparedStatement searchItems = connection.prepareStatement(
            "SELECT * FROM app.items WHERE name LIKE ? ORDER BY name"
        );
        searchItems.setString(1, "%" + partialName + "%"); // % allows partial matches before/after the input

        resultSet = searchItems.executeQuery();
        
        while (resultSet.next()) {
            items.add(new ItemEntry(
                resultSet.getString("barcode"),
                resultSet.getString("name"),
                resultSet.getString("brands"),
                resultSet.getInt("quantity"),
                resultSet.getString("IMGURL")
            ));
        }
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
    }
    return items;
}
    
    public static ArrayList<Integer> getCategoriesForItem(String barcode) {
        connection = DBConnection.getConnection();
        ArrayList<Integer> categoryIds = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT category_id FROM item_categories WHERE barcode = ?"
            );
            stmt.setString(1, barcode);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                categoryIds.add(resultSet.getInt("category_id"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return categoryIds;
    }

    public static ArrayList<ItemEntry> searchItems(String namePattern, String category) {
        String query = "SELECT * FROM items WHERE name LIKE ? AND category = ?";

        connection = DBConnection.getConnection(); 
        ArrayList<ItemEntry> items = new ArrayList<>();
        try {
        PreparedStatement stmt = connection.prepareStatement(query);
        
        // Set the parameters
        stmt.setString(1, "%" + namePattern + "%"); // Wildcard for name
        stmt.setString(2, category); // Specific category

        ResultSet resultSet = stmt.executeQuery();

        
        while (resultSet.next()) {
            // Assuming you have an Item class with an appropriate constructor
            ItemEntry item = new ItemEntry( resultSet.getString("barcode"),
                                 resultSet.getString("name"),
                                 resultSet.getString("brands"), resultSet.getInt("quantity"), 
                                 resultSet.getString("IMGURL"));
            items.add(item);
        }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return items;
    }


    public static ArrayList<ItemEntry> getItemsByCategory(int categoryId) {
        connection = DBConnection.getConnection();
        ArrayList<ItemEntry> items = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT i.* FROM items i " +
                "JOIN item_categories ic ON i.id = ic.item_id " +
                "WHERE ic.category_id = ?"
            );
            stmt.setInt(1, categoryId);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                ItemEntry item = new ItemEntry(
                    resultSet.getString("barcode"),
                    resultSet.getString("name"),
                    resultSet.getString("brands"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("image_url")
                );
                items.add(item);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return items;
    }




    
}
