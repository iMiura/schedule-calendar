import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {PrincipalService} from '../auth/principal.service';

@Injectable({
  providedIn: 'root',
})
export class LoginInterceptor implements CanActivate {

  constructor(private principalService: PrincipalService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!this.principalService.hasToken()) {
      return true;
    }

    return this.principalService.identity().then(ret => {
      if (ret) {
        this.router.navigate([''], {
          skipLocationChange: false,
        });
        return false;
      }
      return true;
    });
  }
}
