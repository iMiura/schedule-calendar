package com.scheduleservice.googlesheets.security.realm;

import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.service.IRoleInfoService;
import com.scheduleservice.googlesheets.repository.service.ITeamInfoService;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
import com.scheduleservice.googlesheets.security.jwt.JwtService;
import com.scheduleservice.googlesheets.security.jwt.JwtToken;
import java.util.Collection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * realmの定義
 *
 * @author :keisho
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private JwtService jwtService;

    @Autowired
    @Lazy
    private IUserInfoService userService;
    @Autowired
    private ITeamInfoService iTeamInfoService;
    @Autowired
    private IRoleInfoService iRoleInfoService;

    /**
     * Shiroエラーのため、メソッドの再定義
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 権限認証（ユーザ、権限）、controller実行時認証を行う（redisは権限情報を記録する）
     * ユーザ権限認証処理を実行時、本メソッドが実行する。例：checkRole,checkPermission
     *
     * @param principals ユーザ情報
     * @return AuthorizationInfo 権限情報
     */
    @SneakyThrows
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // ユーザ情報の取得
        UserInfoEntity user = (UserInfoEntity) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (!StringUtils.hasLength(user.getUserPermission().toString())) {
            // 権限の追加
            simpleAuthorizationInfo.addRole(user.getUserPermission().toString());
        }
        return simpleAuthorizationInfo;
    }

    /**
     * ログイン時のユーザ情報認証(redis存在しない場合)
     * ユーザ名、パスワードの認証を行う。入力不備の場合、エラー。
     *
     * @param token ユーザ名、パスワード
     * @return ユーザ情報 AuthenticationInfo
     * @throws AuthenticationException e
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwtToken = (String) token.getCredentials();
        if (!StringUtils.hasLength(jwtToken)) {
            throw new AuthenticationException("ログイン失敗しました。");
        }

        if (!jwtService.verify(jwtToken)) {
            throw new AuthenticationException("タイムアウトしました。再度ログインしてください。");
        }

        String username = jwtService.getUsername(jwtToken);
        // 取出用户信息
        UserInfoEntity userInfo = userService.getByGmail(username);
        if (userInfo == null) {
            throw new AuthenticationException("入力したユーザ情報が存在しません。");
        }
        String roleId = jwtService.getRoleId(jwtToken);
        userInfo.setGUserId(roleId);
        userInfo.setTeamName(iTeamInfoService.getById(userInfo.getTeamId()).getTeamName());
        userInfo.setRoleName(iRoleInfoService.getById(userInfo.getRoleId()).getRoleName());

        this.setCredentialsMatcher(credentialsMatcher());

        // ユーザ情報をsubjectに保存し、sessionで保持する
        return new SimpleAuthenticationInfo(userInfo, token, getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        UserInfoEntity user = (UserInfoEntity) principals.getPrimaryPrincipal();
        AuthorizationInfo info = getAuthorizationInfo(principals);
        Collection<String> perms = info.getStringPermissions();
        if (perms != null && !perms.isEmpty()) {
            for (String perm : perms) {
                if (perm.equals(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * パスワード認証（JWTToken認証のため、以下の処理は、実装せず、True固定で返却（デフォルト値がFalseのため、Trueを設定しないと、認証エラー）
     */
    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }
}
