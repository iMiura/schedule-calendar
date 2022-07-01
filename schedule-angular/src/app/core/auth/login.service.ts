import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthJwtService} from './auth-jwt.service';
import {ResponseEnum} from '../../response.enum';
import {PrincipalService} from './principal.service';
import {AccountService} from './account.service';
import {SessionStorageService} from 'ngx-webstorage';

@Injectable({providedIn: 'root'})
export class LoginService {

  constructor(private principal: PrincipalService,
              private authJwtService: AuthJwtService,
              private http: HttpClient,
              private sessionStorageService: SessionStorageService,
              private accountService: AccountService) {
  }

  /**
   * ログイン
   */
  login(credentials: any, ip: any, browser: any): Promise<any> {
    return this.authJwtService.login(credentials, ip, browser).then(result => {
      if (result.body.code !== ResponseEnum.SUCCESS) {
        return result;
      }
      return this.principal.identity(true).then(() => {
        return result;
      });
    });
  }

  /**
   * ログアウト
   */
  logout() {
    return new Promise((resolve, reject) => {
      this.accountService.logout().then(result => {
        if (result.body.code !== ResponseEnum.SUCCESS) {
          reject(result.body.msg);
          return;
        }
        this.authJwtService.logout().subscribe();
        this.principal.logout();
        resolve();
      });
    });
  }

}
