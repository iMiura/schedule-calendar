import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PlatformLocation} from '@angular/common';
import {LoginService} from "../../../core/auth/login.service";
import {PrincipalService} from 'src/app/core/auth/principal.service';
import {LocalStorageService} from "ngx-webstorage";

declare let google: any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  animations: []
})
export class NavbarComponent implements OnInit, OnDestroy {

  userIdentity: any;
  userName: any;

  constructor(private router: Router,
              private activeRoute: ActivatedRoute,
              private location: PlatformLocation,
              private localStorage: LocalStorageService,
              private principalService: PrincipalService,
              private loginService: LoginService) {
  }

  ngOnInit(): void {
    const that = this;
    that.userIdentity = this.localStorage.retrieve('userIdentity');
    if (that.userIdentity) {
      this.userName = that.userIdentity.userName;
    }
    const button = document.getElementById('signout_button');
    button.onclick = () => {
      google.accounts.id.disableAutoSelect();
      that.loginService.logout().then(() => {
        this.router.navigate(['/login']).then(() => {
        });
      });
    }
  }

  ngOnDestroy(): void {
    this.router.events.subscribe().unsubscribe();
  }

}
