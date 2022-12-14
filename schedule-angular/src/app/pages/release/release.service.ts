import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class ReleaseService {

  constructor(private http: HttpClient) {
  }

  searchInit(): Promise<any> {
    return this.http.get(SERVER_API_URL + '/releaseInit', {}).toPromise();
  }

  getCarModelList(makerCd): Promise<any> {
    return this.http.get(SERVER_API_URL + '/getCarModelList', {params: {makerCd}}).toPromise();
  }

  getCarModelGroupList(makerCd, carModelCd): Promise<any> {
    return this.http.get(SERVER_API_URL + '/getCarModelGroupList', {params: {makerCd, carModelCd}}).toPromise();
  }

  searchRelease(makerCd, carModelCd, carModelGroupCd, supportPeriod, salesCategoryL, salesCategoryR, releaseCategory, picId, checked): Promise<any> {
    return this.http.post(SERVER_API_URL + '/searchRelease', {makerCd, carModelCd, carModelGroupCd, supportPeriod, salesCategoryL, salesCategoryR, releaseCategory, picId}, {params: {checked}}).toPromise();
  }
}
