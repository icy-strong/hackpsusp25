/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mj.hackpsusp25;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author icy
 */
public class Hackpsusp25 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while(true){
        String barcode = scanner.nextLine();

        
        try {
            ItemEntry it = BarcodeInterface.getProduct(barcode);
            System.out.println("Name: " + it.getName());
        } catch (IOException e) {
            System.err.println("Error retrieving product: " + e.getMessage());
        }
    }
    }
}

