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
    private static int id = 0;

    // Add a new category if it doesn't already exist
    public static void addCategory(String categoryName) {
        connection = DBConnection.getConnection();
        
        // Check if category exists first
        if (getCategoryId(categoryName) != -1) {
            System.out.println("Category already exists: " + categoryName);
            return;
        }

        try {
            addCategory = connection.prepareStatement(
                "INSERT INTO app.categories (category_id), (name) VALUES (?)"
            );
            addCategory.setInt(1, id);
            id++;
            addCategory.setString(2, categoryName);
            addCategory.executeUpdate();
            System.out.println("Category added: " + categoryName);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // Get category ID by name (returns -1 if not found)
    public static int getCategoryId(String categoryName) {
        connection = DBConnection.getConnection();
        int categoryId = -1;

        try {
            getCategoryId = connection.prepareStatement(
                "SELECT id FROM app.categories WHERE name = ?"
            );
            getCategoryId.setString(1, categoryName);
            resultSet = getCategoryId.executeQuery();

            if (resultSet.next()) {
                categoryId = resultSet.getInt("id");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return categoryId;
    }
    
    public static String getCategoryName(Integer categoryID) {
        connection = DBConnection.getConnection();
        String categoryName = null;

        try {
            getCategoryId = connection.prepareStatement(
                "SELECT id FROM app.categories WHERE id = ?"
            );
            getCategoryId.setInt(1, categoryID);
            resultSet = getCategoryId.executeQuery();

            if (resultSet.next()) {
                categoryName = resultSet.getString("name");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return categoryName;
    }
    
    public static Integer getCategoryIdByName(String categoryName) {
        connection = DBConnection.getConnection();
        Integer categoryId = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT id FROM app.categories WHERE name = ?"
            );
            stmt.setString(1, categoryName);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                categoryId = resultSet.getInt("id");
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
