package com.scheduleservice.googlesheets.util;// Copyright 2022 Google LLC

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateValues {

    /**
     * Google Sheetsの所定セルに値を投入
     *
     * @param sheetsService Google Sheets
     * @param spreadsheetId Google SheetsのファイルID
     * @param range セル指定（例：A1:B1）
     * @param valueInputOption 投入値の型（デフォルト）
     * @param values 投入値（リスト）
     * @return result
     * @throws IOException
     */
    public static UpdateValuesResponse updateValues(Sheets sheetsService,
                                                    String spreadsheetId,
                                                    List<String> range,
                                                    String valueInputOption,
                                                    List values)
        throws IOException {

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
            sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                log.debug("Spreadsheet not found with id '%s'.\n",spreadsheetId);
                System.out.printf("Spreadsheet not found with id '%s'.\n",spreadsheetId);
            } else {
                throw e;
            }
        } catch (IOException e) {
            throw e;
        }
        return result;
    }
}
