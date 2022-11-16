import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class HomeService {

  constructor(private http: HttpClient) {
  }

  showSheet(ym?, urlFlg?, userId?): Promise<any> {
    return this.http.get(SERVER_API_URL + '/showSheet', {params: {ym, urlFlg, userId}}).toPromise();
  }

  calendarDeploye(deployedYm?, finalChangeDate?): Promise<any> {
    return this.http.post(SERVER_API_URL + '/calendarDeploye', {}, {observe: 'response', params: {deployedYm, finalChangeDate}}).toPromise();
  }

  showUserList(): Promise<any> {
    return this.http.get(SERVER_API_URL + '/showUserList', {}).toPromise();
  }

  sendSlack(slackText?): Promise<any> {
    return this.http.get(SERVER_API_URL + '/sendSlack', {params: {slackText}}).toPromise();
  }
}
