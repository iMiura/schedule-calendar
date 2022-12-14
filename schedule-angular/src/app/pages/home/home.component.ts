import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {NzButtonSize} from "ng-zorro-antd/button";
import {DomSanitizer} from "@angular/platform-browser";
import * as moment from "moment";
import {HomeService} from "./home.service";
import {ResponseEnum} from "../../response.enum";
import {NzMessageService} from "ng-zorro-antd/message";
import {NzModalService} from 'ng-zorro-antd/modal';
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "../message/message.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  radioValue = 0;
  size: NzButtonSize = 'small';
  google_sheets_src: any;

  message: any;
  calendarYm: any;
  permission: any;
  deployedYm: any;
  startYm: any;
  ymShow: any;
  listYm: any;
  finalChangeDate: any;
  userIdentity: any;
  userList: any;
  currentUserId: any;
  listUserId: any;
  userId: any;
  isFilter: any;

  constructor(private http: HttpClient,
              private router: Router,
              private modal: NzModalService,
              private nzMessage: NzMessageService,
              private messageService: MessageService,
              private homeService: HomeService,
              private localStorage: LocalStorageService,
              private sanitizer: DomSanitizer) {

  }

  ngOnInit(): void {
    this.message = null;
    this.calendarYm = '';
    this.listYm = '';
    this.currentUserId = '';
    this.listUserId = '';
    this.userId = '';
    if (this.localStorage.retrieve('listYm')) {
      this.listYm = this.localStorage.retrieve('listYm');
    }
    if (this.localStorage.retrieve('listUserId')) {
      this.listUserId = this.localStorage.retrieve('listUserId');
    }
    this.getUserList();
    this.messageService.get().subscribe(data => {
      this.radioValue = data.code;
      if (this.radioValue < 3) {
        this.changeList();
      }
    });
  }

  changeList() {
    // カレンダー展開の場合
    if (this.radioValue == 2) {
      this.calendarYm = '';
    } else {
      this.calendarYm = this.listYm;
    }
    // タスクリストベース進捗画面の場合
    if (this.radioValue == 0) {
      this.currentUserId = this.listUserId;
    } else {
      this.currentUserId = '';
    }
    if (this.currentUserId) {
      this.isFilter = true;
    } else {
      this.isFilter = false;
    }
    this.getSheet();
  }

  doPrevYm() {
    this.calendarYm = moment(this.calendarYm + '01').add(-1, 'months').format('YYYYMM');
    this.getSheet();
  }

  doCurrentYm() {
    this.calendarYm = '';
    this.getSheet();
  }

  doNextYm() {
    this.calendarYm = moment(this.calendarYm + '01').add(1, 'months').format('YYYYMM');
    this.getSheet();
  }

  private getSheet() {
    this.google_sheets_src = '';
    this.finalChangeDate = '';
    this.homeService.showSheet(this.calendarYm, this.radioValue, this.currentUserId).then(res => {
      this.permission = res.result.permission;
      this.calendarYm = res.result.calendarYm;
      this.ymShow = moment(this.calendarYm + '01').format('YYYY年MM月度');
      this.deployedYm = res.result.deployedYm;
      this.startYm = res.result.startYm;
      this.message = res.result.message;
      if (this.message == null) {
        this.google_sheets_src = this.sanitizer.bypassSecurityTrustResourceUrl(res.result.listUrl);
        // Fvid登録無しユーザは、画面表示調整しない
        if (this.google_sheets_src.changingThisBreaksApplicationSecurity.includes('fvid')) {
          this.isFilter = true;
        } else {
          this.isFilter = false;
        }
      }
      // カレンダー展開の場合
      if (this.radioValue == 2) {
        this.finalChangeDate = res.result.finalChangeDate;
      } else {
        this.listYm = this.calendarYm;
        this.localStorage.store('listYm', this.listYm);
      }
    });
  }

  showConfirm(event): void {
    const that = this;
    that.modal.confirm({
      nzTitle: '<i>カレンダー展開を確定します。よろしいですか？</i>',
      nzContent: '<b></b>',
      nzOkText: '確定',
      nzOkType: 'primary',
      nzOkDanger: true,
      nzOnOk: () => that.doDeploye(event, that),
      nzCancelText: '取消',
    });
  }

  doDeploye(event, that) {
    that.deployedYm = that.calendarYm;
    that.homeService.calendarDeploye(that.deployedYm, that.finalChangeDate).then(res => {
      // 展開成功
      if (res.body.code === ResponseEnum.SUCCESS) {
        that.nzMessage.success(res.body.msg);
        let deployeButton = event.target;
        if (!(deployeButton instanceof HTMLButtonElement)) {
          deployeButton = event.target.parentNode;
        }
        deployeButton.disabled = true;
        return;
      }
      // 展開失敗
      if (res.body.code === ResponseEnum.FAILED) {
        that.nzMessage.error(res.body.msg);
        return;
      }
    });
  }

  private getUserList() {
    this.homeService.showUserList().then(res => {
      this.userList = res.result.userList;
    });
  }

  private setSheetFvid() {
    this.currentUserId = this.userId;
    this.listUserId = this.userId;
    this.localStorage.store('listUserId', this.listUserId);
    if (this.userId) {
      this.isFilter = true;
    } else {
      this.isFilter = false;
    }
    this.getSheet();
  }
}
