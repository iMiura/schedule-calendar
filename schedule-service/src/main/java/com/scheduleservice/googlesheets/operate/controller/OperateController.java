package com.scheduleservice.googlesheets.operate.controller;

import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.operate.service.OperateService;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作業時間情報処理アクションオブジェクト.
 *
 * @author :keisho
 */
@RestController
@Api(tags = "")
@Slf4j
public class OperateController {

    @Autowired
    OperateService operateService;
    @Autowired
    private IUserInfoService iUserInfoService;

    @Autowired
    private CustomMessageResource messageSource;

    @GetMapping("/findWorkTime")
    public ResponseEntity<JsonResult> findWorkTime(
        @RequestParam("ym") String ym,
        @RequestParam("taskId") Long taskId,
        @RequestParam("taskOwner") String taskOwner) {
        log.debug("作業時間情報処理 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // チームID
        Long teamId = SessionUtil.getUserInfo().getTeamId();

        // 作業時間取得
        Map resultMap = operateService.getWorkTime(teamId, ym, taskId);

        // ログイン者と異なる場合、メッセージ（赤）を表示する
        if (!taskOwner.equals(SessionUtil.getUserInfo().getGmailAddress())) {
            UserInfoEntity userInfo = iUserInfoService.getByGmail(taskOwner);
            if (userInfo != null) {
                resultMap.put("message_owner", userInfo.getUserName());
            } else {
                resultMap.put("message_owner", "登録されてない利用者");
            }
        }

        log.debug("作業時間情報処理 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
        return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
    }

    @PostMapping("/updateStart")
    public ResponseEntity<JsonResult> updateStart(
        @RequestParam("range") String range,
        @RequestParam("ym") String ym,
        @RequestParam("status") int status,
        @RequestParam("taskId") Long taskId,
        @RequestParam("finalChangeDate") String finalChangeDate) {
        log.debug("作業時間情報処理 " + CommonConstant.PROGRESS_STATUS[status] + "ボタン 開始（" + SessionUtil.getUserInfo().getUserName() + "）");

        // 作業時間管理 開始
        if (operateService.updateStart(ym, taskId, range, status, finalChangeDate)) {
            log.debug("作業時間情報処理 " + CommonConstant.PROGRESS_STATUS[status] + "ボタン 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
            return new ResponseEntity<>(JsonResult.success(messageSource.getMessage("info.worktime.update.success",
                CommonConstant.PROGRESS_STATUS[1])), HttpStatus.OK);
        } else {
            log.debug(messageSource.getMessage("errors.exclusive"));
            log.debug("作業時間情報処理 " + CommonConstant.PROGRESS_STATUS[status] + "ボタン 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
            return new ResponseEntity<>(JsonResult.failed(messageSource.getMessage("errors.exclusive")), HttpStatus.OK);
        }
    }

    @PostMapping("/updateTimeInfo")
    public ResponseEntity<JsonResult> updateTimeInfo(
        @RequestParam("range") String range,
        @RequestParam("ym") String ym,
        @RequestParam("timeRecordId") Long timeRecordId,
        @RequestParam("status") int status,
        @RequestParam("finalChangeDate") String finalChangeDate) {
        log.debug("作業時間情報処理 " + CommonConstant.PROGRESS_STATUS[status] + "ボタン 開始（" + SessionUtil.getUserInfo().getUserName() + "）");
        // 作業時間管理 開始
        if (operateService.updateTimeInfo(ym, timeRecordId, range, status, finalChangeDate)) {
            log.debug("作業時間情報処理 " + CommonConstant.PROGRESS_STATUS[status] + "ボタン 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
            return new ResponseEntity<>(JsonResult.success(messageSource.getMessage("info.worktime.update.success",
                CommonConstant.PROGRESS_STATUS[status])), HttpStatus.OK);
        } else {
            log.debug(messageSource.getMessage("errors.exclusive"));
            log.debug("作業時間情報処理 " + CommonConstant.PROGRESS_STATUS[status] + "ボタン 完了（" + SessionUtil.getUserInfo().getUserName() + "）");
            return new ResponseEntity<>(JsonResult.failed(messageSource.getMessage("errors.exclusive")), HttpStatus.OK);
        }
    }

}
