import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class OperateService {

  constructor(private http: HttpClient) {
  }

  findWorkTime(ym, taskId): Promise<any> {
    return this.http.get(SERVER_API_URL + '/findWorkTime', {params: {ym, taskId}}).toPromise();
  }

  updateStart(range, ym, taskId, status, finalChangeDate): Promise<any> {
    return this.http.post(SERVER_API_URL + '/updateStart', {}, {observe: 'response', params: {range, ym, taskId, status, finalChangeDate}}).toPromise();
  }

  updateTimeInfo(range, ym, timeRecordId, status, finalChangeDate): Promise<any> {
    return this.http.post(SERVER_API_URL + '/updateTimeInfo', {}, {observe: 'response', params: {range, ym, timeRecordId,  status, finalChangeDate}}).toPromise();
  }
}
