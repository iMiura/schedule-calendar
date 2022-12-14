package com.scheduleservice.googlesheets.release.controller;

import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.release.entity.ReleaseInfo;
import com.scheduleservice.googlesheets.release.service.ReleaseService;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.util.JsonResult;
import io.swagger.annotations.Api;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * リリース情報処理アクションオブジェクト.
 *
 * @author :keisho
 */
@RestController
@Api(tags = "")
@Slf4j
public class ReleaseController {

    @Autowired
    ReleaseService releaseService;
    @Autowired
    private CustomMessageResource messageSource;

    @GetMapping("/releaseInit")
    public ResponseEntity<JsonResult> releaseInit() {
        log.debug("リリース情報検索画面初期化処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // リリース情報
        Map resultMap = releaseService.initReleaseSearch();

        log.debug("リリース情報検索画面初期化処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
    }

    @GetMapping("/getCarModelList")
    public ResponseEntity<JsonResult> getCarModelList(
        @RequestParam("makerCd") Long makerCd) {
        log.debug("車種情報取得処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // 車種情報取得
        Map resultMap = releaseService.getCarModelList(makerCd);

        log.debug("車種情報取得処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
    }

    @GetMapping("/getCarModelGroupList")
    public ResponseEntity<JsonResult> getCarModelGroupList(
        @RequestParam("makerCd") Long makerCd,
        @RequestParam("carModelCd") Long carModelCd) {
        log.debug("車種系統情報取得処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // 車種系統情報取得
        Map resultMap = releaseService.getCarModelGroupList(makerCd, carModelCd);

        log.debug("車種系統情報取得処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
    }

    @PostMapping("/searchRelease")
    public ResponseEntity<JsonResult> searchRelease(
        @RequestBody ReleaseInfo releaseInfo,
        @RequestParam("checked") String checked
    ) {
        log.debug("リリース情報検索処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // リリース情報取得
        Map resultMap = releaseService.getReleaseFile(releaseInfo, checked);

            log.debug("リリース情報検索処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
    }

    @GetMapping("/findReleaseInfo")
    public ResponseEntity<JsonResult> findReleaseInfo(
        @RequestParam("releaseInfoId") String releaseInfoId
    ) {
        log.debug("リリース情報検索処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // リリース情報取得
        Map resultMap = releaseService.getReleaseInfo(releaseInfoId);

        log.debug("リリース情報検索処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
    }

    @PostMapping("/saveRelease")
    public ResponseEntity<JsonResult> saveReleaseInfo(
        @RequestBody ReleaseInfo releaseInfo,
        @RequestParam("releaseInfoId") String releaseInfoId,
        @RequestParam("finalChangeDate") String finalChangeDate
    ) {
        log.debug("リリース情報登録処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        String message = messageSource.getMessage("info.update.success");
        JsonResult jsonResult = JsonResult.success(message);
        if (releaseService.checkReleaseFile()) {
            // スプレッドシートデータを用意されていません
            message = messageSource.getMessage("errors.google_sheets_nofile");
            log.debug(message);
            jsonResult = JsonResult.failed(message);
        } else {
            // リリース情報更新
            releaseInfo.setReleaseInfoId(releaseInfoId);
            if (!releaseService.saveReleaseInfo(releaseInfo, finalChangeDate)) {
                message = messageSource.getMessage("errors.exclusive");
                log.debug(message);
                jsonResult = JsonResult.failed(message);
            }
        }

        log.debug("リリース情報登録処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }

    @PostMapping("/delRelease")
    public ResponseEntity<JsonResult> delReleaseInfo(
        @RequestParam("releaseInfoId") String releaseInfoId,
        @RequestParam("finalChangeDate") String finalChangeDate
    ) {
        log.debug("リリース情報削除処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        String message = messageSource.getMessage("info.update.success");

        JsonResult jsonResult = JsonResult.success(message);
        // リリース情報削除
        if (!releaseService.delReleaseInfo(releaseInfoId, finalChangeDate)) {
            message = messageSource.getMessage("errors.exclusive");
            log.debug(message);
            jsonResult = JsonResult.failed(message);
        }
        log.debug("リリース情報削除処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }
}
