/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mj.hackpsusp25;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author icy
 */

public class CategoryQueries {
    private static Connection connection;
    private static PreparedStatement addCategory;
    private static PreparedStatement getCategoryId;
    private static PreparedStatement getAllCategories;
    private static ResultSet resultSet;

    // Add a new category if it doesn't already exist
    public static void addCategory(String categoryName) {
        connection = DBConnection.getConnection();
        
        try {
            addCategory = connection.prepareStatement(
                "INSERT INTO app.categories (name) VALUES (?)"
            );
            addCategory.setString(1, categoryName);
            addCategory.executeUpdate();
            System.out.println("Category added: " + categoryName);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void removeCategory(String categoryName){
        connection = DBConnection.getConnection();
        
        try {
            addCategory = connection.prepareStatement(
                "DELETE FROM app.categories WHERE name = ?"
            );
            addCategory.setString(1, categoryName);
            addCategory.executeUpdate();
            System.out.println("Category removed: " + categoryName);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        try {
            addCategory = connection.prepareStatement(
                "DELETE FROM app.itemcategories WHERE cat_name = ?"
            );
            addCategory.setString(1, categoryName);
            addCategory.executeUpdate();
            System.out.println("Category removed: " + categoryName);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        
    }
    
    public static Integer getCategoryIdByName(String categoryName) {
        connection = DBConnection.getConnection();
        Integer categoryId = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT category_id FROM app.categories WHERE name = ?"
            );
            stmt.setString(1, categoryName);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                categoryId = resultSet.getInt("category_id");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return categoryId;
    }


    // Retrieve all categories
    public static ArrayList<String> getAllCategories() {
        connection = DBConnection.getConnection();
        ArrayList<String> categories = new ArrayList<>();

        try {
            getAllCategories = connection.prepareStatement("SELECT name FROM app.categories");
            resultSet = getAllCategories.executeQuery();

            while (resultSet.next()) {
                categories.add(resultSet.getString("name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return categories;
    }
}
