import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SERVER_API_URL} from "../../app.constant";

@Injectable({providedIn: 'root'})
export class CredentialService {

  constructor(private http: HttpClient) {
  }

  getCredential(code): Promise<any> {
    return this.http.post(SERVER_API_URL + '/account/createCredential', {}, {observe: 'response', params: {code}}).toPromise();
  }
}
