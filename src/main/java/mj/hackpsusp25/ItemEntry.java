package mj.hackpsusp25;

import java.util.ArrayList;

/**
 *
 * @author icy
 */
public class ItemEntry {
   private int itemId;                // Unique ID for internal reference
    private String barcode;            // Barcode for scanning
    private String name;               // Item name
    private String brands;
    private ArrayList<Integer> categoryIds; // List of category IDs (for multi-category support)
    private int quantity;              // Inventory count
    private String imageUrl;           // Image URL or path
    private int desiredQnty;
    
    
    
    public ItemEntry(String barcode, String name, String brands,
                     int quantity, String imageUrl) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.brands = brands;
        this.categoryIds = new ArrayList<>();
        desiredQnty = 0;
    }
    
    public ItemEntry(String barcode, String name, String brands,
                     int quantity, String imageUrl, int desiredQnty) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.brands = brands;
        this.categoryIds = new ArrayList<>();
        this.desiredQnty = desiredQnty;
    }
    
    public ItemEntry(String barcode, String name, String brands) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity = 1;
        this.brands = brands;
        this.categoryIds = new ArrayList<>();
        desiredQnty = 0;
    }
    
    
    public void addCategory(int categoryID){
        
        if(!categoryIds.contains(Integer.valueOf(categoryID))){
            categoryIds.add(Integer.valueOf(categoryID));
        }
    }
    
    public void removeCategory(int categoryID){
        categoryIds.remove(Integer.valueOf(categoryID));
    }

    // Getters
    public int getItemId() { return itemId; }
    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public ArrayList<Integer> getCategoryIds() { return categoryIds; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
    public String getBrands(){ return brands;}
    public int getDesiredQnty(){ return desiredQnty;}

    // Setters
    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public void setName(String name) { this.name = name; }
    public void setCategoryIds(ArrayList<Integer> categoryIds) { this.categoryIds = categoryIds; }
    public void setQuantity(int quantity) { 
        if(quantity>=0){
            this.quantity = quantity; 
        }else{
            this.quantity = 0;
        }
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setBrands(String brands) { this.brands = brands; }
    public void setDesiredQnty(int desiredQnty){ this.desiredQnty = desiredQnty;}

}