package com.scheduleservice.googlesheets.sheets.controller;

import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.sheets.service.SheetsShowService;
import com.scheduleservice.googlesheets.util.JsonResult;
import io.swagger.annotations.Api;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * スプレッドシート情報処理アクションオブジェクト.
 *
 * @author :keisho
 */
@RestController
@Api(tags = "スプレッドシート情報")
@Slf4j
public class SheetsShowController {

    @Autowired
    SheetsShowService sheetsShowService;

    @Autowired
    private CustomMessageResource messageSource;

    /**
     * スプレッドシートファイル表示
     *
     * @return json
     */
    @GetMapping("/showSheet")
    public ResponseEntity<JsonResult> showSheet(
        @RequestParam("ym") String calendarYm, @RequestParam("urlFlg") String urlFlg, @RequestParam("userId") String userId) {
        log.debug("スプレッドシート情報表示" + CommonConstant.RADIO_LABEL[Integer.parseInt(urlFlg)] + " 開始（" + SessionUtil.getUserInfo().getUserName() + "）");
        // スプレッドシート情報取得
        Map result = sheetsShowService.getGoogleSheetsInfo(calendarYm, urlFlg, userId);
        log.debug("スプレッドシート情報表示" + CommonConstant.RADIO_LABEL[Integer.parseInt(urlFlg)] + " 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(result), HttpStatus.OK);
    }

    /**
     * カレンダーリスト表示
     *
     * @return json
     */
    @PostMapping("/calendarDeploye")
    public ResponseEntity<JsonResult> calendarDeploye(@RequestParam("deployedYm") String deployedYm,
            @RequestParam("finalChangeDate") String finalChangeDate) {
        log.debug("スプレッドシート情報展開 開始（" + SessionUtil.getUserInfo().getUserName() + "）");
        if (sheetsShowService.updateCalendarDeployed(deployedYm, finalChangeDate)) {
            log.debug("スプレッドシート情報展開 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
            return new ResponseEntity<>(JsonResult.success(messageSource.getMessage("info.deploye.success")), HttpStatus.OK);
        } else {
            log.debug(messageSource.getMessage("errors.exclusive.calendar.expansion"));
            log.debug("スプレッドシート情報展開 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
            return new ResponseEntity<>(JsonResult.failed(messageSource.getMessage("errors.exclusive.calendar.expansion")), HttpStatus.OK);
        }

    }

    /**
     * フィルタ対象担当者プルダウンの選択肢取得
     *
     * @return json
     */
    @GetMapping("/showUserList")
    public ResponseEntity<JsonResult> showUserList() {
        log.debug("フィルタ対象担当者プルダウンの選択肢取得 開始（" + SessionUtil.getUserInfo().getUserName() + "）");
        // スプレッドシート情報取得
        Map result = sheetsShowService.getUserList();
        log.debug("フィルタ対象担当者プルダウンの選択肢取得 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(result), HttpStatus.OK);
    }
}
