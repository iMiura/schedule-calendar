import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {SERVER_API_URL} from "../../app.constant";

export class AuthInterceptor implements HttpInterceptor {

  constructor(private localStorage: LocalStorageService, private sessionStorage: SessionStorageService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!request || !request.url || (/^http/.test(request.url)) || request.url.startsWith(SERVER_API_URL + '/login') || request.url.startsWith('SERVER_API_URL + /client')) {
      return next.handle(request);
    }
    const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');

    if (!!token) {
      request = request.clone({
        setHeaders: {
          Authorization: 'Bearer ' + token,

        }
      });
    }
    return next.handle(request);
  }

}
