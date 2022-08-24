package com.scheduleservice.googlesheets.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.scheduleservice.googlesheets.config.ConstantPropertiesConfig;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.util.DateUtil;
import java.util.Date;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * jwt service
 *
 * @author :keisho
 */
@Service
public class JwtService {

    @Autowired
    private ConstantPropertiesConfig constant;

    /**
     * header
     *
     * @param request req
     * @return header
     */
    public String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(CommonConstant.AUTHORIZATION);
    }

    /**
     * token
     *
     * @param username   ユーザ名
     * @param rememberMe 情報保持フラグ（True：保持、False：保持しない）
     * @return jwt token
     */
    public String createAccessToken(String username, boolean rememberMe, String roleId) {
        // 失効時間を取得
        long expiresAt = Long.parseLong(rememberMe ? constant.getJwtRememberMeExpiresAt() : constant.getJwtNormalExpiresAt());
        // アルゴリズム生成
        Algorithm algorithm = Algorithm.HMAC256(constant.getJwtSecretKey());
        // jwt返却
        return CommonConstant.BEARER + JWT
                .create()
                .withClaim("username", username)
                .withClaim("rememberMe", rememberMe)
                .withClaim("roleId", roleId)
                .withClaim("t", DateUtil.getIntUnixMilli())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresAt * 1000))
                .sign(algorithm);
    }

    /**
     * token検証
     *
     * @param token 対象token
     * @return 検証結果（True：有効、False：無効）
     */
    public boolean verify(String token) {
        try {
            // パスワードでJWTバリデータ作成
            Algorithm algorithm = Algorithm.HMAC256(constant.getJwtSecretKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", getUsername(token))
                    .withClaim("rememberMe", getRememberMe(token))
                    .build();
            // TOKEN検証
            token = getToken(token);
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Token内情報取得（secretなし）
     *
     * @return tokenに含まれたユーザ名
     */
    public String getUsername(String token) {
        return getClaimField(token, "username");
    }

    /**
     * Token内情報取得（secretなし）
     *
     * @return tokenに含まれた権限ID
     */
    public String getRoleId(String token) {
        return getClaimField(token, "roleId");
    }

    /**
     * Token内容情報取得（Key指定）
     *
     * @param token token
     * @param key   key
     * @return value
     */
    public String getClaimField(String token, String key) {
        try {
            token = getToken(token);
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            throw new AuthenticationException("リクエスト無効");
        }
    }

    /**
     * Token内容情報取得（sessionKey）
     */
    public boolean getRememberMe(String token) throws JWTDecodeException {
        try {
            token = getToken(token);
            return Optional.ofNullable(JWT.decode(token).getClaim("rememberMe").asBoolean()).orElse(false);
        } catch (JWTDecodeException e) {
            throw new AuthenticationException("リクエスト無効");
        }
    }

    private String getToken(String token) {
        return token.substring(7);
    }
}
