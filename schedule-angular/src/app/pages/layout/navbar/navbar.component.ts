import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PlatformLocation} from '@angular/common';
import {LoginService} from "../../../core/auth/login.service";
import {LocalStorageService} from "ngx-webstorage";
import {NzModalService} from "ng-zorro-antd/modal";
import {MessageService, Msg} from "../../message/message.service";

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
  roleName: any;
  radioValue: any;
  permission: any;
  teamId: any;
  teamName: any;
  radioLabel = ['タスクリストベース進捗画面', '業務フローベース進捗画面', 'カレンダー展開画面', '新車入力画面', 'みどり画面', 'リリース管理画面']

  constructor(private router: Router,
              private activeRoute: ActivatedRoute,
              private modal: NzModalService,
              private location: PlatformLocation,
              private localStorage: LocalStorageService,
              private loginService: LoginService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    const that = this;
    that.userIdentity = this.localStorage.retrieve('userIdentity');
    if (that.userIdentity) {
      this.userName = that.userIdentity.userName;
      this.roleName = that.userIdentity.roleName;
      this.teamId = that.userIdentity.teamId;
      this.teamName = this.userIdentity.teamName;
      this.permission = that.userIdentity.userPermission;
      this.radioValue = 0;
      if (this.teamId == 1) {
        if (this.permission == 0 || this.permission == 1) {
          this.radioValue = 2;
          setTimeout(function() {
            that.messageService.send(new Msg(that.radioValue));
          }, 100);
        }
      }
      if (this.teamId == 2) {
        this.radioValue = 5;
      }
    }
    this.messageService.get().subscribe(data => {
      this.radioValue = data.code;
    });
  }

  ngOnDestroy(): void {
    this.router.events.subscribe().unsubscribe();
  }

  changeList() {
    const that = this;
    if (this.radioValue == 3) {
      this.router.navigate(['/home/newCar'], {
        skipLocationChange: false,
      });
    }
    if (this.radioValue == 5) {
      this.router.navigate(['/release'], {
        skipLocationChange: false,
      });
    }
    if (this.radioValue < 3) {
      this.teamName = this.userIdentity.teamName;
      this.router.navigate(['/home'], {
        skipLocationChange: false,
      });
      setTimeout(function() {
        that.messageService.send(new Msg(that.radioValue));
      }, 100);
    }
  }

  logout() {
    google.accounts.id.disableAutoSelect();
    this.loginService.logout().then(() => {
      this.router.navigate(['/login']).then(() => {
      });
    });
  }
}
