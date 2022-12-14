import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class ReleaseInfoService {

  constructor(private http: HttpClient) {
  }

  getCarModelList(makerCd): Promise<any> {
    return this.http.get(SERVER_API_URL + '/getCarModelList', {params: {makerCd}}).toPromise();
  }

  getCarModelGroupList(makerCd, carModelCd): Promise<any> {
    return this.http.get(SERVER_API_URL + '/getCarModelGroupList', {params: {makerCd, carModelCd}}).toPromise();
  }

  findReleaseInfo(releaseInfoId): Promise<any> {
    return this.http.get(SERVER_API_URL + '/findReleaseInfo', {params: {releaseInfoId}}).toPromise();
  }

  saveRelease(body, releaseInfoId, finalChangeDate): Promise<any> {
    return this.http.post(SERVER_API_URL + '/saveRelease', body, {observe: 'response', params: {releaseInfoId, finalChangeDate}}).toPromise();
  }

  delRelease(releaseInfoId, finalChangeDate): Promise<any> {
    return this.http.post(SERVER_API_URL + '/delRelease', {}, {params: {releaseInfoId, finalChangeDate}}).toPromise();
  }
}
