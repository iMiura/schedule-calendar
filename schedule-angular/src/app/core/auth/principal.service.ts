import {Injectable} from '@angular/core';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {ResponseEnum} from '../../response.enum';
import {AccountService} from './account.service';

@Injectable({providedIn: 'root'})
export class PrincipalService {

  private authenticated = false;
  private userIdentity: any;
  private permissions: any;

  constructor(private accountService: AccountService,
              private localStorage: LocalStorageService,
              private sessionStorage: SessionStorageService) {
  }

  /**
   * ユーザー情報を取得
   */
  getIdentity() {
    return this.userIdentity;
  }

  identity(force?: boolean): Promise<any> {
    // 認証
    if (force === true) {
      this.userIdentity = undefined;
    }

    if (this.userIdentity) {
      return Promise.resolve(this.userIdentity);
    }

    return this.accountService
      .account().then(response => {
        if (response.body && response.body.state !== ResponseEnum.FAILED) {
          this.userIdentity = response.body.result.userInfo;
          this.permissions = response.body.result.userInfo.userPermission;
          this.authenticated = true;
          this.localStorage.store('userIdentity', this.userIdentity);
        } else {
          this.userIdentity = null;
          this.authenticated = false;
          this.permissions = null;
        }
        return this.userIdentity;
      }).catch(() => {
        this.userIdentity = null;
        this.authenticated = false;
        this.permissions = null;
        return null;
      });
  }

  hasToken() {
    return this.getToken();
  }

  getToken() {
    return this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
  }

  logout() {
    this.authenticated = false;
    this.userIdentity = null;
  }
}
