package com.scheduleservice.googlesheets.util;

// Copyright 2022 Google LLC

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.util.*;

import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateFilterView {

    /**
     * Google Sheetsの所定セルに値を投入
     *
     * @param sheetsService Google Sheets
     * @param spreadsheetId Google SheetsのファイルID
     * @param valueInputOption 投入値の型（デフォルト）
     * @return result
     * @throws IOException
     */
    /** Sheet service. */
    private Sheets sheetsService;

    public static AddFilterViewResponse addFilter(Sheets sheetsService, String spreadsheetId, Integer sheetId) throws IOException {

        AddFilterViewResponse result =null;
        try {
//            sheetId = 161445681;
            GridRange gridRange = new GridRange()
                    .setSheetId(sheetId)
                    .setStartRowIndex(0)
                    .setStartColumnIndex(0);

            // 「森本」を指定すると、森本が隠れるので(hiddenData)
            // DBでユーザを引っ張ってきて今ログインしているユーザ以外でループ
            List<String> hiddenData = new ArrayList<>();
            hiddenData.add("森本");

            FilterCriteria filterCriteria = new FilterCriteria().setHiddenValues(hiddenData);

            Map<String, FilterCriteria> criteria = new HashMap<>();
            criteria.put("0", filterCriteria);

            FilterView filter = new FilterView()
                    .setTitle("abcd")
                    .setRange(gridRange)
                    .setCriteria(criteria);




            AddFilterViewRequest req = new AddFilterViewRequest().setFilter(filter);


            //PUT https://sheets.googleapis.com/v4/spreadsheets/{spreadsheetId}/values/{range}?valueInputOption=USER_ENTERED

//            スプレッドシート内の複数の値の範囲を更新するためのリクエスト。
//            これは、Google Sheets API を使用するときに HTTP 経由で送信される JSON を解析/シリアル化する方法を指定する Java データ モデル クラスです。詳細な説明については、 https://developers.google.com/api-client-library/java/google-http-java-client/json を参照してください。

            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest();
//                    .setValueInputOption(valueInputOption);
//                    .setData(data);
            sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();


            // update,deleteがある。
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

