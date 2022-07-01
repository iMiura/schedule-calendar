import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class AccountService {
  constructor(private http: HttpClient) {
  }

  /**
   * 認証
   */
  account(): Promise<any> {
    return this.http.post(SERVER_API_URL + '/account/authorization', {}, {observe: 'response'}).toPromise();
  }

  /**
   * ログアウト
   */
  logout(): Promise<any> {
    return this.http.post(SERVER_API_URL + '/logout', {}, {observe: 'response'}).toPromise();
  }

}
