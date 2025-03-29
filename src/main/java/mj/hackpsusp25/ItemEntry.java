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
    private String description;        // Item description
    private String brands;
    private ArrayList<Integer> categoryIds; // List of category IDs (for multi-category support)
    private int quantity;              // Inventory count
    private String imageUrl;           // Image URL or path
    
    
    
    public ItemEntry(String barcode, String name, String description, String brands,
                     int quantity, String imageUrl) {
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.brands = brands;
        this.categoryIds = new ArrayList<>();
    }
    
    public ItemEntry(String barcode, String name, String description, String brands) {
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.quantity = quantity = 1;
        this.brands = brands;
        this.categoryIds = new ArrayList<>();
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
    public String getDescription() { return description; }
    public ArrayList<Integer> getCategoryIds() { return categoryIds; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
    public String getBrands(){ return brands;}

    // Setters
    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryIds(ArrayList<Integer> categoryIds) { this.categoryIds = categoryIds; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setBrands(String brands) { this.brands = brands; }

}


