package com.scheduleservice.googlesheets.login.controller;

import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ログインユーザー情報処理アクションオブジェクト.
 *
 * @author :keisho
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    /**
     * ログインユーザー情報取得
     *
     * @return json
     */
    @ApiOperation("ログインユーザー情報取得")
    @PostMapping("/authorization")
    public ResponseEntity<JsonResult> authority() {
        log.debug("ログインユーザー情報取得 開始");
        Map<String, Object> map = new HashMap<>(2);

        UserInfoEntity userInfo = SessionUtil.getUserInfo();
        map.put("userInfo", userInfo);
        log.debug("ログインユーザー情報取得 完了");
        return new ResponseEntity<>(JsonResult.success(map), HttpStatus.OK);
    }

}
