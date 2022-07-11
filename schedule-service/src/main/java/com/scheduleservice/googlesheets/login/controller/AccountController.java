package com.scheduleservice.googlesheets.login.controller;

import com.scheduleservice.googlesheets.login.service.impl.LoginServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログインユーザー情報処理アクションオブジェクト.
 *
 * @author :keisho
 */
@Controller
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    LoginServiceImpl loginService;

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

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @return json
     */
    @PostMapping("/createCredential")
    public ResponseEntity<JsonResult> createCredential(@RequestParam("code") String code, ServletRequest request) {
        log.debug("Google Sheetsの権限認証チェック 開始");

        loginService.credential(SessionUtil.getUserInfo().getGUserId(), code, request);

        log.debug("Google Sheetsの権限認証チェック 完了");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @return json
     */
    @GetMapping("/credential")
    public String credential(@RequestParam("code") String code, ServletRequest request, Model model) {
        log.debug("Google Sheetsの所定セルに値を投入時の権限認証 開始");

        model.addAttribute("CODE", code);
        model.addAttribute("URL", (String) WebUtils.toHttp(request).getSession(true).getAttribute("URL"));

        log.debug("Google Sheetsの所定セルに値を投入時の権限認証 完了");
        return "index";
    }
}
