import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class AuthJwtService {

  constructor(private http: HttpClient,
              private sessionStorage: SessionStorageService,
              private localStorage: LocalStorageService) {
  }

  /**
   * ログイン
   */
  login(credential: any, ip: any, browser: any): Promise<any> {
    const that = this;
    return this.http
      .post(SERVER_API_URL + '/authentication', {}, {observe: 'response', params: {credential, ip, browser}})
      .pipe(map(authenticateSuccess.bind(this)))
      .toPromise();

    function authenticateSuccess(resp) {
      const bearerToken = resp.headers.get('Authorization');
      if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
        const jwt = bearerToken.slice(7, bearerToken.length);
        that.storeAuthenticationToken(jwt, true);
      }
      return resp;
    }
  }

  /**
   * トークンを保存する
   * @param jwt jwt
   * @param rememberMe rememberMe
   */
  storeAuthenticationToken(jwt, rememberMe) {
    if (rememberMe) {
      this.localStorage.store('authenticationToken', jwt);
    } else {
      this.sessionStorage.store('authenticationToken', jwt);
    }
  }

  /**
   * ログアウト
   */
  logout(): Observable<any> {
    return new Observable(observer => {
      this.localStorage.clear('authenticationToken');
      this.sessionStorage.clear('authenticationToken');
      observer.complete();
    });
  }

}
