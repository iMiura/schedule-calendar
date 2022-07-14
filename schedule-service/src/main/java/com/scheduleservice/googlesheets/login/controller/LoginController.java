package com.scheduleservice.googlesheets.login.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.login.service.impl.LoginServiceImpl;
import com.scheduleservice.googlesheets.operate.service.impl.OperateServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.security.jwt.JwtService;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザーログイン処理アクションオブジェクト.
 *
 * @author :keisho
 */
@RestController
@Api(tags = "ユーザーログイン")
@Slf4j
public class LoginController {

    @Autowired
    LoginServiceImpl loginService;
    @Autowired
    OperateServiceImpl operateService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomMessageResource messageSource;

    /**
     * ユーザーログイン
     *
     * @return json
     */
    @PostMapping("/authentication")
    public ResponseEntity<JsonResult> authentication(@RequestParam("credential") String credential,
        @RequestParam("ip") String ip, @RequestParam("browser") String browser, ServletRequest request) {

        log.debug("ログイン認証　開始");
        Map userModel = new HashMap();

        final String CLIENT_ID = loginService.getClientId();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
            GsonFactory.getDefaultInstance()).setAudience(Collections.singletonList(CLIENT_ID)).build();
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(credential);
        } catch (GeneralSecurityException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            log.debug(e.getMessage());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            String email = payload.getEmail();

            try {
                // ログイン
                UserInfoEntity userIfno = loginService.login(userId, email);
                log.debug("ログインユーザー：" + userIfno.getUserName() + "、端末情報（IP：" + ip
                    + "、Webブラウザ：" + browser + "）");

                // トークン
                String token = jwtService.createAccessToken(email, true, userId);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", token);
                headers.add("Access-Control-Expose-Headers", CommonConstant.AUTHORIZATION);

                String url = operateService.authorize(userIfno.getGUserId(), request);
                userModel.put("URL", url);

                log.debug("ログイン認証　完了");
                return new ResponseEntity<>(JsonResult.success(userModel), headers, HttpStatus.OK);
            } catch (ServiceException e) {
                return new ResponseEntity<>(JsonResult.failed(e.getMessage()), HttpStatus.OK);
            }
        } else {
            log.debug("ログイン失敗");
            log.debug("ログイン認証　完了");
            return new ResponseEntity<>(JsonResult.failed(messageSource.getMessage("errors.check.login")), HttpStatus.OK);
        }
    }

    /**
     * ログアウト
     *
     * @return ResponseEntity
     */
    @ApiOperation("ログアウト")
    @PostMapping("/logout")
    public ResponseEntity<JsonResult> logout() throws ServiceException {
        log.debug("ログアウト 開始");
        log.debug("ユーザーログアウト：" + SessionUtil.getUserInfo().getUserName());
        log.debug("ログアウト 完了");
        return new ResponseEntity<>(JsonResult.success("ログアウト"), HttpStatus.OK);
    }

    /**
     * クライアントID
     *
     * @return json
     */
    @GetMapping("/client")
    public ResponseEntity<JsonResult> client() {
        Map result = new HashMap();
        result.put("CLIENT_ID", loginService.getClientId());
        return new ResponseEntity<>(JsonResult.success(result), HttpStatus.OK);
    }
}
