/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mj.hackpsusp25;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
/**
 *
 * @author icy
 */
public class BarcodeInterface {
    private static final String API_URL = "https://world.openfoodfacts.org/api/v0/product/";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    
    
    public static ItemEntry getProduct(String barcode) throws IOException{
        String url = API_URL + barcode + ".json";
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            String jsonResponse = response.body().string();
            ProductResponse productResponse = gson.fromJson(jsonResponse, ProductResponse.class);
            
            //Map<String, Object> jsonMap = gson.fromJson(jsonResponse, Map.class);
            //System.out.println("\nAvailable JSON Fields:");
            //printJsonFields(jsonMap, ""); // Recursive function to print all keys

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonObject product = jsonObject.getAsJsonObject("product");
            
            if (productResponse.getStatus() != 1) {
                throw new IOException("Product not found for barcode: " + barcode);
            }
           
            ItemEntry item = new ItemEntry(barcode, productResponse.getProduct().getProductName(),  productResponse.getProduct().getBrands());
            if(product.has("image_url")){
                item.setImageUrl(product.get("image_url").getAsString());
            }
            return item;
            
        }
        
    }
    
     private static void printJsonFields(Map<String, Object> map, String prefix) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (value instanceof Map) {
                printJsonFields((Map<String, Object>) value, fullKey); // Recursively print nested objects
            } else {
                System.out.println(fullKey);
            }
        }
    }
    
    class ProductResponse {
    private int status;
    private Product product;
    
    // status == 1 means a product was found
    public int getStatus() {
        return status;
    }
    
    public Product getProduct() {
        return product;
    }
}

// Basic product class (customize fields as needed)
class Product {
    private String product_name;
    private String brands;
    private String ingredients_text;
    //private String image
    
    public String getProductName() {
        return product_name;
    }
    
    public String getBrands() {
        return brands;
    }
    
    public String getIngredients() {
        return ingredients_text;
    }
}
    
}
