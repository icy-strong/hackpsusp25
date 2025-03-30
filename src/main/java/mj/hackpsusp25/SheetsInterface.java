/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mj.hackpsusp25;

/**
 *
 * @author icy
 */
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SheetsInterface {
    private static final String APPLICATION_NAME = "Google Sheets API Java";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_FILE = "/Users/icy/Downloads/inventorydb.json"; // <-- Update this

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE))
                .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void readFromSheet(Sheets service, String spreadsheetId, String range) throws IOException {
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List<Object> row : values) {
                System.out.println(row);
            }
        }
    }

    public static void writeToSheet(Sheets service, String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);
        service.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")  // Options: "RAW" or "USER_ENTERED"
                .execute();
        System.out.println("Data successfully written to " + range);
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        String spreadsheetId = "1be1eaIzrXCHs7HBkhr41Gq1yv5I7v-mJ8lXLVPIaL08";  // <-- Replace with your Google Sheet ID

        // ✅ 1️⃣ Read existing data
        //String readRange = "Sheet1!A1:D10"; // Adjust as needed
        //readFromSheet(service, spreadsheetId, readRange);

        // ✅ 2️⃣ Write new data
        String writeRange = "Shopping List!A2"; // Start writing from A2
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("Item Name", "Category", "Price", "Stock"),
                Arrays.asList("Laptop", "Electronics", "1000", "5"),
                Arrays.asList("Phone", "Electronics", "500", "10")
        );
        writeToSheet(service, spreadsheetId, writeRange, data);
    }
}

