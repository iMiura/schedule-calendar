package com.scheduleservice.googlesheets.util;// Copyright 2022 Google LLC

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.scheduleservice.googlesheets.login.controller.LoginController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpdateValues {

    /**
     * Google Sheetsの所定セルに値を投入
     *
     * @param userId ユーザID
     * @param appName APPLICATION_NAME
     * @param spreadsheetId Google SheetsのファイルID
     * @param range セル指定（例：A1:B1）
     * @param valueInputOption 投入値の型（デフォルト）
     * @param values 投入値（リスト）
     * @return result
     * @throws IOException
     */
    public static UpdateValuesResponse updateValues(String userId,
                                                    String filePath,
                                                    String tokensFilePath,
                                                    String appName,
                                                    String spreadsheetId,
                                                    List<String> range,
                                                    String valueInputOption,
                                                    List values)
        throws IOException {

        // Create the sheets API client
        Sheets service = new Sheets.Builder(new NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            authorize(userId, filePath, tokensFilePath))
            .setApplicationName(appName)
            .build();

        UpdateValuesResponse result =null;
        try {

            List<ValueRange> data = new ArrayList<>();
            for (int i = 0; i <range.size(); i++) {
                if (values.get(i) != null) {
                    data.add(new ValueRange()
                        .setRange(range.get(i))
                        .setValues(Arrays.asList(Arrays.asList(values.get(i)))));
                }
            }
            // Additional ranges to update ...
            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                .setValueInputOption(valueInputOption)
                .setData(data);
            service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n",spreadsheetId);
            } else {
                throw e;
            }
        } catch (IOException e) {
            throw e;
        }
        return result;
    }

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @param userId ユーザID
     * @return Credential
     * @throws IOException
     */
    private static Credential authorize(String userId, String filePath, String tokensFilePath) throws IOException {

        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
            new InputStreamReader(LoginController.class.getResourceAsStream(filePath)));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensFilePath)))
            .setAccessType("offline")
            .build();
        Credential credential = flow.loadCredential(userId);
        return credential;
    }
}
